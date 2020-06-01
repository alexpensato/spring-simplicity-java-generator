package org.pensatocode.simplicity.generator.writers;

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

import java.io.File;

@Log4j2
public class ControllerWriter implements JavaSourceWriter {

    private final VelocityEngine velocityEngine;
    private final DirectoryService dirService;
    private final Packages packages;
    private final Config config;

    public ControllerWriter(VelocityEngine velocityEngine, DirectoryService dirService, Packages packages) {
        this.velocityEngine = velocityEngine;
        this.dirService = dirService;
        this.packages = packages;
        this.config = ConfigProperties.get();
    }

    public boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id) {
        // repository implementation
        String controllerName = entity.getNameAsString() + GeneratorUtil.REST_CONTROLLER_SUFFIX;
        String fileAbsolutePath = dirService.getControllersDir().getAbsolutePath()
                + File.separator
                + controllerName
                + GeneratorUtil.JAVA_EXTENSION;
        File sourceFile = new File(fileAbsolutePath);
        boolean regenerate = config.getRegenerateControllers().contains(entity.getNameAsString()) ||
                config.getRegenerateControllers().contains(StringUtil.ALL);
        if (sourceFile.exists() && !regenerate) {
            // do nothing
            return true;
        }
        // repository interface
        String repositoryName = entity.getNameAsString() + GeneratorUtil.REPOSITORY_SUFFIX;
        String qualifiedRepositoryName = packages.getRepositoriesPackage() + GeneratorUtil.DOT + repositoryName;
        // repository-based name
        String endpointMapping = StringUtil.decapitalize(entity.getNameAsString());
        // entity-based name
        String beanName = StringUtil.decapitalize(repositoryName);
        // create the template
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("packageName", packages.getControllersPackage());
        velocityContext.put("qualifiedEntityName", entity.getFullyQualifiedName().orElse(""));
        velocityContext.put("qualifiedRepositoryName", qualifiedRepositoryName);
        velocityContext.put("endpointMapping", endpointMapping);
        velocityContext.put("controllerName", controllerName);
        velocityContext.put("repositoryName", repositoryName);
        velocityContext.put("beanName", beanName);
        velocityContext.put("entityName", entity.getNameAsString());
        velocityContext.put("idName", id.getName());
        velocityContext.put("idType", id.getType());
        velocityContext.put("apiContext", config.getApiContext());
        Template template = velocityEngine.getTemplate("controller-class.vm");
        // write the class
        return VelocityUtil.writeFile(fileAbsolutePath, template, velocityContext);
    }

}
