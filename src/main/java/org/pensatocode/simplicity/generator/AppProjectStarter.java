package org.pensatocode.simplicity.generator;

import lombok.extern.log4j.Log4j2;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.util.ComponentBinder;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.ServiceBinder;
import org.pensatocode.simplicity.generator.util.VelocityUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Log4j2
public class AppProjectStarter {

    public static void main(String[] args) {
        try {
            // Check initial conditions
            DirectoryService dirService = ServiceBinder.getDirectoryService();
            dirService.checkProjectDir();
            // Extract starter project
            copyAndExtractProjectFiles(ComponentBinder.getPaths().getProjectPath());
            // Create dirs
            if (!dirService.createStarterDirectories()) {
                throw new GeneratorConfigurationException("There was a problem creating directories.");
            }
            // Create all files
            VelocityEngine velocityEngine = VelocityUtil.getVelocityEngine();
            FileSourceStarter fileSourceStarter = new FileSourceStarter(dirService, velocityEngine);
            if (!fileSourceStarter.generateProject()) {
                throw new GeneratorConfigurationException("There was a problem generating project.");
            }
            log.info("Success!");

        } catch (GeneratorConfigurationException e) {
            e.printStackTrace();
            log.error("FAIL: " + e.getMessage());
        }
    }

    private static void copyAndExtractProjectFiles(String newProjectDir) throws GeneratorConfigurationException {
        try {
            // Generator directory
            String currentDir = System.getProperty("user.dir");
            log.info(String.format("Current project directory is %s", currentDir));
            // Copy zip file
            String zipsDir = currentDir + File.separator + GeneratorUtil.ZIPS_DIR_NAME;
            Path zipSrc = Paths.get(zipsDir);
            Path zipDest = Paths.get(newProjectDir);
            Files.copy(zipSrc, zipDest, StandardCopyOption.REPLACE_EXISTING);
            // Unzip
            unzipFiles(zipsDir, zipDest);
            // Copy Simplicity library JAR
            String jarFile = zipsDir + File.separator + GeneratorUtil.LIBS_DIR_NAME +
                    File.separator + GeneratorUtil.SIMPLICITY_JAR_FILE_NAME;
            String newJarFile = newProjectDir + File.separator + GeneratorUtil.LIBS_DIR_NAME +
                    File.separator + GeneratorUtil.SIMPLICITY_JAR_FILE_NAME;
            Path libSrc = Paths.get(jarFile);
            Path libDest = Paths.get(newJarFile);
            Files.copy(libSrc, libDest, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
            throw new GeneratorConfigurationException(
                    String.format("Error in copyAndExtractProjectFiles: %s", e.getMessage()));
        }
    }

    private static void unzipFiles(String zipsDir, Path outputPath) throws GeneratorConfigurationException {
        String zipFile = zipsDir + File.separator + GeneratorUtil.PROJECT_ZIP_FILE_NAME;
        try (var zf = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> zipEntries = zf.entries();
            zipEntries.asIterator().forEachRemaining(entry -> {
                try {
                    if (entry.isDirectory()) {
                        var dirToCreate = outputPath.resolve(entry.getName());
                        Files.createDirectories(dirToCreate);
                    } else {
                        var fileToCreate = outputPath.resolve(entry.getName());
                        Files.copy(zf.getInputStream(entry), fileToCreate);
                    }
                } catch(IOException ei) {
                    ei.printStackTrace();
                }
            });
        } catch(IOException e) {
            e.printStackTrace();
            throw new GeneratorConfigurationException(
                    String.format("Error in unzipFiles: %s", e.getMessage()));
        }
    }
}