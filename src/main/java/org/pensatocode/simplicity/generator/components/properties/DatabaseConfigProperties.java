package org.pensatocode.simplicity.generator.components.properties;

import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.DatabaseConfig;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.services.Platform;
import org.pensatocode.simplicity.generator.util.PlatformUtil;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public enum DatabaseConfigProperties implements DatabaseConfig {

    SINGLETON;

    // Properties file
    private final Properties properties;

    private final Map<String, String> springPropertiesValues = new HashMap<>();
    private final Map<String, String> hikariPropertiesValues = new HashMap<>();

    // Assembled properties
    private String platformName;
    private String databaseName;
    private String portNumber;
    private String serverName;
    private String user;
    private String password;
    private String schemaUser;
    private String schemaPassword;

    // Platform
    private Platform platform;

    // Constructor
    DatabaseConfigProperties() {
        properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("custom-generator.properties");
            if (inputStream == null) {
                getClass().getClassLoader().getResourceAsStream("simplicity-generator.properties");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        try {
            this.simplicityValidation();
        } catch (GeneratorConfigurationException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        this.assembleProperties();
    }

    /*
        Initialization methods
     */

    private void simplicityValidation() throws GeneratorConfigurationException {
        if(StringUtil.isEmpty(getSimplicityDatasourcePlatform())) {
            throw new GeneratorConfigurationException("Datasource Platform not found");
        }
        if(StringUtil.isEmpty(getSimplicityDatasourceDatabaseName())) {
            throw new GeneratorConfigurationException("Datasource DatabaseName not found");
        }
        if(StringUtil.isEmpty(getSimplicityDatasourcePortNumber())) {
            throw new GeneratorConfigurationException("Datasource PortNumber not found");
        }
        if(StringUtil.isEmpty(getSimplicityDatasourceServerName())) {
            throw new GeneratorConfigurationException("Datasource ServerName not found");
        }
        if(StringUtil.isEmpty(getSimplicityDatasourceUser())) {
            throw new GeneratorConfigurationException("Datasource User not found");
        }
        if(StringUtil.isEmpty(getSimplicityDatasourcePassword())) {
            throw new GeneratorConfigurationException("Datasource Password not found");
        }
    }

    private void assembleProperties() {
        platformName = this.getSimplicityDatasourcePlatform();
        databaseName = this.getSimplicityDatasourceDatabaseName();
        portNumber = this.getSimplicityDatasourcePortNumber();
        serverName = this.getSimplicityDatasourceServerName();
        user = this.getSimplicityDatasourceUser();
        password = this.getSimplicityDatasourcePassword();
        schemaUser = this.getSimplicityDatasourceSchemaUser();
        schemaPassword = this.getSimplicityDatasourceSchemaPassword();
        if (StringUtil.isEmpty(schemaUser)) {
            schemaUser = user;
        }
        if(StringUtil.isEmpty(schemaPassword)) {
            schemaPassword = password;
        }
        // create platform and assemble properties values
        platform = PlatformUtil.createPlatformUsing(platformName);
        assembleSpringProperties();
        assembleHikariProperties();
    }

    private void assembleHikariProperties() {
        hikariPropertiesValues.put("dataSourceClassName", platform.getDataSourceClassName());
        hikariPropertiesValues.put("dataSource.user", user);
        hikariPropertiesValues.put("dataSource.password", password);
        hikariPropertiesValues.put("dataSource.databaseName", databaseName);
        hikariPropertiesValues.put("dataSource.portNumber", portNumber);
        hikariPropertiesValues.put("dataSource.serverName", serverName);
    }

    private void assembleSpringProperties() {
        springPropertiesValues.put("spring.datasource.platform", platformName);
        springPropertiesValues.put("spring.datasource.driver-class-name", platform.getDriverClassName());
        String jdbcUrl = platform.getJdbcUrlPreffix() + serverName + ":" + portNumber + "/" + databaseName;
        springPropertiesValues.put("spring.datasource.url", jdbcUrl);
        springPropertiesValues.put("spring.datasource.schema-username", schemaUser);
        springPropertiesValues.put("spring.datasource.schema-password", schemaPassword);
    }

    /*
        Assembled methods
     */

    @Override
    public String getPlatformName() {
        return platformName;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String getPortNumber() {
        return portNumber;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getSchemaUser() {
        return schemaUser;
    }

    @Override
    public String getSchemaPassword() {
        return schemaPassword;
    }

    @Override
    public Map<String, String> getSpringPropertiesValues() {
        return springPropertiesValues;
    }

    @Override
    public Map<String, String> getHikariPropertiesValues() {
        return hikariPropertiesValues;
    }

    @Override
    public Platform getPlatform() {
        return platform;
    }

    /*
        Properties direct methods
     */

    private String getSimplicityDatasourcePlatform() {
        return properties.getProperty("simplicity.datasource.platform");
    }

    private String getSimplicityDatasourceDatabaseName() {
        return properties.getProperty("simplicity.datasource.databaseName");
    }

    private String getSimplicityDatasourcePortNumber() {
        return properties.getProperty("simplicity.datasource.portNumber");
    }

    private String getSimplicityDatasourceServerName() {
        return properties.getProperty("simplicity.datasource.serverName");
    }

    private String getSimplicityDatasourceUser() {
        return properties.getProperty("simplicity.datasource.user");
    }

    private String getSimplicityDatasourcePassword() {
        return properties.getProperty("simplicity.datasource.password");
    }

    private String getSimplicityDatasourceSchemaUser() {
        return properties.getProperty("simplicity.datasource.schemaUser");
    }

    private String getSimplicityDatasourceSchemaPassword() {
        return properties.getProperty("simplicity.datasource.schemaPassword");
    }

}
