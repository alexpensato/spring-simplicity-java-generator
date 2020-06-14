package org.pensatocode.simplicity.generator.components.properties;

import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.components.Paths;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public enum PathProperties implements Paths {

    SINGLETON;

    // Properties file
    private final Properties properties;

    // Package Util
    private final Packages packages;

    // Separators
    private static final char DOT = '.';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char LINUX_SEPARATOR = '/';

    private final char WRONG_SEPARATOR;

    // Assembled properties
    private String projectPath;
    private String rootPackagePath;
    private String modelPath;
    private String restControllersPath;
    private String mvcControllersPath;
    private String repositoriesPath;
    private String repoImplementationsPath;
    private String mappersPath;
    private String resourcesPath;
    private String testJavaSourcePath;
    private String testResourcesPath;
    private String testRepositoriesPath;

    // Paths map
    private Map<String, String> destinyPaths;

    // Constructor
    PathProperties() {
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
        WRONG_SEPARATOR = loadWrongSeparator();
        packages = PackageProperties.SINGLETON;
        this.assemblePaths();
    }

    /*
        Initialization methods
     */

    private void simplicityValidation() throws GeneratorConfigurationException {
        if(StringUtil.isEmpty(getSimplicityProjectName())) {
            throw new GeneratorConfigurationException("Project name not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectPath())) {
            throw new GeneratorConfigurationException("Project path not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectJavaSource())) {
            throw new GeneratorConfigurationException("Java source set not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectJavaResources())) {
            throw new GeneratorConfigurationException("Java resources set not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectTestSource())) {
            throw new GeneratorConfigurationException("Test source set not found");
        }
        if(StringUtil.isEmpty(getSimplicityProjectTestResources())) {
            throw new GeneratorConfigurationException("Test resources set not found");
        }
    }

    private void assemblePaths() {
        // project path
        projectPath = fixPath(this.getSimplicityProjectPath()) + this.getSimplicityProjectName();
        // base java path
        final String baseJavaPath = fixPath(this.getSimplicityProjectPath())
                + fixPath(this.getSimplicityProjectJavaSource());
        // root package
        rootPackagePath = baseJavaPath + fixPath(packages.getPackageGroup());
        // destiny paths used for starting a project
        destinyPaths = new TreeMap<>();
        // simplicity origin path
        modelPath = baseJavaPath + fixPath(packages.getModelsPackage());
        destinyPaths.put(GeneratorUtil.MODELS_KEY, modelPath);
        // simplicity destiny paths
        restControllersPath = baseJavaPath + fixPath(packages.getRestControllersPackage());
        destinyPaths.put(GeneratorUtil.REST_CONTROLLERS_KEY, restControllersPath);
        mvcControllersPath = baseJavaPath + fixPath(packages.getMvcControllersPackage());
        destinyPaths.put(GeneratorUtil.MVC_CONTROLLERS_KEY, mvcControllersPath);
        repositoriesPath = baseJavaPath + fixPath(packages.getRepositoriesPackage());
        destinyPaths.put(GeneratorUtil.REPOSITORIES_KEY, repositoriesPath);
        repoImplementationsPath = baseJavaPath + fixPath(packages.getRepoImplementationsPackage());
        destinyPaths.put(GeneratorUtil.REPOSITORY_IMPL_KEY, repoImplementationsPath);
        mappersPath = baseJavaPath + fixPath(packages.getMappersPackage());
        destinyPaths.put(GeneratorUtil.MAPPERS_KEY, mappersPath);
        // resources
        resourcesPath = fixPath(this.getSimplicityProjectPath())
                + fixPath(this.getSimplicityProjectJavaResources());
        // test-resources
        testResourcesPath = fixPath(this.getSimplicityProjectPath())
                + fixPath(this.getSimplicityProjectTestResources());
        // test-classes-path
        final String baseTestPath = fixPath(this.getSimplicityProjectPath())
                + fixPath(this.getSimplicityProjectTestSource());
        testJavaSourcePath = baseTestPath + fixPath(packages.getPackageGroup());
        testRepositoriesPath = baseTestPath + fixPath(packages.getRepositoriesPackage());
        destinyPaths.put(GeneratorUtil.TEST_REPOSITORIES_KEY, testRepositoriesPath);
    }

    private String fixPath(String str) {
        if (str == null) {
            return File.separator;
        }
        String tmpStr = str.replace(WRONG_SEPARATOR, File.separatorChar);
        tmpStr = tmpStr.replace(DOT, File.separatorChar);
        if (tmpStr.charAt(str.length()-1) == File.separatorChar) {
            return tmpStr;
        }
        return tmpStr + File.separator;
    }

    private char loadWrongSeparator() {
        if (File.separatorChar == WINDOWS_SEPARATOR) {
            return LINUX_SEPARATOR;
        } else {
            return WINDOWS_SEPARATOR;
        }
    }

    /*
        Assembled paths methods
     */

    public String getProjectPath() {
        return projectPath;
    }

    public String getRootPackagePath() {
        return rootPackagePath;
    }

    public String getModelPath() {
        return modelPath;
    }

    public String getRestControllersPath() {
        return restControllersPath;
    }

    public String getMvcControllersPath() {
        return mvcControllersPath;
    }

    public String getRepositoriesPath() {
        return repositoriesPath;
    }

    public String getRepoImplementationsPath() {
        return repoImplementationsPath;
    }

    public String getMappersPath() {
        return mappersPath;
    }

    public String getResourcesPath() {
        return resourcesPath;
    }

    public String getTestResourcesPath() {
        return testResourcesPath;
    }

    public String getTestRepositoriesPath() {
        return testRepositoriesPath;
    }

    public String getTestJavaSourcePath() {
        return testJavaSourcePath;
    }

    /*
        Collections
     */

    public Map<String, String> getDestinyPaths() {
        if (destinyPaths == null) {
            log.warn("Destiny paths should not be null");
            return Collections.emptyMap();
        }
        return destinyPaths;
    }

    /*
        Properties direct methods
     */

    private String getSimplicityProjectName() {
        return properties.getProperty("simplicity.starter.projectName");
    }

    private String getSimplicityProjectPath() {
        return properties.getProperty("simplicity.starter.projectPath");
    }

    private String getSimplicityProjectJavaSource() {
        return properties.getProperty("simplicity.generator.javaSrc");
    }

    private String getSimplicityProjectJavaResources() {
        return properties.getProperty("simplicity.generator.javaResources");
    }

    private String getSimplicityProjectTestSource() {
        return properties.getProperty("simplicity.generator.testSrc");
    }

    private String getSimplicityProjectTestResources() {
        return properties.getProperty("simplicity.generator.testResources");
    }

}
