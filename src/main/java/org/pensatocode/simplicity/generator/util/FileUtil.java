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
}
