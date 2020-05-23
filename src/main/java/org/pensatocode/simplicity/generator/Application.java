package org.pensatocode.simplicity.generator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.components.properties.PackageProperties;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.components.Paths;
import org.pensatocode.simplicity.generator.components.properties.PathProperties;
import org.pensatocode.simplicity.generator.util.VelocityUtil;

import java.util.List;

@Log4j2
public class Application {

    public static void main(String[] args) {
        try {
            // Example using stdout
            Paths paths = PathProperties.SINGLETON;
            JavaClassService javaService = JavaClassService.SINGLETON;
            DirectoryService dirService = DirectoryService.SINGLETON;
            // Example using stdout
            printJavaClasses(paths, javaService);
            // Create dirs
            if (!dirService.createDirectories()) {
                System.out.println("There was a problem creating directories.");
            }
            // Create all classes
            Packages packages = PackageProperties.SINGLETON;
            VelocityEngine velocityEngine = VelocityUtil.getVelocityEngine();
            JavaSourceGenerator generator = new JavaSourceGenerator(packages, dirService, javaService, velocityEngine);
            if (!generator.generateAllSources()) {
                System.out.println("There was a problem generating source classes.");
            }
        } catch (GeneratorConfigurationException e) {
            System.out.println(e.getMessage());
        }
        log.info("Success!");
    }

    public static void printJavaClasses(Paths paths, JavaClassService javaClassService) throws GeneratorConfigurationException {
        System.out.println("Simplicity is ready ...");
        System.out.println("Path = " + paths.getModelPath());
        List<ClassOrInterfaceDeclaration> classes = javaClassService.getJavaClasses();

        for (ClassOrInterfaceDeclaration javaClass: classes) {
            String qualifiedName = javaClass.getFullyQualifiedName().orElse("");
            System.out.println(String.format("JavaClass: %s", qualifiedName));
            for(FieldDeclaration field: javaClass.getFields()) {
                VariableDeclarator variable = field.getVariable(0);
                System.out.println(String.format(" * var %s: %s", variable.getName(), variable.getType()));
            }
        }
    }
}