package org.pensatocode.simplicity.generator.writers.generator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.components.Config;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.components.properties.ConfigProperties;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.StringUtil;
import org.pensatocode.simplicity.generator.util.VelocityUtil;
import org.pensatocode.simplicity.generator.writers.JavaSourceWriter;

import java.io.File;

@Log4j2
public class RepositoryWriter implements JavaSourceWriter {

    private final VelocityEngine velocityEngine;
    private final DirectoryService dirService;
    private final Packages packages;
    private final Config config;

    public RepositoryWriter(VelocityEngine velocityEngine, DirectoryService dirService, Packages packages) {
        this.velocityEngine = velocityEngine;
        this.dirService = dirService;
        this.packages = packages;
        this.config = ConfigProperties.get();
    }

    public boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id) {
        // repository interface
        String repositoryName = entity.getNameAsString() + GeneratorUtil.REPOSITORY_SUFFIX;
        String fileAbsolutePath = dirService.getRepositoriesDir().getAbsolutePath()
                + File.separator
                + repositoryName
                + GeneratorUtil.JAVA_EXTENSION;
        File sourceFile = new File(fileAbsolutePath);
        boolean regenerate = config.getRegenerateRepositories().contains(entity.getNameAsString()) ||
                config.getRegenerateRepositories().contains(StringUtil.ALL);
        if (sourceFile.exists() && !regenerate) {
            // do nothing
            return true;
        }
        // create the template
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("packageName", packages.getRepositoriesPackage());
        velocityContext.put("qualifiedEntityName", entity.getFullyQualifiedName().orElse(""));
        velocityContext.put("entityName", entity.getNameAsString());
        velocityContext.put("repositoryName", repositoryName);
        velocityContext.put("idType", id.getType());
        Template template = velocityEngine.getTemplate("repository-interface.vm");
        // write the class
        return VelocityUtil.writeFile(fileAbsolutePath, template, velocityContext);
    }

}
