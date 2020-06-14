package org.pensatocode.simplicity.generator.util;

import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.DatabaseConfig;
import org.pensatocode.simplicity.generator.model.DefaultSqlSampleData;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.services.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public final class SchemaUtil {

    private SchemaUtil() {
        // Util
    }

    public static boolean checkForSchemaPresent(File schemaFile, String tableName) {
        final String ddl = "CREATE TABLE IF NOT EXISTS " + tableName.toUpperCase();
        return checkForTextPresentInLine(schemaFile, ddl);
    }

    public static boolean checkForDataInsertPresent(File dataFile, String tableName) {
        final String ddl = "INSERT INTO " + tableName.toUpperCase();
        return checkForTextPresentInLine(dataFile, ddl);
    }

    public static boolean checkForDatasetXmlPresent(File dataFile, String tableName) {
        final String ddl = "<" + tableName.toUpperCase() + " ";
        return checkForTextPresentInLine(dataFile, ddl);
    }

    private static boolean checkForTextPresentInLine(File file, String text) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().toUpperCase().contains(text)) {
                    return true;
                }
            }
        } catch (IOException e) {
            log.warn("There was a problem reading the file: " + e.getMessage());
        }
        return false;
    }

    public static String createSchemaDdl(Platform platform,
                                         String tableName,
                                         List<MapperVariable> variables,
                                         VariableDeclarator parsedId,
                                         DatabaseConfig databaseConfig) {
        // extract id from list
        MapperVariable id = extractIdFromList(variables, parsedId);
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

    private static MapperVariable extractIdFromList(List<MapperVariable> variables, VariableDeclarator parsedId) {
        MapperVariable id = null;
        for(MapperVariable variable: variables) {
            if (variable.getName().equals(parsedId.getNameAsString())) {
                id = variable;
            }
        }
        return id;
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

    public static String createInsertDdl(String tableName, List<MapperVariable> variables, VariableDeclarator parsedId) {
        // extract id from list
        MapperVariable id = extractIdFromList(variables, parsedId);
        if (id == null) {
            // exit
            return "";
        }
        // list schema names and types
        List<String> schemaNames = new ArrayList<>();
        List<String> sqlTypes = new ArrayList<>();
        for(MapperVariable variable: variables) {
            if (variable.getName().equals(id.getName())) {
                continue;
            }
            schemaNames.add(variable.getSchemaName());
            sqlTypes.add(variable.getType().getJavaType());
        }
        // create Schema DDL
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ")
                .append(tableName)
                .append(" (")
                .append(String.join(",", schemaNames))
                .append(") VALUES (")
                .append(convertSqlTypesIntoSampleData(sqlTypes))
                .append(");\n\n");
        // return schema DDL
        return sb.toString();
    }

    public static String createDatasetXmlDdl(String tableName, List<MapperVariable> variables, VariableDeclarator parsedId) {
        // extract id from list
        MapperVariable id = extractIdFromList(variables, parsedId);
        if (id == null) {
            // exit
            return "";
        }
        // create Schema DDL
        StringBuilder sb = new StringBuilder();
        sb.append("    <")
                .append(tableName)
                .append(" ");
        for(MapperVariable variable: variables) {
            if (variable.getName().equals(id.getName())) {
                continue;
            }
            String sampleData = DefaultSqlSampleData.getSampleData(variable.getType().getJavaType());
            if (sampleData!=null) {
                sampleData = sampleData.replace("'", "");
            }
            sb.append(variable.getSchemaName())
                    .append("='")
                    .append(sampleData)
                    .append("' ");
        }
        sb.append("/>\n");
        // return schema DDL
        return sb.toString();
    }

    private static String convertSqlTypesIntoSampleData(List<String> sqlTypes) {
        List<String> sampleData = new ArrayList<>();
        for(String typeName: sqlTypes) {
            sampleData.add(DefaultSqlSampleData.getSampleData(typeName));
        }
        return String.join(",", sampleData);
    }
}
