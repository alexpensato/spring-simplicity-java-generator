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
public class RepositoryImplWriter implements JavaSourceWriter {

    private final VelocityEngine velocityEngine;
    private final DirectoryService dirService;
    private final Packages packages;
    private final Config config;

    public RepositoryImplWriter(VelocityEngine velocityEngine, DirectoryService dirService, Packages packages) {
        this.velocityEngine = velocityEngine;
        this.dirService = dirService;
        this.packages = packages;
        this.config = ConfigProperties.get();
    }

    public boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id) {
        // concrete repository
        String repositoryImplName = entity.getNameAsString() + GeneratorUtil.REPOSITORY_IMPL_SUFFIX;
        String fileAbsolutePath = dirService.getRepositoryImplementationDir().getAbsolutePath()
                + File.separator
                + repositoryImplName
                + GeneratorUtil.JAVA_EXTENSION;
        File sourceFile = new File(fileAbsolutePath);
        boolean regenerate = config.getRegenerateRepositories().contains(entity.getNameAsString()) ||
                config.getRegenerateRepositories().contains(StringUtil.ALL);
        if (sourceFile.exists() && !regenerate) {
            // do nothing
            return true;
        }
        // mapper
        String mapperName = entity.getNameAsString() + GeneratorUtil.MAPPER_SUFFIX;
        String mapperQualifiedName = packages.getMappersPackage() + GeneratorUtil.DOT + mapperName;
        // repository interface
        String repositoryName = entity.getNameAsString() + GeneratorUtil.REPOSITORY_SUFFIX;
        String repositoryQualifiedName = packages.getRepositoriesPackage() + GeneratorUtil.DOT + repositoryName;
        // repository-based name
        String beanName = StringUtil.decapitalize(repositoryName);
        // entity-based names
        String tableName = StringUtil.convertToSnakeCase(entity.getNameAsString());
        String entityClass = entity.getNameAsString() + GeneratorUtil.CLASS_EXTENSION;
        // create the template
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("packageName", packages.getRepoImplementationsPackage());
        velocityContext.put("qualifiedEntityName", entity.getFullyQualifiedName().orElse(""));
        velocityContext.put("qualifiedMapperName", mapperQualifiedName);
        velocityContext.put("qualifiedInterfaceName", repositoryQualifiedName);
        velocityContext.put("repositoryBeanName", beanName);
        velocityContext.put("concreteRepositoryName", repositoryImplName);
        velocityContext.put("interfaceName", repositoryName);
        velocityContext.put("mapperName", mapperName);
        velocityContext.put("entityName", entity.getNameAsString());
        velocityContext.put("tableName", tableName);
        velocityContext.put("entityClass", entityClass);
        velocityContext.put("idName", id.getName());
        velocityContext.put("idType", id.getType());
        Template template = velocityEngine.getTemplate("repository-class.vm");
        // write the class
        return VelocityUtil.writeFile(fileAbsolutePath, template, velocityContext);
    }

}
