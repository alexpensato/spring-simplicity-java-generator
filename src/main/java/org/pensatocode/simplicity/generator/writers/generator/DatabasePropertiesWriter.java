package org.pensatocode.simplicity.generator.writers.generator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.DatabaseConfig;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.services.Platform;
import org.pensatocode.simplicity.generator.util.*;
import org.pensatocode.simplicity.generator.writers.JavaSourceWriter;

import java.io.File;
import java.util.List;

@Log4j2
public class DatabasePropertiesWriter implements JavaSourceWriter {

    private final DirectoryService dirService;
    private final DatabaseConfig databaseConfig;

    public DatabasePropertiesWriter(DirectoryService dirService) {
        this.dirService = dirService;
        this.databaseConfig = ComponentBinder.getDatabaseConfig();
    }

    public boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id) {
        // repository implementation
        String springFileAbsolutePath = dirService.getResourcesDir().getAbsolutePath()
                + File.separator
                + GeneratorUtil.SPRING_PROPERTIES_FILE;
        String hikariFileAbsolutePath = dirService.getResourcesDir().getAbsolutePath()
                + File.separator
                + GeneratorUtil.HIKARI_PROPERTIES_FILE;
        // platform
        Platform platform = databaseConfig.getPlatform();
        // Replace Spring properties
        FileUtil.replacePropertiesValues(springFileAbsolutePath, databaseConfig.getSpringPropertiesValues());
        // Replace Spring properties
        FileUtil.replacePropertiesValues(hikariFileAbsolutePath, databaseConfig.getHikariPropertiesValues());
        // return
        return true;
    }
}
