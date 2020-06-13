package org.pensatocode.simplicity.generator.model;

public interface SchemaType {
    String getJavaType();
    String getJavaMapperType();
    String getSqlType();
    String getResultSetTypeGetter();
    String getResultSetPrefix();
    String getResultSetSuffix();
}
