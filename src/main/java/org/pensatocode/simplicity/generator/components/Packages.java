package org.pensatocode.simplicity.generator.components;

import java.util.Map;

public interface Packages {
    // Packages
    String getRestControllersPackage();
    String getMvcControllersPackage();
    String getMappersPackage();
    String getModelsPackage();
    String getPackageGroup();
    String getRepositoriesPackage();
    String getRepoImplementationsPackage();

    //Collections
    Map<String, String> getDestinyPackages();
}
