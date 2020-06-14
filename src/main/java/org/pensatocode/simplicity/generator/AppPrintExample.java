package org.pensatocode.simplicity.generator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.Paths;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.util.ComponentBinder;
import org.pensatocode.simplicity.generator.util.ServiceBinder;

import java.util.List;

@Log4j2
public class AppPrintExample {

    public static void main(String[] args) {
        try {
            // Example using stdout
            Paths paths = ComponentBinder.getPaths();
            JavaClassService javaService = ServiceBinder.getJavaClassService();
            // Example using stdout
            printJavaClasses(paths, javaService);

        } catch (GeneratorConfigurationException e) {
            System.out.println(e.getMessage());
        }
        log.info("Success!");
    }

    public static void printJavaClasses(Paths paths, JavaClassService javaClassService) throws GeneratorConfigurationException {
        System.out.println("Simplicity is ready ...");
        System.out.println("Path = " + paths.getModelPath());
        List<ClassOrInterfaceDeclaration> classes = javaClassService.getEntities();

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