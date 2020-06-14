package org.pensatocode.simplicity.generator.util;

import java.util.ArrayList;
import java.util.List;

public class GeneratorUtil {

    private GeneratorUtil() {
        // Util
    }

    private static final List<String> destinyKeys = new ArrayList<>();

    // Suffixes
    public static final String REPOSITORY_SUFFIX = "Repository";
    public static final String REPOSITORY_IMPL_SUFFIX = "RepositoryImpl";
    public static final String MAPPER_SUFFIX = "Mapper";
    public static final String REST_CONTROLLER_SUFFIX = "RestController";
    public static final String MVC_CONTROLLER_SUFFIX = "MvcController";

    // Extension
    public static final String JAVA_EXTENSION = ".java";
    public static final String CLASS_EXTENSION = ".class";

    // Project files, classes and dirs
    public static final String BUILD_GRADLE_FILE_NAME = "build.gradle.kts";
    public static final String SETTINGS_GRADLE_FILE_NAME = "settings.gradle.kts";
    public static final String MAIN_APPLICATION_CLASS_NAME = "Application.java";
    public static final String SERVLET_INITIALIZER_CLASS_NAME = "ServletInitializer.java";
    public static final String DB_CONFIG_DIR_NAME = "config";
    public static final String DB_CONFIG_CLASS_NAME = "DbConfig.java";
    public static final String TEST_APP_CLASS_NAME = "AppTests.java";
    public static final String ZIPS_DIR_NAME = "zips";
    public static final String PROJECT_ZIP_FILE_NAME = "simplicity-project-template.zip";
    public static final String LIBS_DIR_NAME = "libs";
    public static final String SIMPLICITY_JAR_FILE_NAME = "spring-simplicity-java.jar";

    // Fixed files
    public static final String SCHEMA_FILE = "schema.sql";
    public static final String DATA_FILE = "data.sql";
    public static final String DATASET_XML_FILE = "dataset.xml";

    // Separator
    public static final String DOT = ".";

    // Keys
    public static final String PROJECT_KEY = "Project";
    public static final String ORIGIN_KEY = "Origin (model)";
    public static final String MODELS_KEY = "Model";
    public static final String REST_CONTROLLERS_KEY = "Rest-Controllers";
    public static final String MVC_CONTROLLERS_KEY = "Mvc-Controllers";
    public static final String REPOSITORIES_KEY = "Repository-Interfaces";
    public static final String REPOSITORY_IMPL_KEY = "Repository-Implementations";
    public static final String MAPPERS_KEY = "Mappers";

    // Extra keys
    public static final String ROOT_PACKAGE_KEY = "Root-Package";
    public static final String RESOURCES_KEY = "Resources";
    public static final String TEST_JAVA_SOURCE_KEY = "Test-Java-Source";
    public static final String TEST_RESOURCES_KEY = "Test-Resources";
    public static final String TEST_REPOSITORIES_KEY = "Test-Repositories";

    public static List<String> getDestinyKeys() {
        if (destinyKeys.isEmpty()) {
            destinyKeys.add(REPOSITORIES_KEY);
            destinyKeys.add(MAPPERS_KEY);
            destinyKeys.add(MODELS_KEY);
            destinyKeys.add(REPOSITORY_IMPL_KEY);
            destinyKeys.add(REST_CONTROLLERS_KEY);
            destinyKeys.add(MVC_CONTROLLERS_KEY);
            destinyKeys.add(TEST_REPOSITORIES_KEY);
        }
        return destinyKeys;
    }

}
