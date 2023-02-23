package org.fireflyest.craftdatabase.annotation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 表注解处理器
 * @author Fireflyest
 * @since 2022/8/16
 */
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("org.fireflyest.craftdatabase.annotation.Table")
public class TableProcessor extends AbstractProcessor {

    private static final Map<String, Map<String, ColumnInfo>> tableInfoMap = new HashMap<>();
    private static final Map<String, String> tableNameMap = new HashMap<>();

    /**
     * 通过表名获取列信息
     * @param tableName 表名
     * @return 列信息
     */
    public static Map<String, ColumnInfo> getTableColumns(String tableName){
        return tableInfoMap.get(tableName);
    }

    /**
     * 通过类名获取表名
     * @param objType 对象类全名
     * @return 表名
     */
    public static String getTableName(String objType){
        return tableNameMap.get(objType);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Processing database table...");

        // 当前处理器支持的所有注解种类
        for (TypeElement typeElement : annotations) {
            // 获得被该注解声明的元素
            for (Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {

                TypeElement classElement = ((TypeElement) element);

                Table table = classElement.getAnnotation(Table.class);
                String className = classElement.getQualifiedName().toString();
                String tableName = "".equals(table.value()) ? classElement.getSimpleName().toString() : table.value();
                tableNameMap.put(className, tableName);
                tableInfoMap.put(tableName, new HashMap<>());

                // 遍历成员变量
                for (Element enclosedElement : classElement.getEnclosedElements()) {
                    if (enclosedElement.getKind() != ElementKind.FIELD) continue;
                    if (enclosedElement.getAnnotation(Skip.class) != null) continue;

                    VariableElement variableElement = ((VariableElement) enclosedElement);
                    // 获取列信息
                    Column column = enclosedElement.getAnnotation(Column.class);
                    String varName = enclosedElement.getSimpleName().toString();
                    String columnName = varName;
                    String dataType = variableElement.asType().toString();
                    if (column != null && !"".equals(column.name())) columnName = column.name();

                    // 记录列信息
                    ColumnInfo columnInfo = new ColumnInfo(varName, columnName, dataType);

                    // 加入各成员变量
                    ID id = enclosedElement.getAnnotation(ID.class);
                    Primary primary = enclosedElement.getAnnotation(Primary.class);
                    if (id != null){
                        columnInfo.id = true;
                        columnInfo.primary = true;
                    } else if (primary != null) {
                        columnInfo.primary = true;
                    } else if (column != null){
                        columnInfo.noNull = column.noNull();
                        columnInfo.defaultValue = column.defaultValue();
                        // 特殊的类型，比如TEXT
                        if (!"".equals(column.dataType())) {
                            columnInfo.columnDataType = column.dataType();
                        }
                    }

                    tableInfoMap.get(tableName).put(columnName, columnInfo);
                }
            }
        }
        return true;
    }

    public static class ColumnInfo {

        public ColumnInfo(String varName, String columnName, String dataType) {
            this.varName = varName;
            this.columnName = columnName;
            this.dataType = dataType;
        }

        public String varName;
        public String columnName;
        public String dataType;
        public String columnDataType;
        public boolean id;
        public boolean primary;
        public String defaultValue;
        public boolean noNull;

        @Override
        public String toString() {
            return "ColumnInfo{" +
                    "varName='" + varName + '\'' +
                    ", columnName='" + columnName + '\'' +
                    ", dataType='" + dataType + '\'' +
                    ", columnDataType='" + columnDataType + '\'' +
                    ", id=" + id +
                    ", primary=" + primary +
                    ", defaultValue='" + defaultValue + '\'' +
                    ", noNull=" + noNull +
                    '}';
        }
    }

}


