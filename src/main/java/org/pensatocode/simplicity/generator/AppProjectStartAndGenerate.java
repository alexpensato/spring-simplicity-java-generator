package org.pensatocode.simplicity.generator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.PathUtil;
import org.pensatocode.simplicity.generator.util.ServiceBinder;

import java.io.File;
import java.util.List;

@Log4j2
public class AppProjectStartAndGenerate {

    public static void main(String[] args) {
        AppProjectStarter.main(args);
        entitiesParser();
        AppProjectGenerator.main(args);
    }

    private static void entitiesParser() {
        try {
            // Generator directory
            String currentPath = System.getProperty("user.dir");
            // Entities directory
            String entitiesPath = PathUtil.fixPath(currentPath + File.separator + GeneratorUtil.ENTITIES_DIR_NAME);
            log.info(String.format("Local entities directory is %s", entitiesPath));
            File entitiesDir = new File(entitiesPath);
            if (!entitiesDir.isDirectory() || entitiesDir.listFiles() == null) {
                String message = "Local entities directory is empty or not a directory. Fix it before proceeding.";
                log.warn(message);
                throw new GeneratorConfigurationException(message);
            }
            // Retrieve entities
            JavaClassService javaClassService = ServiceBinder.getJavaClassService();
            List<ClassOrInterfaceDeclaration> entities = javaClassService.getEntities(entitiesDir, false);
            for (ClassOrInterfaceDeclaration entity: entities) {
                javaClassService.createEntitySource(entity);
            }
            log.info("Success!");

        } catch (GeneratorConfigurationException e) {
            e.printStackTrace();
            log.error("FAIL: " + e.getMessage());
        }
    }

}