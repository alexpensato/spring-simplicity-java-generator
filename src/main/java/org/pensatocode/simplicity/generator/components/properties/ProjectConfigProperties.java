package org.pensatocode.simplicity.generator.components.properties;

import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.ProjectConfig;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public enum ProjectConfigProperties implements ProjectConfig {

    SINGLETON;

    // Properties file
    private final Properties properties;

    // Assembled properties
    private String projectName;
    private String projectPath;
    private String packageGroup;

    // Constructor
    ProjectConfigProperties() {
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
        this.assembleProperties();
    }

    /*
        Initialization methods
     */

    private void simplicityValidation() throws GeneratorConfigurationException {
        if(StringUtil.isEmpty(getSimplicityProjectName())) {
            throw new GeneratorConfigurationException("Project Name not found");
        }
    }

    private void assembleProperties() {
        // project name
        projectName = this.getSimplicityProjectName();
        // project path
        projectPath = PathProperties.SINGLETON.getProjectPath();
        // pacakge group
        packageGroup = PackageProperties.SINGLETON.getPackageGroup();
    }

    /*
        Assembled methods
     */

    public String getProjectName() {
        return projectName;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getPackageGroup() {
        return packageGroup;
    }

    /*
        Properties direct methods
     */

    private String getSimplicityProjectName() {
        return properties.getProperty("simplicity.starter.projectName");
    }

}
