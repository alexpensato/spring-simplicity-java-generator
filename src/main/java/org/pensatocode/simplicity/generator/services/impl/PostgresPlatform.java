package org.pensatocode.simplicity.generator.services.impl;

import com.github.javaparser.ast.type.Type;
import lombok.NonNull;
import org.pensatocode.simplicity.generator.model.DefaultSqlSchemaType;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.model.PlatformSchemaType;
import org.pensatocode.simplicity.generator.model.SchemaType;
import org.pensatocode.simplicity.generator.services.Platform;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class PostgresPlatform implements Platform {

    public static class InnerMap
    {
        private static final Map<String, String> PLATFORM_SQL_TYPES = new HashMap<>();

        private static final Map<String, SchemaType> INSTANCE = new HashMap<>();

        static {
            PLATFORM_SQL_TYPES.put("Double","DOUBLE PRECISION");

            for(SchemaType defaultSchemaType: DefaultSqlSchemaType.values()) {
                SchemaType platformSchemaType = new PlatformSchemaType(
                        defaultSchemaType, PLATFORM_SQL_TYPES.get(defaultSchemaType.getJavaType()));
                INSTANCE.put(defaultSchemaType.getJavaType(), platformSchemaType);
            }
        }
    }

    @Override
    public SchemaType getSchemaType(@NonNull Type type) {
        return InnerMap.INSTANCE.get(StringUtil.capitalize(type.asString()));
    }

    @Override
    public boolean isSequenceNecessary() {
        return true;
    }

    @Override
    public boolean areGrantsNecessary() {
        return true;
    }

    @Override
    public void appendPrimaryKey(String tableName, MapperVariable id, StringBuilder sb) {
        sb.append("  ")
                .append(id.getSchemaName())
                .append(" ")
                .append(id.getType().getSqlType().toLowerCase())
                .append(" DEFAULT nextval('")
                .append(tableName)
                .append("_id_seq') PRIMARY KEY,\n");
    }
}
