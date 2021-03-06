package org.pensatocode.simplicity.generator.util;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.Map;

@Log4j2
public final class FileUtil {

    private FileUtil() {
        // Util
    }

    public static void appendLine(File file, String newLine) {
        // append to the file
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(newLine);
        } catch (IOException e) {
            log.warn("There was a problem appending to the file: " + e.getMessage());
        }
    }

    public static void appendBeforeMatchingLine(String fileName, String patternMatch, String newLine) {
        // read file content and append newLine
        StringBuilder content = new StringBuilder();
        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {
            String line = buffer.readLine();
            while (line != null) {
                if(line.contains(patternMatch)) {
                    break;
                }
                content.append(line);
                content.append('\n');
                line = buffer.readLine();
            }
            content.append(newLine);
            content.append(line);

        } catch (Exception e) {
            log.warn("There was a problem reading the input file: " + e.getMessage());
        }
        // write over the file
        writeOverFile(fileName, content);
    }

    public static void replacePropertiesValues(String fileName, Map<String,String> propertiesValues) {
        // read file content and replace properties values
        StringBuilder content = new StringBuilder();
        try (BufferedReader buffer = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if(line.length() > 0  && line.charAt(0) != '#') {
                    int idxEqualSign = line.indexOf('=');
                    String propertyName = line.substring(0, idxEqualSign);
                    String propertyValue = propertiesValues.get(propertyName);
                    if (StringUtil.isNotEmpty(propertyValue)) {
                        line = propertyName + "=" + propertyValue;
                    }
                }
                content.append(line);
                content.append('\n');
            }

        } catch (Exception e) {
            log.warn("There was a problem reading the input file: " + e.getMessage());
        }
        // write over the file
        writeOverFile(fileName, content);
    }

    private static void writeOverFile(String fileName, StringBuilder content) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            fileOut.write(content.toString().getBytes());
        } catch (Exception e) {
            log.warn("There was a problem writing to the file: " + e.getMessage());
        }
    }

}
