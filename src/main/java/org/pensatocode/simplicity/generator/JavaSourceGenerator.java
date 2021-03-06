package org.pensatocode.simplicity.generator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.writers.*;
import org.pensatocode.simplicity.generator.writers.generator.*;

import java.util.*;

@Log4j2
public class JavaSourceGenerator {

    private final DirectoryService dirService;
    private final JavaClassService javaService;
    private final VelocityEngine velocityEngine;

    private List<JavaSourceWriter> javaSourceWriters;

    public JavaSourceGenerator(DirectoryService dirService, JavaClassService javaService, VelocityEngine velocityEngine) {
        this.dirService = dirService;
        this.javaService = javaService;
        this.velocityEngine = velocityEngine;
    }

    public boolean generateAllSources() throws GeneratorConfigurationException {
        List<ClassOrInterfaceDeclaration> entities = javaService.getEntities();
        for(ClassOrInterfaceDeclaration entity: entities) {
            VariableDeclarator id = javaService.extractId(entity);
            if (id == null) {
                // stop processing if id was not found
                log.warn(String.format("Entity %s has no field annotated with @Id", entity.getNameAsString()));
                return false;
            }
            for (JavaSourceWriter writer: getJavaSourceWriters()) {
                if(!writer.generateSourceCode(entity, id)) {
                    // stop processing if something went wrong
                    log.warn(String.format("Entity %s could not be generated", entity.getNameAsString()));
                    return false;
                }
            }
        }
        return true;
    }

    private List<JavaSourceWriter> getJavaSourceWriters() {
        if (javaSourceWriters == null) {
            javaSourceWriters = new ArrayList<>();
            javaSourceWriters.add(new RepositoryWriter(velocityEngine, dirService));
            javaSourceWriters.add(new MapperWriter(velocityEngine, dirService, javaService));
            javaSourceWriters.add(new RepositoryImplWriter(velocityEngine, dirService));
            javaSourceWriters.add(new RestControllerWriter(velocityEngine, dirService));
            javaSourceWriters.add(new SchemaWriter(dirService, javaService));
            javaSourceWriters.add(new DataInsertWriter(dirService, javaService));
            javaSourceWriters.add(new DatasetXmlWriter(dirService, javaService));
            javaSourceWriters.add(new DatabasePropertiesWriter(dirService));
            javaSourceWriters.add(new TestSchemaWriter(dirService, javaService));
        }
        return javaSourceWriters;
    }
}
