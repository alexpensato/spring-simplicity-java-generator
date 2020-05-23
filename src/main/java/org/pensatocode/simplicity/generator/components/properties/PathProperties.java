package org.pensatocode.simplicity.generator.components.properties;

import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.components.Paths;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.io.File;
import java.io.IOException;
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
    private String modelPath;
    private String controllersPath;
    private String repositoriesPath;
    private String repoImplementationsPath;
    private String mappersPath;

    // Paths map
    private Map<String, String> destinyPaths;

    // Constructor
    PathProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("simplicity.properties"));
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
        // base java path
        final String baseJavaPath = fixPath(this.getSimplicityProjectPath())
                + fixPath(this.getSimplicityProjectJavaSource());
        // simplicity origin path
        modelPath = baseJavaPath + fixPath(packages.getModelsPackage());
        // simplicity destiny paths
        destinyPaths = new TreeMap<>();
        controllersPath = baseJavaPath + fixPath(packages.getControllersPackage());
        destinyPaths.put(GeneratorUtil.CONTROLLERS_KEY, controllersPath);
        repositoriesPath = baseJavaPath + fixPath(packages.getRepositoriesPackage());
        destinyPaths.put(GeneratorUtil.REPOSITORIES_KEY, repositoriesPath);
        repoImplementationsPath = baseJavaPath + fixPath(packages.getRepoImplementationsPackage());
        destinyPaths.put(GeneratorUtil.REPOSITORY_IMPL_KEY, repoImplementationsPath);
        mappersPath = baseJavaPath + fixPath(packages.getMappersPackage());
        destinyPaths.put(GeneratorUtil.MAPPERS_KEY, mappersPath);
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

    public String getModelPath() {
        return modelPath;
    }

    public String getControllersPath() {
        return controllersPath;
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

    private String getSimplicityProjectPath() {
        return properties.getProperty("simplicity.generator.projectPath");
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
