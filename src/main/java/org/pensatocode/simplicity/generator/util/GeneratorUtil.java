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
    public static final String CONTROLLER_SUFFIX = "Controller";

    // Extension
    public static final String JAVA_EXTENSION = ".java";
    public static final String CLASS_EXTENSION = ".class";

    // Fixed files
    public static final String SCHEMA_FILE = "schema.sql";

    // Separator
    public static final String DOT = ".";

    // Keys
    public static final String ORIGIN_KEY = "Origin (model)";
    public static final String MODELS_KEY = "Model";
    public static final String CONTROLLERS_KEY = "Controllers";
    public static final String REPOSITORIES_KEY = "Repository-Interfaces";
    public static final String REPOSITORY_IMPL_KEY = "Repository-Implementations";
    public static final String MAPPERS_KEY = "Mappers";

    // Extra keys
    public static final String RESOURCES_KEY = "Resources";
    public static final String TEST_RESOURCES_KEY = "Test-Resources";
    public static final String TEST_REPOSITORIES_KEY = "Test-Repositories";

    public static List<String> getDestinyKeys() {
        if (destinyKeys.isEmpty()) {
            destinyKeys.add(REPOSITORIES_KEY);
            destinyKeys.add(MAPPERS_KEY);
            destinyKeys.add(REPOSITORY_IMPL_KEY);
            destinyKeys.add(CONTROLLERS_KEY);
        }
        return destinyKeys;
    }

}
