package org.pensatocode.simplicity.generator.model;

import lombok.Data;

@Data
public class UserDefinedSchemaType implements SchemaType {
    private String javaType;
    private String javaMapperType = "OTHER";
    private String sqlType = "OTHER";
    private String resultSetTypeGetter;
    private String resultSetPrefix = "";
    private String resultSetSuffix = "";

    public UserDefinedSchemaType(String javaType) {
        this.javaType = javaType;
        this.resultSetTypeGetter = "get"+javaType;
    }
}
