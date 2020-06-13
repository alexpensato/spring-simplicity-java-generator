package org.pensatocode.simplicity.generator.writers.generator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.DatabaseConfig;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.services.Platform;
import org.pensatocode.simplicity.generator.services.impl.TestPlatform;
import org.pensatocode.simplicity.generator.util.*;
import org.pensatocode.simplicity.generator.writers.JavaSourceWriter;

import java.io.*;
import java.util.List;

@Log4j2
public class TestSchemaWriter implements JavaSourceWriter {

    private final DirectoryService dirService;
    private final JavaClassService javaService;
    private final DatabaseConfig databaseConfig;

    public TestSchemaWriter(DirectoryService dirService, JavaClassService javaService) {
        this.dirService = dirService;
        this.javaService = javaService;
        this.databaseConfig = ComponentBinder.getDatabaseConfig();
    }

    public boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id) {
        // repository implementation
        String fileAbsolutePath = dirService.getTestResourcesDir().getAbsolutePath()
                + File.separator
                + GeneratorUtil.SCHEMA_FILE;
        File testSchemaFile = new File(fileAbsolutePath);
        String tableName = StringUtil.convertToSnakeCase(entity.getNameAsString());
        // check if schema is already present
        if (SchemaUtil.checkForSchemaPresent(testSchemaFile, tableName)) {
            // do nothing
            return true;
        }
        // platform
        Platform testPlatform = new TestPlatform();
        // Schema DDL
        List<MapperVariable> variables = javaService.listMapperVariables(entity, testPlatform);
        String testSchemaDdl = SchemaUtil.createSchemaDdl(testPlatform, tableName, variables, id, databaseConfig);
        // append to the schema file
        try (FileWriter fileWriter = new FileWriter(testSchemaFile, true)) {
            fileWriter.write(testSchemaDdl);
        } catch (IOException e) {
            log.warn("There was a problem appending to the test schema file: " + e.getMessage());
        }
        // return
        return true;
    }

}
