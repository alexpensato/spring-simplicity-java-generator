package org.pensatocode.simplicity.generator.components;

import java.util.Map;

public interface Paths {
    // Paths
    String getProjectPath();
    String getRootPackagePath();
    String getRestControllersPath();
    String getMvcControllersPath();
    String getMappersPath();
    String getModelPath();
    String getRepositoriesPath();
    String getRepoImplementationsPath();
    String getResourcesPath();
    String getTestJavaSourcePath();
    String getTestResourcesPath();
    String getTestRepositoriesPath();

    //Collections
    Map<String, String> getDestinyPaths();
}
