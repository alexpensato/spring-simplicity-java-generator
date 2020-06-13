package org.pensatocode.simplicity.generator.writers.generator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.components.Config;
import org.pensatocode.simplicity.generator.components.DatabaseConfig;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.services.Platform;
import org.pensatocode.simplicity.generator.util.*;
import org.pensatocode.simplicity.generator.writers.JavaSourceWriter;

import java.io.File;

@Log4j2
public class MapperWriter implements JavaSourceWriter {

    private final VelocityEngine velocityEngine;
    private final DirectoryService dirService;
    private final JavaClassService javaService;
    private final Packages packages;
    private final Config config;
    private final DatabaseConfig databaseConfig;

    public MapperWriter(VelocityEngine velocityEngine, DirectoryService dirService, JavaClassService javaClassService) {
        this.velocityEngine = velocityEngine;
        this.dirService = dirService;
        this.javaService = javaClassService;
        this.packages = ComponentBinder.getPackages();
        this.config = ComponentBinder.getConfig();
        this.databaseConfig = ComponentBinder.getDatabaseConfig();
    }

    public boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id) {
        // mapper
        String mapperName = entity.getNameAsString() + GeneratorUtil.MAPPER_SUFFIX;
        String fileAbsolutePath = dirService.getMappersDir().getAbsolutePath()
                + File.separator
                + mapperName
                + GeneratorUtil.JAVA_EXTENSION;
        File sourceFile = new File(fileAbsolutePath);
        boolean regenerate = config.getRegenerateMappers().contains(entity.getNameAsString()) ||
                config.getRegenerateMappers().contains(StringUtil.ALL);
        if (sourceFile.exists() && !regenerate) {
            // do nothing
            return true;
        }
        // platform
        Platform platform = PlatformUtil.createPlatformUsing(databaseConfig.getPlatformName());
        // create the template
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("packageName", packages.getMappersPackage());
        velocityContext.put("qualifiedEntityName", entity.getFullyQualifiedName().orElse(""));
        velocityContext.put("mapperName", mapperName);
        velocityContext.put("entityName", entity.getNameAsString());
        velocityContext.put("variables", javaService.listMapperVariables(entity, platform));
        velocityContext.put("newline", "\n");
        Template template = velocityEngine.getTemplate("mapper-class.vm");
        // write the class
        return VelocityUtil.writeFile(fileAbsolutePath, template, velocityContext);
    }

}
