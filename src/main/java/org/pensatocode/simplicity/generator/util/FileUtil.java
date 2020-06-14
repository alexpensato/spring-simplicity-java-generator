package org.pensatocode.simplicity.generator.util;

import lombok.extern.log4j.Log4j2;

import java.io.*;

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
        try {
            // append the newLine in the StringBuilder input
            BufferedReader file = new BufferedReader(new FileReader(fileName));
            StringBuilder input = new StringBuilder();
            String line = file.readLine();
            while (line != null) {
                if(line.contains(patternMatch)) {
                    break;
                }
                input.append(line);
                input.append('\n');
                line = file.readLine();
            }
            input.append(newLine);
            input.append(line);
            file.close();
            // write over the file
            FileOutputStream fileOut = new FileOutputStream(fileName);
            fileOut.write(input.toString().getBytes());
            fileOut.close();

        } catch (Exception e) {
            log.warn("There was a problem reading/writing to the file: " + e.getMessage());
        }
    }

}
