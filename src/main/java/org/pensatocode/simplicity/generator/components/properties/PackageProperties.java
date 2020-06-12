package org.pensatocode.simplicity.generator.components.properties;

import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public enum PackageProperties implements Packages {

    SINGLETON;

    // Properties file
    private final Properties properties;

    // Separators
    private static final char DOT = '.';

    // Assembled properties
    private String packageGroup;
    private String modelsPackage;
    private String restControllersPackage;
    private String mvcControllersPackage;
    private String mappersPackage;
    private String repositoriesPackage;
    private String repoImplementationsPackage;

    // Packages map
    private Map<String, String> destinyPackages;

    // Constructor
    PackageProperties() {
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
        if(StringUtil.isEmpty(getSimplicityProjectPackageGroup())) {
            throw new GeneratorConfigurationException("Package group not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectPackageModel())) {
            throw new GeneratorConfigurationException("Models package not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectPackageRestControllers())) {
            throw new GeneratorConfigurationException("RestControllers package not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectPackageMvcControllers())) {
            throw new GeneratorConfigurationException("MvcControllers package not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectPackageRepositories())) {
            throw new GeneratorConfigurationException("Repositories package not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectPackageRepositoriesImpl())) {
            throw new GeneratorConfigurationException("RepoImplementations package not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectPackageMappers())) {
            throw new GeneratorConfigurationException("Mappers package not found");
        }
    }

    private void assemblePackages() {
        // group package
        packageGroup = this.getSimplicityProjectPackageGroup();
        // origin package
        modelsPackage = packageGroup + DOT + this.getSimplicityProjectPackageModel();
        // simplicity packages
        destinyPackages = new TreeMap<>();
        restControllersPackage = packageGroup + DOT + this.getSimplicityProjectPackageRestControllers();
        mvcControllersPackage = packageGroup + DOT + this.getSimplicityProjectPackageMvcControllers();
        destinyPackages.put(GeneratorUtil.REST_CONTROLLERS_KEY, restControllersPackage);
        mappersPackage = packageGroup + DOT + this.getSimplicityProjectPackageMappers();
        destinyPackages.put(GeneratorUtil.MAPPERS_KEY, mappersPackage);
        repositoriesPackage = packageGroup + DOT + this.getSimplicityProjectPackageRepositories();
        destinyPackages.put(GeneratorUtil.REPOSITORIES_KEY, repositoriesPackage);
        repoImplementationsPackage = packageGroup + DOT + this.getSimplicityProjectPackageRepositoriesImpl();
        destinyPackages.put(GeneratorUtil.REPOSITORY_IMPL_KEY, repoImplementationsPackage);
    }

    /*
        Assembled package methods
     */

    public String getPackageGroup() {
        return packageGroup;
    }

    public String getModelsPackage() {
        return modelsPackage;
    }

    public String getRestControllersPackage() {
        return restControllersPackage;
    }

    public String getMvcControllersPackage() {
        return mvcControllersPackage;
    }

    public String getMappersPackage() {
        return mappersPackage;
    }

    public String getRepositoriesPackage() {
        return repositoriesPackage;
    }

    public String getRepoImplementationsPackage() {
        return repoImplementationsPackage;
    }

    /*
        Collections
     */

    public Map<String, String> getDestinyPackages() {
        if (destinyPackages == null) {
            log.warn("Packages map should not be null");
            return Collections.emptyMap();
        }
        return destinyPackages;
    }

    /*
        Properties direct methods
     */

    private String getSimplicityProjectPackageGroup() {
        return properties.getProperty("simplicity.starter.package.group");
    }

    private String getSimplicityProjectPackageModel() {
        return properties.getProperty("simplicity.generator.package.model");
    }

    private String getSimplicityProjectPackageRestControllers() {
        return properties.getProperty("simplicity.generator.package.restControllers");
    }

    private String getSimplicityProjectPackageMvcControllers() {
        return properties.getProperty("simplicity.generator.package.mvcControllers");
    }

    private String getSimplicityProjectPackageRepositories() {
        return properties.getProperty("simplicity.generator.package.repositories");
    }

    private String getSimplicityProjectPackageRepositoriesImpl() {
        return properties.getProperty("simplicity.generator.package.repositoriesImpl");
    }

    private String getSimplicityProjectPackageMappers() {
        return properties.getProperty("simplicity.generator.package.mappers");
    }
}
