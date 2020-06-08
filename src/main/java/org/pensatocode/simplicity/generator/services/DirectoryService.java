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

    public void checkProjectDir() throws GeneratorConfigurationException {
        if(getProjectDir().exists()) {
            log.warn("Project directory already exists. Cannot start a project over it.");
            throw new GeneratorConfigurationException(
                    "Project directory already exists. Cannot start a project over it.");
        }
    }

    public boolean createStarterDirectories() throws GeneratorConfigurationException {
        if (log.isDebugEnabled()) {
            log.debug("Simplicity Starter is creating directories...");
        }
        return createDirectories();
    }

    public boolean createGeneratorDirectories() throws GeneratorConfigurationException {
        if (log.isDebugEnabled()) {
            log.debug("Simplicity Generator is creating directories...");
        }
        if(!getOriginDirectory().exists()) {
            log.warn("Models directory does not exist. Cannot generate anything without a model.");
            return false;
        }
        return createDirectories();
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

    public File getProjectDir() {
        return getDirectory(GeneratorUtil.PROJECT_KEY, paths.getProjectPath());
    }

    public File getRootPackageDir() {
        return getDirectory(GeneratorUtil.ROOT_PACKAGE_KEY, paths.getRootPackagePath());
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

    public File getRestControllersDir() {
        return getDirectory(GeneratorUtil.REST_CONTROLLERS_KEY, paths.getRestControllersPath());
    }

    public File getMvcControllersDir() {
        return getDirectory(GeneratorUtil.MVC_CONTROLLERS_KEY, paths.getMvcControllersPath());
    }

    public File getResourcesDir() {
        return getDirectory(GeneratorUtil.RESOURCES_KEY, paths.getResourcesPath());
    }

    public File getTestJavaSourceDir() {
        return getDirectory(GeneratorUtil.TEST_JAVA_SOURCE_KEY, paths.getTestJavaSourcePath());
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

    private boolean createDirectories() throws GeneratorConfigurationException {
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

    public boolean createDir(String pathDir) {
        File dir = new File(pathDir);
        if(!dir.exists()) {
            if (!dir.mkdirs()) {
                log.warn(String.format("Generator was unable to create %s directory", dir.getAbsolutePath()));
                return false;
            }
        }
        return true;
    }
}
