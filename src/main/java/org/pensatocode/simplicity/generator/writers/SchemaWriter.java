package org.pensatocode.simplicity.generator.writers;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.StringUtil;

import java.io.*;
import java.util.List;

@Log4j2
public class SchemaWriter implements JavaSourceWriter {

    private final DirectoryService dirService;
    private final JavaClassService javaService;
    private final Packages packages;

    public SchemaWriter(DirectoryService dirService, JavaClassService javaService, Packages packages) {
        this.dirService = dirService;
        this.javaService = javaService;
        this.packages = packages;
    }

    public boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id) {
        // repository implementation
        String fileAbsolutePath = dirService.getResourcesDir().getAbsolutePath()
                + File.separator
                + GeneratorUtil.SCHEMA_FILE;
        File schemaFile = new File(fileAbsolutePath);
        String tableName = StringUtil.convertToSnakeCase(entity.getNameAsString());
        // check if schema is already present
        if (schemaIsPresent(schemaFile, tableName)) {
            // do nothing
            return true;
        }
        // Schema DDL
        List<MapperVariable> variables = javaService.listMapperVariables(entity);
        String schemaDdl = createSchemaDdl(tableName, variables, id);
        // append to the schema file
        try (FileWriter fileWriter = new FileWriter(schemaFile, true)) {
            fileWriter.write(schemaDdl);
        } catch (IOException e) {
            log.warn("There was a problem appending to the schema file: " + e.getMessage());
        }
        // return
        return true;
    }

    private boolean schemaIsPresent(File schemaFile, String tableName) {
        final String ddl = "CREATE TABLE IF NOT EXISTS " + tableName.toUpperCase();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(schemaFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().toUpperCase().contains(ddl)) {
                    return true;
                }
            }
        } catch (IOException e) {
            log.warn("There was a problem reading the schema file: " + e.getMessage());
        }
        return false;
    }

    private String createSchemaDdl(String tableName, List<MapperVariable> variables, VariableDeclarator parsedId) {
        // extract id from list
        MapperVariable id = null;
        for(MapperVariable variable: variables) {
            if (variable.getName().equals(parsedId.getNameAsString())) {
                id = variable;
            }
        }
        if (id == null) {
            // exit
            return "";
        }
        // create Schema DDL
        StringBuffer sb = new StringBuffer();
        sb.append("\nCREATE SEQUENCE IF NOT EXISTS ")
                .append(tableName)
                .append("_id_seq START 1;\n\n")
                .append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(" (\n");
        sb.append("  ")
                .append(id.getName())
                .append(" ")
                .append(id.getDatabaseType().toLowerCase())
                .append(" DEFAULT nextval('")
                .append(tableName)
                .append("_id_seq') PRIMARY KEY,\n");
        int counter = 0;
        int sizeLimit = variables.size() - 1;
        for(MapperVariable variable: variables) {
            if (variable.getName().equals(id.getName())) {
                continue;
            }
            sb.append("  ")
                    .append(variable.getName())
                    .append(" ")
                    .append(variable.getDatabaseType().toLowerCase())
                    .append(" NOT NULL");
            if(counter < sizeLimit) {
                sb.append(",");
            }
            counter++;
            sb.append("\n");
        }
        sb.append(");\n");
        // return schema DDL
        return sb.toString();
    }
}
