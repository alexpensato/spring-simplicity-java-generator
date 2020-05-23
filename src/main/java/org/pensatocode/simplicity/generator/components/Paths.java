package org.pensatocode.simplicity.generator.components;

import java.util.Map;

public interface Paths {
    // Paths
    String getControllersPath();
    String getMappersPath();
    String getModelPath();
    String getRepositoriesPath();
    String getRepoImplementationsPath();
    String getResourcesPath();
    String getTestResourcesPath();
    String getTestRepositoriesPath();

    //Collections
    Map<String, String> getDestinyPaths();
}
