package org.pensatocode.simplicity.generator;

import lombok.extern.log4j.Log4j2;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.util.ServiceBinder;
import org.pensatocode.simplicity.generator.util.VelocityUtil;

@Log4j2
public class AppGenerator {

    public static void main(String[] args) {
        try {
            // Create dirs
            DirectoryService dirService = ServiceBinder.getDirectoryService();
            if (!dirService.createGeneratorDirectories()) {
                throw new GeneratorConfigurationException("There was a problem creating directories.");
            }
            // Create all classes
            JavaClassService javaService = ServiceBinder.getJavaClassService();
            VelocityEngine velocityEngine = VelocityUtil.getVelocityEngine();
            JavaSourceGenerator generator = new JavaSourceGenerator(dirService, javaService, velocityEngine);
            if (!generator.generateAllSources()) {
                throw new GeneratorConfigurationException("There was a problem generating source classes.");
            }
            log.info("Success!");

        } catch (GeneratorConfigurationException e) {
            log.error("FAIL: " + e.getMessage());
        }
    }

}