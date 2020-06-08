package org.pensatocode.simplicity.generator;

import lombok.extern.log4j.Log4j2;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.components.Starter;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.writers.*;
import org.pensatocode.simplicity.generator.writers.starter.*;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class FileSourceStarter {

    private final Starter starterProps;
    private final DirectoryService dirService;
    private final VelocityEngine velocityEngine;

    private List<FileSourceWriter> fileSourceWriters;

    public FileSourceStarter(Starter starterProps, DirectoryService dirService, VelocityEngine velocityEngine) {
        this.starterProps = starterProps;
        this.dirService = dirService;
        this.velocityEngine = velocityEngine;
    }

    public boolean generateProject() throws GeneratorConfigurationException {
        for (FileSourceWriter writer: getFileSourceWriters()) {
            if(!writer.generateFileSource()) {
                // stop processing if something went wrong
                log.warn(String.format("File source using '%s' could not be generated", writer.getVmFileName()));
                return false;
            }
        }
        return true;
    }

    private List<FileSourceWriter> getFileSourceWriters() {
        if (fileSourceWriters == null) {
            fileSourceWriters = new ArrayList<>();
            fileSourceWriters.add(new SettingsGradleWriter(velocityEngine, dirService, starterProps));
            fileSourceWriters.add(new BuildGradleWriter(velocityEngine, dirService, starterProps));
            fileSourceWriters.add(new ApplicationClassWriter(velocityEngine, dirService, starterProps));
            fileSourceWriters.add(new ServletInitClassWriter(velocityEngine, dirService, starterProps));
            fileSourceWriters.add(new DbConfigClassWriter(velocityEngine, dirService, starterProps));
            fileSourceWriters.add(new AppTestClassWriter(velocityEngine, dirService, starterProps));
        }
        return fileSourceWriters;
    }
}
