package org.pensatocode.simplicity.generator.writers;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.services.DirectoryService;
import org.pensatocode.simplicity.generator.services.JavaClassService;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.StringUtil;
import org.pensatocode.simplicity.generator.util.VelocityUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Log4j2
public class MapperWriter implements JavaSourceWriter {

    private final VelocityEngine velocityEngine;
    private final DirectoryService dirService;
    private final JavaClassService javaService;
    private final Packages packages;

    public MapperWriter(VelocityEngine velocityEngine, DirectoryService dirService, JavaClassService javaClassService, Packages packages) {
        this.velocityEngine = velocityEngine;
        this.dirService = dirService;
        this.javaService = javaClassService;
        this.packages = packages;
    }

    public boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id) {
        // mapper
        String mapperName = entity.getNameAsString() + GeneratorUtil.MAPPER_SUFFIX;
        String fileAbsolutePath = dirService.getMappersDir().getAbsolutePath()
                + File.separator
                + mapperName
                + GeneratorUtil.JAVA_EXTENSION;
        // create the template
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("packageName", packages.getMappersPackage());
        velocityContext.put("qualifiedEntityName", entity.getFullyQualifiedName().orElse(""));
        velocityContext.put("mapperName", mapperName);
        velocityContext.put("entityName", entity.getNameAsString());
        velocityContext.put("variables", javaService.listMapperVariables(entity));
        velocityContext.put("newline", "\n");
        Template template = velocityEngine.getTemplate("mapper-class.vm");
        // write the class
        return VelocityUtil.writeFile(fileAbsolutePath, template, velocityContext);
    }

}
