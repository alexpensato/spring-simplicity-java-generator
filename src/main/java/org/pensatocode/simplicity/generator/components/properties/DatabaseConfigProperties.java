package org.pensatocode.simplicity.generator.components.properties;

import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.DatabaseConfig;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public enum DatabaseConfigProperties implements DatabaseConfig {

    SINGLETON;

    // Properties file
    private final Properties properties;

    // Assembled properties
    private String platform;
    private String databaseName;
    private String portNumber;
    private String serverName;
    private String user;
    private String password;
    private String schemaUser;
    private String schemaPassword;

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
        this.assemblePackages();
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

    private void assemblePackages() {
        platform = this.getSimplicityDatasourcePlatform();
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
    }

    /*
        Assembled methods
     */

    @Override
    public String getPlatformName() {
        return platform;
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
