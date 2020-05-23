package org.pensatocode.simplicity.generator.services;

import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.components.Paths;
import org.pensatocode.simplicity.generator.components.properties.PathProperties;

import java.io.File;

@Log4j2
public enum DirectoryService {

    SINGLETON;

    private final Paths paths;

    DirectoryService() {
        paths = PathProperties.SINGLETON;
    }

    public boolean createDirectories() throws GeneratorConfigurationException {
        if (log.isDebugEnabled()) {
            log.debug("Simplicity Generator is creating directories...");
        }
        if(!getOriginDirectory().exists()) {
            log.warn("Models directory does not exist");
        }
        for(String key: GeneratorUtil.getDestinyKeys()) {
            String path = paths.getDestinyPaths().get(key);
            File dir = new File(path);
            if(dir.exists()) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("%s directory already exists = %s", key, dir.getAbsolutePath()));
                }
                continue;
            }
            if (!dir.mkdirs()) {
                log.warn(String.format("Generator was unable to create %s directory", dir.getAbsolutePath()));
                return false;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Finished creating directories!");
        }
        return true;
    }

    public File getOriginDirectory() throws GeneratorConfigurationException {
        File dir = getDirectory(GeneratorUtil.ORIGIN_KEY, paths.getModelPath());
        if (!dir.isDirectory() || dir.listFiles() == null) {
            String message = "Origin directory is empty or not a directory. Fix it before proceeding.";
            log.warn(message);
            throw new GeneratorConfigurationException(message);
        }
        return dir;
    }

    public File getModelsDir() {
        return getDirectory(GeneratorUtil.MODELS_KEY, paths.getModelPath());
    }

    public File getRepositoriesDir()  {
        return getDirectory(GeneratorUtil.REPOSITORIES_KEY, paths.getRepositoriesPath());
    }

    public File getRepositoryImplementationDir() {
        return getDirectory(GeneratorUtil.REPOSITORY_IMPL_KEY, paths.getRepoImplementationsPath());
    }

    public File getMappersDir()  {
        return getDirectory(GeneratorUtil.MAPPERS_KEY, paths.getMappersPath());
    }

    public File getControllersDir() {
        return getDirectory(GeneratorUtil.CONTROLLERS_KEY, paths.getControllersPath());
    }

    public File getResourcesDir() {
        return getDirectory(GeneratorUtil.RESOURCES_KEY, paths.getResourcesPath());
    }

    public File getTestResourcesDir() {
        return getDirectory(GeneratorUtil.TEST_RESOURCES_KEY, paths.getTestResourcesPath());
    }

    public File getTestRepositoriesDir() {
        return getDirectory(GeneratorUtil.TEST_REPOSITORIES_KEY, paths.getTestRepositoriesPath());
    }

    private File getDirectory(String key, String directoryPath) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s path = %s", key, directoryPath));
        }
        return new File(directoryPath);
    }
}
