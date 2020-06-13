package org.pensatocode.simplicity.generator.model;

import lombok.Data;

@Data
public class PlatformSchemaType implements SchemaType {
    private String javaType;
    private String javaMapperType;
    private String sqlType;
    private String resultSetTypeGetter;
    private String resultSetPrefix;
    private String resultSetSuffix;

    public PlatformSchemaType(SchemaType defaultSchemaType, String platformSqlType) {
        this.javaType = defaultSchemaType.getJavaType();
        this.javaMapperType = defaultSchemaType.getJavaMapperType();
        if (platformSqlType != null) {
            this.sqlType = platformSqlType;
        } else {
            this.sqlType = defaultSchemaType.getSqlType();
        }
        this.resultSetTypeGetter = defaultSchemaType.getResultSetTypeGetter();
        this.resultSetPrefix = defaultSchemaType.getResultSetPrefix();
        this.resultSetSuffix = defaultSchemaType.getResultSetSuffix();
    }
}
