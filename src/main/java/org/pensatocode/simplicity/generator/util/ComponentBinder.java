package org.pensatocode.simplicity.generator.util;

import org.pensatocode.simplicity.generator.components.*;
import org.pensatocode.simplicity.generator.components.properties.*;

import java.util.HashMap;
import java.util.Map;

public class ComponentBinder {

    private static class ComponentHolder {
        private static final Map<String, Object> instance = new HashMap<>();
    }

    private enum ComponentName {
        CONFIG(null), // Lazy loaded due to external properties file
        DATABASE_CONFIG(DatabaseConfigProperties.SINGLETON),
        PACKAGES(PackageProperties.SINGLETON),
        PATHS(PathProperties.SINGLETON),
        PROJECT_CONFIG(ProjectConfigProperties.SINGLETON);

        ComponentName(Object componentInstance) {
            ComponentHolder.instance.put(this.name(), componentInstance);
        }
    }

    public static Config getConfig() {
        Config config = (Config) ComponentHolder.instance.get(ComponentName.CONFIG.name());
        if (config == null) {
            config = ConfigProperties.get();
            ComponentHolder.instance.put(ComponentName.CONFIG.name(), config);
        }
        return config;
    }

    public static DatabaseConfig getDatabaseConfig() {
        return (DatabaseConfig) ComponentHolder.instance.get(ComponentName.DATABASE_CONFIG.name());
    }

    public static Packages getPackages() {
        return (Packages) ComponentHolder.instance.get(ComponentName.PACKAGES.name());
    }

    public static Paths getPaths() {
        return (Paths) ComponentHolder.instance.get(ComponentName.PATHS.name());
    }

    public static ProjectConfig getProjectConfig() {
        return (ProjectConfig) ComponentHolder.instance.get(ComponentName.PROJECT_CONFIG.name());
    }
}
