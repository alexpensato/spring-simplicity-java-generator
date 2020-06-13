package org.pensatocode.simplicity.generator.model;

import java.util.HashMap;
import java.util.Map;

public final class DefaultSqlSampleData {

    private static final Map<String, String> INSTANCE;

    static {
        INSTANCE = new HashMap<>();
        INSTANCE.put("Boolean", "true");
        INSTANCE.put("Byte", "0");
        INSTANCE.put("Short", "1");
        INSTANCE.put("Int", "1");
        INSTANCE.put("Integer", "1");
        INSTANCE.put("Long", "1");
        INSTANCE.put("Float", "1.0");
        INSTANCE.put("Double", "1.0");
        INSTANCE.put("BigDecimal", "1.0");
        INSTANCE.put("Char", "'-'");
        INSTANCE.put("Character", "'-'");
        INSTANCE.put("String", "'abc'");
        INSTANCE.put("Byte[]", "'abc'::bytea");
        INSTANCE.put("Date", "'2020-01-31'");
        INSTANCE.put("LocalDate", "'2020-01-31'");
        INSTANCE.put("LocalTime", "'01:30'");
        INSTANCE.put("LocalDateTime", "'2020-01-01 00:00:01' UTC");
        INSTANCE.put("Object", "OTHER");
    }

    DefaultSqlSampleData(String javaType, String sampleData) {
        INSTANCE.put(javaType, sampleData);
    }

    public static String getSampleData(String typeName) {
        return INSTANCE.get(typeName);
    }
}
