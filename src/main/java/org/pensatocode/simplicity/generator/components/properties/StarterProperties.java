package org.pensatocode.simplicity.generator.components.properties;

import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.Starter;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public enum StarterProperties implements Starter {

    SINGLETON;

    // Properties file
    private final Properties properties;

    // Assembled properties
    private String projectName;
    private String projectPath;
    private String packageGroup;

    // Constructor
    StarterProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("simplicity-generator.properties"));
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
        if(StringUtil.isEmpty(getSimplicityProjectName())) {
            throw new GeneratorConfigurationException("Project Name not found");
        }
    }

    private void assemblePackages() {
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
