package org.pensatocode.simplicity.generator.model;

/**
 * This enum provides an easy way to convert Java lang types into Sql Types.
 *
 * A description of these mappings is widely available in the Internet and can also be found here:
 * https://www.cis.upenn.edu/~bcpierce/courses/629/jdkdocs/guide/jdbc/getstart/mapping.doc.html
 */
public enum DefaultSqlSchemaType implements SchemaType {
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

    DefaultSqlSchemaType(String javaType, String defaultSqlType, String resultSetTypeGetter, String resultSetPrefix, String resultSetSuffix) {
        this.javaType = javaType;
        this.defaultSqlType = defaultSqlType;
        this.defaultJavaMapperType = defaultSqlType;
        this.resultSetTypeGetter = resultSetTypeGetter;
        this.resultSetSuffix = resultSetSuffix;
        this.resultSetPrefix = resultSetPrefix;
    }

    DefaultSqlSchemaType(String javaType, String defaultSqlType, String resultSetTypeGetter) {
        this(javaType, defaultSqlType, resultSetTypeGetter, "", "");
    }

    DefaultSqlSchemaType(String javaType, String defaultSqlType) {
        this(javaType, defaultSqlType, "get"+javaType, "", "");
    }

    private final String javaType;
    private final String defaultSqlType;
    private final String defaultJavaMapperType;
    private final String resultSetTypeGetter;
    private final String resultSetPrefix;
    private final String resultSetSuffix;

    @Override
    public String getJavaType() {
        return javaType;
    }

    @Override
    public String getSqlType() {
        return defaultSqlType;
    }

    public String getJavaMapperType() {
        return defaultJavaMapperType;
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
