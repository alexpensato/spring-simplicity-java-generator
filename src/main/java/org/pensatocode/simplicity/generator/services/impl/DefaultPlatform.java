package org.pensatocode.simplicity.generator.services.impl;

import com.github.javaparser.ast.type.Type;
import lombok.NonNull;
import org.pensatocode.simplicity.generator.model.DefaultSqlSchemaType;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.model.SchemaType;
import org.pensatocode.simplicity.generator.services.Platform;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class DefaultPlatform implements Platform {

    public static class InnerMap
    {
        private static final Map<String, SchemaType> INSTANCE = new HashMap<>();

        static {
            for(SchemaType defaultSchemaType: DefaultSqlSchemaType.values()) {
                INSTANCE.put(defaultSchemaType.getJavaType(), defaultSchemaType);
            }
        }
    }

    @Override
    public SchemaType getSchemaType(@NonNull Type type) {
        return InnerMap.INSTANCE.get(StringUtil.capitalize(type.asString()));
    }

    @Override
    public boolean isSequenceNecessary() {
        return false;
    }

    @Override
    public boolean areGrantsNecessary() {
        return false;
    }

    @Override
    public void appendPrimaryKey(String tableName, MapperVariable id, StringBuilder sb) {
        sb.append("  ")
                .append(id.getSchemaName())
                .append(" ")
                .append(id.getType().getSqlType().toLowerCase())
                .append(" AUTO_INCREMENT NOT NULL PRIMARY KEY,\n");
    }
}
