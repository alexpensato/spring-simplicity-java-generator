package org.pensatocode.simplicity.generator.components;

public interface DatabaseConfig {
    String getPlatformName();
    String getDatabaseName();
    String getPortNumber();
    String getServerName();
    String getUser();
    String getPassword();
    String getSchemaUser();
    String getSchemaPassword();
}
