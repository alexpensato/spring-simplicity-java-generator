package org.pensatocode.simplicity.generator.util;

import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.DatabaseConfig;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.services.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Log4j2
public final class SchemaUtil {

    private SchemaUtil() {
        // Util
    }

    public static boolean checkForSchemaPresent(File schemaFile, String tableName) {
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

    public static String createSchemaDdl(Platform platform,
                                         String tableName,
                                         List<MapperVariable> variables,
                                         VariableDeclarator parsedId,
                                         DatabaseConfig databaseConfig) {
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
        StringBuilder sb = new StringBuilder();
        createSequenceDdl(platform, tableName, sb);
        createTableDdl(platform, tableName, id, variables, sb);
        createGrantDdl(platform, tableName, databaseConfig, sb);

        // return schema DDL
        return sb.toString();
    }

    private static void createSequenceDdl(Platform platform,
                                          String tableName,
                                          StringBuilder sb) {
        if (platform.isSequenceNecessary()) {
            sb.append("CREATE SEQUENCE IF NOT EXISTS ")
                    .append(tableName)
                    .append("_id_seq START 1;\n\n");
        }
    }

    private static void createTableDdl(Platform platform,
                                       String tableName,
                                       MapperVariable id,
                                       List<MapperVariable> variables,
                                       StringBuilder sb) {
        sb.append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(" (\n");
        // Platform-specific command
        platform.appendPrimaryKey(tableName, id, sb);
        // Database table columns
        int counter = 0;
        int sizeLimit = variables.size() - 2; // id is removed
        for(MapperVariable variable: variables) {
            if (variable.getName().equals(id.getName())) {
                continue;
            }
            sb.append("  ")
                    .append(variable.getSchemaName())
                    .append(" ")
                    .append(variable.getType().getSqlType().toLowerCase())
                    .append(" NOT NULL");
            if(counter < sizeLimit) {
                sb.append(",");
            }
            counter++;
            sb.append("\n");
        }
        sb.append(");\n\n");
    }

    private static void createGrantDdl(Platform platform,
                                       String tableName,
                                       DatabaseConfig databaseConfig,
                                       StringBuilder sb) {
        if (platform.areGrantsNecessary()) {
            sb.append("GRANT USAGE, SELECT ON SEQUENCE ")
                    .append(tableName)
                    .append("_id_seq TO ")
                    .append(databaseConfig.getUser())
                    .append(";\n")
                    .append("GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE ")
                    .append(tableName)
                    .append(" TO ")
                    .append(databaseConfig.getUser())
                    .append(";\n\n");
        }
    }
}
