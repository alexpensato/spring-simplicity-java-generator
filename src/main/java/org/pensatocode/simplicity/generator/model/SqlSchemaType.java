package org.pensatocode.simplicity.generator.model;

import com.github.javaparser.ast.type.Type;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum provides an easy way to convert Java lang types into Sql Types.
 *
 * A description of these mappings is widely available in the Internet and can also be found here:
 * https://www.cis.upenn.edu/~bcpierce/courses/629/jdkdocs/guide/jdbc/getstart/mapping.doc.html
 */
public enum SqlSchemaType implements SchemaType {
    BOOLEAN("Boolean","BOOLEAN"),
    BYTE("Byte","TINYINT"),
    SHORT("Short","SMALLINT"),
    INT("Int","INTEGER"),
    INTEGER("Integer","INTEGER", "getInt"),
    LONG("Long","BIGINT"),
    FLOAT("Float","REAL"),
    DOUBLE("Double","DOUBLE"),
    BIG_DECIMAL("BigDecimal","DECIMAL"),
    CHAR("Char","CHAR","getString","convertToChar(",")"),
    CHARACTER("Character","CHAR","getString","convertToChar(",")"),
    STRING("String","VARCHAR"),
    BYTE_ARRAY("Byte[]","VARBINARY", "getBytes"),
    DATE("Date","DATE","getTimestamp","convertToDate(",")"),
    LOCAL_DATE("LocalDate","DATE","getDate","convertToLocalDate(",")"),
    LOCAL_TIME("LocalTime","TIME","getTime","convertToLocalTime(",")"),
    LOCAL_DATE_TIME("LocalDateTime","TIMESTAMP","getTimestamp","convertToLocalDateTime(",")"),
    OBJECT("Object","OTHER")
    ;

    SqlSchemaType(String javaType, String sqlType, String resultSetTypeGetter, String resultSetPrefix, String resultSetSuffix) {
        this.javaType = javaType;
        this.sqlType = sqlType;
        this.resultSetTypeGetter = resultSetTypeGetter;
        this.resultSetSuffix = resultSetSuffix;
        this.resultSetPrefix = resultSetPrefix;
    }

    SqlSchemaType(String javaType, String sqlType, String resultSetTypeGetter) {
        this(javaType, sqlType, resultSetTypeGetter, "", "");
    }

    SqlSchemaType(String javaType, String sqlType) {
        this(javaType, sqlType, "get"+javaType, "", "");
    }

    private final String javaType;
    private final String sqlType;
    private final String resultSetTypeGetter;
    private final String resultSetPrefix;
    private final String resultSetSuffix;

    private static Map<String, SqlSchemaType> map = new HashMap<>();

    public static SqlSchemaType convertFromJavaParserType(Type type) {
        if (map.isEmpty()) {
            initialiseMap();
        }
        return map.get(StringUtil.capitalize(type.asString()));
    }

    private static void initialiseMap() {
        synchronized (SqlSchemaType.class) {
            if (map.isEmpty()) {
                for(SqlSchemaType sqlSchemaType : SqlSchemaType.values()) {
                    map.put(sqlSchemaType.javaType, sqlSchemaType);
                }
            }
        }
    }

    @Override
    public String getJavaType() {
        return javaType;
    }

    @Override
    public String getSqlType() {
        return sqlType;
    }

    @Override
    public String getResultSetTypeGetter() {
        return resultSetTypeGetter;
    }

    @Override
    public String getResultSetPrefix() {
        return resultSetPrefix;
    }

    @Override
    public String getResultSetSuffix() {
        return resultSetSuffix;
    }
}
