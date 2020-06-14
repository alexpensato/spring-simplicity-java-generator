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
public class DataInsertWriter implements JavaSourceWriter {

    private final DirectoryService dirService;
    private final JavaClassService javaService;
    private final DatabaseConfig databaseConfig;

    public DataInsertWriter(DirectoryService dirService, JavaClassService javaService) {
        this.dirService = dirService;
        this.javaService = javaService;
        this.databaseConfig = ComponentBinder.getDatabaseConfig();
    }

    public boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id) {
        // repository implementation
        String fileAbsolutePath = dirService.getResourcesDir().getAbsolutePath()
                + File.separator
                + GeneratorUtil.DATA_FILE;
        File dataFile = new File(fileAbsolutePath);
        String tableName = StringUtil.convertToSnakeCase(entity.getNameAsString());
        // check if schema is already present
        if (SchemaUtil.checkForDataInsertPresent(dataFile, tableName)) {
            // do nothing
            return true;
        }
        // platform
        Platform platform = databaseConfig.getPlatform();
        // Schema DDL
        List<MapperVariable> variables = javaService.listMapperVariables(entity, platform);
        String insertDdl = SchemaUtil.createInsertDdl(tableName, variables, id);
        FileUtil.appendLine(dataFile, insertDdl);
        // return
        return true;
    }
}
