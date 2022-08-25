package org.fireflyest.craftdatabase.annotation;

import org.fireflyest.craftdatabase.builder.SQLCreateTable;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fireflyest
 * @since 2022/8/17
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.fireflyest.craftdatabase.annotation.Dao")
public class DaoProcessor extends AbstractProcessor {

    private static final Pattern varPattern = Pattern.compile("\\$\\{([^{]*)}");
    private static final Pattern selectPattern = Pattern.compile("SELECT ([^ ]*)");
    private static final Pattern tablePattern = Pattern.compile("FROM ([^ ]*)");

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Processing database dao...");

        // 当前处理器支持的所有注解种类
        for (TypeElement typeElement : annotations) {
            // 获得被该注解声明的元素
            for (Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
                Dao dao = element.getAnnotation(Dao.class);

                TypeElement interfaceElement = ((TypeElement) element);
                String className = interfaceElement.getQualifiedName().toString();
                String daoName = interfaceElement.getSimpleName().toString();
                String pack = className.substring(0, className.lastIndexOf("."));

                // 头部
                StringBuilder javaFileBuilder = new StringBuilder()
                        .append("package ")
                        .append(pack)
                        .append(";\n\nimport java.util.*;\nimport java.sql.*;\n\npublic class ")
                        .append(daoName)
                        .append("Impl implements ")
                        .append(daoName)
                        .append(" {\n\n\tprivate final String url;");

                // 建表
                javaFileBuilder.append("\n\n\tprivate static final String createTable = \"");
                if (!"".equals(dao.value())){
                    String tableClassName = dao.value();
                    String tableName = TableProcessor.getTableName(tableClassName);
                    SQLCreateTable createTableBuilder = new SQLCreateTable(tableName);
                    for (TableProcessor.ColumnInfo value : TableProcessor.getTableColumns(tableName).values()) {
                        if (value.id){
                            createTableBuilder.id(value.columnName);
                        } else if (value.primary) {
                            createTableBuilder.primary(value.columnName, Objects.requireNonNullElseGet(value.columnDataType, () -> "${" + value.dataType + "}"));
                        } else {
                            createTableBuilder.columns(value.columnName, Objects.requireNonNullElseGet(value.columnDataType, () -> "${" + value.dataType + "}"), value.noNull, value.defaultValue);
                        }
                    }
                    javaFileBuilder.append(createTableBuilder.build().replace("\n", ""));
                }
                javaFileBuilder.append("\";");
                javaFileBuilder.append("\n\n\tpublic java.lang.String getCreateTableSQL(){ return createTable; }");

                // 构造函数
                javaFileBuilder.append("\n\n\tpublic ")
                        .append(daoName)
                        .append("Impl(String url) {\n\t\tthis.url = url;\n\t}\n");

                // 遍历所有方法
                for (Element enclosedElement : interfaceElement.getEnclosedElements()) {
                    if (enclosedElement.getKind() != ElementKind.METHOD) continue;

                    javaFileBuilder.append("\n\t@Override\n\tpublic ");
                    ExecutableElement executableElement = ((ExecutableElement) enclosedElement);
                    // 返回的类型
                    String returnType = executableElement.getReturnType().toString();

                    javaFileBuilder.append(returnType)
                            .append(" ")
                            .append(executableElement.getSimpleName())
                            .append("(");
                    // 传递参数
                    int varNum = 0;
                    Set<String> stringParameter = new HashSet<>();
                    for (VariableElement parameter : executableElement.getParameters()) {
                        String parameterName = parameter.getSimpleName().toString();
                        String parameterType = parameter.asType().toString();
                        // 拼接
                        if (varNum++ > 0) javaFileBuilder.append(", ");
                        javaFileBuilder.append(parameterType)
                                .append(" ")
                                .append(parameterName);
                        // 字符串需要转换单引号
                        if ("java.lang.String".equals(parameterType)) stringParameter.add(parameterName);
                    }
                    // sql语句
                    javaFileBuilder.append(") {\n\t\tString sql = \"");
                    // 查询内容
                    Select select;
                    Insert insert;
                    Delete delete;
                    Update update;
                    if ((select = executableElement.getAnnotation(Select.class)) != null){
                        String sqlVar = this.varReplace(select.value(), stringParameter);
                        javaFileBuilder.append(sqlVar).append("\";");
                        this.appendSelect(javaFileBuilder, select.value(), returnType);
                    } else if ((insert = executableElement.getAnnotation(Insert.class)) != null) {
                        String sqlVar = this.varReplace(insert.value(), stringParameter);
                        javaFileBuilder.append(sqlVar).append("\";");
                        this.appendInsert(javaFileBuilder);
                    } else if ((delete = executableElement.getAnnotation(Delete.class)) != null) {
                        String sqlVar = this.varReplace(delete.value(), stringParameter);
                        javaFileBuilder.append(sqlVar).append("\";");
                        this.appendUpdate(javaFileBuilder);
                    } else if ((update = executableElement.getAnnotation(Update.class)) != null) {
                        String sqlVar = this.varReplace(update.value(), stringParameter);
                        javaFileBuilder.append(sqlVar).append("\";");
                        this.appendUpdate(javaFileBuilder);
                    }
                }

                javaFileBuilder.append("\n}");

                // 写入
                try {
                    JavaFileObject source = processingEnv.getFiler().createSourceFile(
                            pack + "." + daoName + "Impl");
                    Writer writer = source.openWriter();
                    writer.write(javaFileBuilder.toString());
                    writer.flush();
                    writer.close();
                } catch (IOException ignored) { }
            }
        }
        return true;
    }

    private void appendUpdate(StringBuilder javaFileBuilder){
        javaFileBuilder.append("\n\t\tlong num = 0;");
        javaFileBuilder.append("\n\t\t");
        javaFileBuilder.append("\n\t\tConnection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);");
        javaFileBuilder.append("\n\t\ttry (PreparedStatement preparedStatement =connection.prepareStatement(sql)){");
        javaFileBuilder.append("\n\t\t\tnum = preparedStatement.executeUpdate();");
        javaFileBuilder.append("\n\t\t\treturn num;");
        javaFileBuilder.append("\n\t\t} catch (SQLException e) {");
        javaFileBuilder.append("\n\t\t\te.printStackTrace();");
        javaFileBuilder.append("\n\t\t}");
        javaFileBuilder.append("\n\t\treturn num;\n\t}\n");
    }

    private void appendInsert(StringBuilder javaFileBuilder){
        javaFileBuilder.append("\n\t\tlong num = 0;");
        javaFileBuilder.append("\n\t\t");
        javaFileBuilder.append("\n\t\tConnection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);");
        javaFileBuilder.append("\n\t\ttry (PreparedStatement preparedStatement =connection.prepareStatement(sql)){");
        javaFileBuilder.append("\n\t\t\tnum = preparedStatement.executeUpdate();");
        javaFileBuilder.append("\n\t\t\treturn num;");
        javaFileBuilder.append("\n\t\t} catch (SQLException e) {");
        javaFileBuilder.append("\n\t\t\te.printStackTrace();");
        javaFileBuilder.append("\n\t\t}");
        javaFileBuilder.append("\n\t\treturn num;\n\t}\n");
    }

    private void appendSelect(StringBuilder javaFileBuilder, String sql , String returnType){
        boolean returnArray = returnType.contains("[]");
        // 对象的类型
        String objType = returnArray ? returnType.substring(0, returnType.length() - 2) : returnType;
        // 对象对应的表
        String tableName = TableProcessor.getTableName(objType);
        // 返回整个对象还是部分数据
        boolean returnAll = returnType.contains(".") && !"java.lang.String".equals(objType);
        String objDataType = returnAll ? objType : this.toObjDataType(objType);
        String selectColumn = "";
        Matcher selectMatcher = selectPattern.matcher(sql);
        Matcher tableMatcher = tablePattern.matcher(sql);
        // 获取选择的列
        if (selectMatcher.find()){
            selectColumn = selectMatcher.group().substring(7);
        }
        // 获取表名
        if (!returnAll && tableMatcher.find()) {
            tableName = tableMatcher.group().substring(5).replace("`", "");
        }

        // 新建返回对象列表
        if (returnArray){
            javaFileBuilder.append("\n\t\t").append(returnType).append(" returnValue;");
        } else {
            javaFileBuilder.append("\n\t\t").append(objDataType).append(" returnValue = null;");
        }
        javaFileBuilder.append("\n\t\tList<").append(objDataType).append("> objList = new ArrayList<>();");
        javaFileBuilder.append("\n\t\t");
        javaFileBuilder.append("\n\t\tConnection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);");
        javaFileBuilder.append("\n\t\ttry (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)){");
        if (returnArray){
            javaFileBuilder.append("\n\t\t\twhile (resultSet.next()){");
        }else {
            javaFileBuilder.append("\n\t\t\tif (resultSet.next()){");
        }
        if (returnAll){
            javaFileBuilder.append("\n\t\t\t\t").append(objType).append(" obj = new ").append(objType).append("();");
            for (Map.Entry<String, TableProcessor.ColumnInfo> columnInfoEntry : TableProcessor.getTableColumns(tableName).entrySet()) {
                TableProcessor.ColumnInfo columnInfo = columnInfoEntry.getValue();
                javaFileBuilder.append("\n\t\t\t\t")
                        .append("obj.set").append(toFirstUpCase(columnInfo.varName))
                        .append("(resultSet.get")
                        .append(toSqlDataType(columnInfo.dataType))
                        .append("(\"").append(columnInfo.columnName).append("\"));");
            }
        }else {
            TableProcessor.ColumnInfo columnInfo = TableProcessor.getTableColumns(tableName).get(selectColumn);
            javaFileBuilder.append("\n\t\t\t\t")
                    .append(objType)
                    .append(" obj = resultSet.get")
                    .append(toSqlDataType(columnInfo.dataType))
                    .append("(\"").append(columnInfo.columnName).append("\");");
        }
        javaFileBuilder.append("\n\t\t\t\tobjList.add(obj);");
        javaFileBuilder.append("\n\t\t\t}");
        javaFileBuilder.append("\n\t\t} catch (SQLException e) {");
        javaFileBuilder.append("\n\t\t\te.printStackTrace();");
        javaFileBuilder.append("\n\t\t}");
        javaFileBuilder.append("\n\t\t");

        // 构建返回对象
        if (returnArray){
            if (returnAll) {
                javaFileBuilder.append("\n\t\treturnValue = objList.toArray(new ").append(objType).append("[0]);");
            } else {
                javaFileBuilder.append("\n\t\treturnValue = new ")
                        .append(returnType, 0, returnType.length()-1)
                        .append("objList.size()];\n\t\tint index = 0;\n\t\tfor (Long aValue : objList) returnValue[index++] = aValue;");
            }
        }else {
            javaFileBuilder.append("\n\t\tif (objList.size() != 0) returnValue = objList.get(0);");
        }

        // 返回值
        if (!returnArray && !returnAll) {
            if ("boolean".equals(returnType)){
                javaFileBuilder.append("\n\t\tif (returnValue == null) return false;");
            } else {
                javaFileBuilder.append("\n\t\tif (returnValue == null) return 0;");
            }
        }
        javaFileBuilder .append("\n\t\treturn returnValue;\n\t}\n");
    }

    /**
     * 替换sql语句中的变量
     * @param sql sql语句
     * @param stringParameter 字符串类型变量集
     * @return 带变量的sql语句
     */
    private String varReplace(String sql, Set<String> stringParameter){
        // 替换变量
        Matcher varMatcher = varPattern.matcher(sql);
        StringBuilder stringBuilder = new StringBuilder();
        while (varMatcher.find()){
            String parameter = varMatcher.group();
            String parameterName = parameter.substring(2, parameter.length()-1);
            if (stringParameter.contains(parameterName)){
                parameterName = parameterName + ".replace(\"'\", \"''\")";
            }
            parameterName = "\" + " + parameterName + " + \"";
            varMatcher.appendReplacement(stringBuilder, parameterName);
        }
        varMatcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * test转换为Test
     * @param str 文本
     * @return 首字母大写
     */
    private String toFirstUpCase(String str){
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }

    /**
     * java.lang.String String
     * @param str 文本
     * @return 首字母大写
     */
    private String toSqlDataType(String str){
        if ("java.lang.String".equals(str)) return "String";
        return toFirstUpCase(str);
    }

    /**
     * java.lang.String String
     * @param str 文本
     * @return 首字母大写
     */
    private String toObjDataType(String str){
        if ("int".equals(str)) return "Integer";
        if ("java.lang.String".equals(str)) return "java.lang.String";
        return toFirstUpCase(str);
    }

}
