package org.pensatocode.simplicity.generator.util;

import lombok.extern.log4j.Log4j2;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Log4j2
public final class VelocityUtil {

    private VelocityUtil() {
        // Util
    }

    /*
        Static methods
     */

    public static VelocityEngine getVelocityEngine() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "./src/main/resources/templates");
        velocityEngine.init();
        return velocityEngine;
    }

    public static boolean writeFile(String fileAbsolutePath, Template template, VelocityContext velocityContext) {
        try {
            Writer fileWriter = new FileWriter(fileAbsolutePath);
            template.merge(velocityContext, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            log.warn(String.format("Unable to create file %s. Error: %s",
                    fileAbsolutePath,
                    e.getMessage()));
            return false;
        }
        return true;
    }

}
