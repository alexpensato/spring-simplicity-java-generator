package org.pensatocode.simplicity.generator;

import lombok.extern.log4j.Log4j2;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.components.properties.PackageProperties;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.util.VelocityUtil;

@Log4j2
public class AppGenerator {

    public static void main(String[] args) {
        try {
            // Create dirs
            DirectoryService dirService = DirectoryService.SINGLETON;
            if (!dirService.createGeneratorDirectories()) {
                throw new GeneratorConfigurationException("There was a problem creating directories.");
            }
            // Create all classes
            Packages packages = PackageProperties.SINGLETON;
            JavaClassService javaService = JavaClassService.SINGLETON;
            VelocityEngine velocityEngine = VelocityUtil.getVelocityEngine();
            JavaSourceGenerator generator = new JavaSourceGenerator(packages, dirService, javaService, velocityEngine);
            if (!generator.generateAllSources()) {
                throw new GeneratorConfigurationException("There was a problem generating source classes.");
            }
            log.info("Success!");

        } catch (GeneratorConfigurationException e) {
            log.error("FAIL: " + e.getMessage());
        }
    }

}