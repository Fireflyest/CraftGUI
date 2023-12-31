package org.fireflyest.craftdatabase.annotation;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.fireflyest.craftdatabase.builder.SQLCreateTable;

/**
 * 数据持久层注解处理器
 * @author Fireflyest
 * @since 2022/8/17
 */
// @SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("org.fireflyest.craftdatabase.annotation.Dao")
public class DaoProcessor extends AbstractProcessor {

    private static final Pattern varPattern = Pattern.compile("\\$\\{([^{]*)}");
    private static final Pattern selectPattern = Pattern.compile("SELECT ([^ ]*)");
    private static final Pattern tablePattern = Pattern.compile("FROM ([^ ]*)");

    private static final String STRING = "java.lang.String";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Processing database dao...");

        // 当前处理器支持的所有注解种类
        for (TypeElement typeElement : annotations) {
            // 获得被该注解声明的元素
            for (Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
                this.processElement(element);
            }
        }
        return true;
    }

    private void processElement(Element element) {
        Dao dao = element.getAnnotation(Dao.class);

        TypeElement interfaceElement = ((TypeElement) element);
        String className = interfaceElement.getQualifiedName().toString();
        String daoName = interfaceElement.getSimpleName().toString();
        String pack = className.substring(0, className.lastIndexOf("."));

        // 头部
        StringBuilder javaFileBuilder = new StringBuilder()
                .append("package ").append(pack).append(";") // 包
                .append("\n\nimport java.sql.*;\nimport java.util.*;") // 引用
                .append("\n\npublic class ").append(daoName).append("Impl implements ").append(daoName)
                .append(" {")
                .append("\n\n    private final String url;"); // 全局变量

        this.appendCreateTable(javaFileBuilder, dao.value()); // 建表指令

        // 构造函数
        javaFileBuilder.append("\n\n    /**")
            .append("\n     * 自动生成的数据访问层")
            .append("\n     * @param url 链接")
            .append("\n     */")
            .append("\n    public ").append(daoName).append("Impl(String url) {")
            .append("\n        this.url = url;\n    }\n");

        // 遍历所有方法
        for (Element enclosedElement : interfaceElement.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.METHOD) continue;
            this.appendField(javaFileBuilder, enclosedElement);
        }

        javaFileBuilder.append("\n}");

        // 写入
        this.writeSource(javaFileBuilder, pack, daoName);
    }

    /**
     * 拼接方法
     * @param javaFileBuilder builder
     * @param enclosedElement 元素
     */
    private void appendField(StringBuilder javaFileBuilder, Element enclosedElement) {
        javaFileBuilder.append("\n    @Override\n    public ");
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
            if (STRING.equals(parameterType)) stringParameter.add(parameterName);
        }
        // sql语句
        javaFileBuilder.append(") {\n        String sql = \"");
        // 查询内容
        Select select;
        Insert insert;
        Delete delete;
        Update update;
        if ((select = executableElement.getAnnotation(Select.class)) != null) {
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

    /**
     * 生成文件
     * @param javaFileBuilder builder
     * @param pack 包
     * @param daoName 名称
     */
    private void writeSource(StringBuilder javaFileBuilder, String pack, String daoName) {
        JavaFileObject source = null;
        try {
            source = processingEnv.getFiler().createSourceFile(pack + "." + daoName + "Impl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (source == null) {
            return;
        }
        try (Writer writer = source.openWriter()) {
            writer.write(javaFileBuilder.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拼接建表指令和获取方法
     * @param javaFileBuilder builder
     * @param obj 表名
     */
    private void appendCreateTable(StringBuilder javaFileBuilder, String obj) {
        // 建表
        javaFileBuilder.append("\n\n    private static final String createTable = \"");
        if (!"".equals(obj)) {
            String tableClassName = obj;
            String tableName = TableProcessor.getTableName(tableClassName);
            SQLCreateTable createTableBuilder = new SQLCreateTable(tableName);
            for (TableProcessor.ColumnInfo value : TableProcessor.getTableColumns(tableName).values()) {
                if (value.id) {
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
        javaFileBuilder.append("\n\n    public java.lang.String getCreateTableSQL(){ return createTable; }");
    }


    /**
     * 更新
     * @param javaFileBuilder builder
     */
    private void appendUpdate(StringBuilder javaFileBuilder){
        javaFileBuilder.append("\n        long num = 0;");
        javaFileBuilder.append("\n        Connection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);");
        javaFileBuilder.append("\n        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {");
        javaFileBuilder.append("\n            num = preparedStatement.executeUpdate();");
        javaFileBuilder.append("\n            return num;");
        javaFileBuilder.append("\n        } catch (SQLException e) {");
        javaFileBuilder.append("\n            e.printStackTrace();");
        javaFileBuilder.append("\n        }");
        javaFileBuilder.append("\n        return num;\n    }\n");
    }

    /**
     * 插入
     * @param javaFileBuilder builder
     */
    private void appendInsert(StringBuilder javaFileBuilder){
        javaFileBuilder.append("\n        long insertId = 0;");
        javaFileBuilder.append("\n        Connection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);");
        javaFileBuilder.append("\n        ResultSet resultSet = null;");
        javaFileBuilder.append("\n        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){");
        javaFileBuilder.append("\n            preparedStatement.executeUpdate();");
        javaFileBuilder.append("\n            resultSet = preparedStatement.getGeneratedKeys();");
        javaFileBuilder.append("\n            if (resultSet.next()) {");
        javaFileBuilder.append("\n                insertId = resultSet.getInt(1);");
        javaFileBuilder.append("\n            }");
        javaFileBuilder.append("\n            return insertId;");
        javaFileBuilder.append("\n        } catch (SQLException e) {");
        javaFileBuilder.append("\n            e.printStackTrace();");
        javaFileBuilder.append("\n        } finally {");
        javaFileBuilder.append("\n            if (resultSet != null) {");
        javaFileBuilder.append("\n                try {");
        javaFileBuilder.append("\n                    resultSet.close();");
        javaFileBuilder.append("\n                } catch (SQLException e) {");
        javaFileBuilder.append("\n                  e.printStackTrace();");
        javaFileBuilder.append("\n                }");
        javaFileBuilder.append("\n            }");
        javaFileBuilder.append("\n        }");
        javaFileBuilder.append("\n        return insertId;\n    }\n");
    }

    /**
     * 查询
     * @param javaFileBuilder builder
     * @param sql 语句
     * @param returnType 返回类型
     */
    private void appendSelect(StringBuilder javaFileBuilder, String sql, String returnType) {
        boolean returnArray = returnType.contains("[]");
        // 对象的类型
        String objType = returnArray ? returnType.substring(0, returnType.length() - 2) : returnType;
        // 对象对应的表
        String tableName = TableProcessor.getTableName(objType);
        // 返回整个对象还是部分数据
        boolean returnAll = returnType.contains(".") && !STRING.equals(objType);
        String objDataType = returnAll ? objType : this.toObjDataType(objType);
        String selectColumn = "";
        Matcher selectMatcher = selectPattern.matcher(sql);
        Matcher tableMatcher = tablePattern.matcher(sql);
        // 获取选择的列
        if (selectMatcher.find()) {
            selectColumn = selectMatcher.group().substring(7);
        }
        // 获取表名
        if (!returnAll && tableMatcher.find()) {
            tableName = tableMatcher.group().substring(5).replace("`", "");
        }

        // 新建返回对象列表
        if (returnArray) {
            javaFileBuilder.append("\n        ").append(returnType).append(" returnValue;");
        } else {
            javaFileBuilder.append("\n        ").append(objDataType).append(" returnValue = null;");
        }
        javaFileBuilder.append("\n        List<").append(objDataType).append("> objList = new ArrayList<>();");
        javaFileBuilder.append("\n        ");
        javaFileBuilder.append("\n        Connection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);");
        javaFileBuilder.append("\n        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {");
        if (returnArray){
            javaFileBuilder.append("\n            while (resultSet.next()) {");
        } else {
            javaFileBuilder.append("\n            if (resultSet.next()) {");
        }
        if (returnAll) {
            javaFileBuilder.append("\n                ").append(objType).append(" obj = new ").append(objType).append("();");
            for (Map.Entry<String, TableProcessor.ColumnInfo> columnInfoEntry : TableProcessor.getTableColumns(tableName).entrySet()) {
                TableProcessor.ColumnInfo columnInfo = columnInfoEntry.getValue();
                javaFileBuilder.append("\n                ")
                        .append("obj.set").append(toFirstUpCase(columnInfo.varName))
                        .append("(resultSet.get")
                        .append(this.toSqlDataType(columnInfo.dataType))
                        .append("(\"").append(columnInfo.columnName).append("\"));");
            }
        } else {
            TableProcessor.ColumnInfo columnInfo = TableProcessor.getTableColumns(tableName).get(selectColumn.replace("`", ""));
            if (columnInfo != null) {
                javaFileBuilder.append("\n                ")
                    .append(objType)
                    .append(" obj = resultSet.get")
                    .append(this.toSqlDataType(columnInfo.dataType))
                    .append("(\"")
                    .append(columnInfo.columnName)
                    .append("\");");
            }
        }
        javaFileBuilder.append("\n                objList.add(obj);");
        javaFileBuilder.append("\n            }");
        javaFileBuilder.append("\n        } catch (SQLException e) {");
        javaFileBuilder.append("\n            e.printStackTrace();");
        javaFileBuilder.append("\n        }");
        javaFileBuilder.append("\n        ");

        // 构建返回对象
        if (returnArray) {
            if (returnAll) {
                javaFileBuilder.append("\n        returnValue = objList.toArray(new ").append(objType).append("[0]);");
            } else {
                javaFileBuilder.append("\n        returnValue = new ")
                        .append(returnType, 0, returnType.length() - 1)
                        .append("objList.size()];\n        int index = 0;\n        for (").append(objDataType).append(" aValue : objList) returnValue[index++] = aValue;");
            }
        } else {
            javaFileBuilder.append("\n        if (objList.size() != 0) {");
            javaFileBuilder.append("\n            returnValue = objList.get(0);");
            javaFileBuilder.append("\n        }");
        }

        // 返回值
        if (!returnArray && !returnAll) {
            if ("boolean".equals(returnType)) {
                javaFileBuilder.append("\n        if (returnValue == null) return false;");
            } else if (STRING.equals(returnType)) {
                javaFileBuilder.append("\n        if (returnValue == null) return null;");
            } else {
                javaFileBuilder.append("\n        if (returnValue == null) return 0;");
            }
        }
        javaFileBuilder.append("\n        return returnValue;\n    }\n");
    }

    /**
     * 替换sql语句中的变量
     * @param sql sql语句
     * @param stringParameter 字符串类型变量集
     * @return 带变量的sql语句
     */
    private String varReplace(String sql, Set<String> stringParameter) {
        // 替换变量
        Matcher varMatcher = varPattern.matcher(sql);
        StringBuilder stringBuilder = new StringBuilder();
        while (varMatcher.find()) {
            String parameter = varMatcher.group();
            String parameterName = parameter.substring(2, parameter.length() - 1);
            if (stringParameter.contains(parameterName)) {
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
    private String toFirstUpCase(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * java.lang.String String
     * @param str 文本
     * @return 首字母大写
     */
    private String toSqlDataType(String str) {
        if (str == null) {
            return "";
        }
        if (STRING.equals(str)) return "String";
        return toFirstUpCase(str);
    }

    /**
     * java.lang.String String
     * @param str 文本
     * @return 首字母大写
     */
    private String toObjDataType(String str) {
        if (str == null) {
            return "";
        }
        if ("int".equals(str)) return "Integer";
        if (STRING.equals(str)) return STRING;
        return toFirstUpCase(str);
    }

}
