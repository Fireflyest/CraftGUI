package org.fireflyest.craftdatabase.annotation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

import java.util.Collections;
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
        return tableInfoMap.getOrDefault(tableName, Collections.emptyMap());
    }

    /**
     * 通过类名获取表名
     * @param objType 对象类全名
     * @return 表名
     */
    public static String getTableName(String objType){
        return tableNameMap.getOrDefault(objType, objType.substring(objType.lastIndexOf(".") + 1));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Processing database table...");

        // 当前处理器支持的所有注解种类
        for (TypeElement typeElement : annotations) {
            // 获得被该注解声明的元素
            for (Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
                this.processElement(element);
            }
        }
        return true;
    }

    /**
     * 处理类元素
     * @param element 类元素
     */
    private void processElement(Element element) {
        TypeElement classElement = ((TypeElement) element);

        // 对象的基本信息创建表
        Table table = classElement.getAnnotation(Table.class);
        String className = classElement.getQualifiedName().toString();
        String tableName = "".equals(table.value()) ? classElement.getSimpleName().toString() : table.value();
        Map<String, ColumnInfo> columnInfoMap = new HashMap<>();

        // 遍历成员变量
        for (Element enclosedElement : classElement.getEnclosedElements()) {
            // 是否需要跳过
            if (enclosedElement.getKind() != ElementKind.FIELD ||  enclosedElement.getAnnotation(Skip.class) != null) {
                continue;
            }
            // 获取注解信息
            ColumnInfo columnInfo = this.processEnclosedElement(enclosedElement);
            columnInfoMap.put(columnInfo.columnName, columnInfo);
        }

        tableInfoMap.put(tableName, columnInfoMap);
        tableNameMap.put(className, tableName);
    }

    /**
     * 处理变量元素
     * @param enclosedElement 成员变量
     * @return 信息
     */
    private ColumnInfo processEnclosedElement(Element enclosedElement) {
        

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
        return columnInfo;
    }

    public static class ColumnInfo {

        /**
         * 建表信息
         * @param varName 成员变量名称
         * @param columnName 表名
         * @param dataType 愿数据类型
         */
        public ColumnInfo(String varName, String columnName, String dataType) {
            this.varName = varName;
            this.columnName = columnName;
            this.dataType = dataType;
        }

        public final String varName;
        public final String columnName;
        public final String dataType;
        public String columnDataType;  // 数据库数据类型
        public boolean id;  // 是否自增主键
        public boolean primary;  // 是否主键
        public String defaultValue;  // 默认值
        public boolean noNull;  // 是否非空

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


