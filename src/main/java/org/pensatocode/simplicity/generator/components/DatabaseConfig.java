package org.pensatocode.simplicity.generator.components;

public interface DatabaseConfig {
    String getPlatform();
    String getDatabaseName();
    String getPortNumber();
    String getServerName();
    String getUser();
    String getPassword();
}
