package org.pensatocode.simplicity.generator.components;

import org.pensatocode.simplicity.generator.services.Platform;

import java.util.Map;

public interface DatabaseConfig {
    String getPlatformName();
    String getDatabaseName();
    String getPortNumber();
    String getServerName();
    String getUser();
    String getPassword();
    String getSchemaUser();
    String getSchemaPassword();
    Map<String, String> getSpringPropertiesValues();
    Map<String, String> getHikariPropertiesValues();
    Platform getPlatform();
}
