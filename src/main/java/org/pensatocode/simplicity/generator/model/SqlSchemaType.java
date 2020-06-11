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
    DOUBLE("Double","DOUBLE PRECISION", true, "DOUBLE"),
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

    public static class InnerMap
    {
        private static final Map<String, SqlSchemaType> INSTANCE = new HashMap<>();
    }

    SqlSchemaType(String javaType, String sqlType, String resultSetTypeGetter, String resultSetPrefix,
                  String resultSetSuffix, boolean modifiedMapperType, String alternativeMapperType) {
        this.javaType = javaType;
        this.sqlType = sqlType;
        this.resultSetTypeGetter = resultSetTypeGetter;
        this.resultSetSuffix = resultSetSuffix;
        this.resultSetPrefix = resultSetPrefix;
        this.modifiedMapperType = modifiedMapperType;
        if (this.modifiedMapperType) {
            this.mapperType = alternativeMapperType;
        } else {
            this.mapperType = sqlType;
        }
        InnerMap.INSTANCE.put(javaType, this);
    }

    SqlSchemaType(String javaType, String sqlType, String resultSetTypeGetter, String resultSetPrefix, String resultSetSuffix) {
        this(javaType, sqlType, resultSetTypeGetter, resultSetPrefix, resultSetSuffix, false, null);
    }

    SqlSchemaType(String javaType, String sqlType, boolean modifiedMapperType, String alternativeMapperType) {
        this(javaType, sqlType, "get"+javaType, "", "", modifiedMapperType, alternativeMapperType);
    }

    SqlSchemaType(String javaType, String sqlType, String resultSetTypeGetter) {
        this(javaType, sqlType, resultSetTypeGetter, "", "", false, null);
    }

    SqlSchemaType(String javaType, String sqlType) {
        this(javaType, sqlType, "get"+javaType, "", "", false, null);
    }

    private final String javaType;
    private final String sqlType;
    private final boolean modifiedMapperType;
    private final String mapperType;
    private final String resultSetTypeGetter;
    private final String resultSetPrefix;
    private final String resultSetSuffix;

    public static SqlSchemaType convertFromJavaParserType(Type type) {
        return InnerMap.INSTANCE.get(StringUtil.capitalize(type.asString()));
    }

    @Override
    public String getJavaType() {
        return javaType;
    }

    @Override
    public String getSqlType() {
        return sqlType;
    }

    public boolean isModifiedMapperType() {
        return modifiedMapperType;
    }

    public String getMapperType() {
        return mapperType;
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
