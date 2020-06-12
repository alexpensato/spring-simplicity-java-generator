package org.pensatocode.simplicity.generator.components.properties;

import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.Config;
import org.pensatocode.simplicity.generator.components.Paths;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public class ConfigProperties implements Config {

    private static volatile ConfigProperties instance;

    public static ConfigProperties get() {
        if (instance == null) {
            synchronized(ConfigProperties.class) {
                if (instance == null) {
                    instance = new ConfigProperties(PathProperties.SINGLETON);
                }
            }
        }
        return instance;
    }

    // Properties file
    private final Properties properties;

    // Assembled properties
    private String databaseUsername = "postgres";
    private String apiContext = "api";
    private final Set<String> regenerateControllers = new HashSet<>();
    private final Set<String> regenerateRepositories = new HashSet<>();
    private final Set<String> regenerateMappers = new HashSet<>();

    // Constructor
    ConfigProperties(Paths paths) {
        String resourcePath = paths.getResourcesPath();
        properties = new Properties();
        try {
            // Properties file in the target project
            String configPath = resourcePath + "simplicity.properties";
            if (log.isDebugEnabled()) {
                log.info(String.format("Config path is %s", configPath));
            }
            properties.load(new FileInputStream(configPath));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        this.assembleConfigs();
    }

    /*
        Initialization methods
     */

    private void assembleConfigs() {
        if(! StringUtil.isEmpty(getSimplicityConfigDatabaseUsername())) {
            databaseUsername = getSimplicityConfigDatabaseUsername();
        }
        if(! StringUtil.isEmpty(getSimplicityConfigApiContext())) {
            apiContext = getSimplicityConfigApiContext();
        }
        if(! StringUtil.isEmpty(getSimplicityConfigRegenerateControllers())) {
            addAll(getSimplicityConfigRegenerateControllers(), regenerateControllers);
        }
        if(! StringUtil.isEmpty(getSimplicityConfigRegenerateMappers())) {
            addAll(getSimplicityConfigRegenerateMappers(), regenerateMappers);
        }
        if(! StringUtil.isEmpty(getSimplicityConfigRegenerateRepositories())) {
            addAll(getSimplicityConfigRegenerateRepositories(), regenerateRepositories);
        }
    }

    /*
        Assembled config methods
     */

    @Override
    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getApiContext() {
        return apiContext;
    }

    @Override
    public Set<String> getRegenerateControllers() {
        return regenerateControllers;
    }

    @Override
    public Set<String> getRegenerateRepositories() {
        return regenerateRepositories;
    }

    @Override
    public Set<String> getRegenerateMappers() {
        return regenerateMappers;
    }

    /*
        Collections
     */

    private void addAll(String strConfig, Set<String> set) {
        String[] configs = strConfig.split(",");
        set.addAll(Arrays.asList(configs));
    }

    /*
        Properties direct methods
     */

    private String getSimplicityConfigDatabaseUsername() {
        return properties.getProperty("simplicity.config.database.username");
    }

    private String getSimplicityConfigApiContext() {
        return properties.getProperty("simplicity.config.api.context");
    }

    private String getSimplicityConfigRegenerateMappers() {
        return properties.getProperty("simplicity.config.regenerate.mappers");
    }

    private String getSimplicityConfigRegenerateControllers() {
        return properties.getProperty("simplicity.config.regenerate.controllers");
    }

    private String getSimplicityConfigRegenerateRepositories() {
        return properties.getProperty("simplicity.config.regenerate.repositories");
    }



}
