package org.pensatocode.simplicity.generator.services;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.model.SchemaType;
import org.pensatocode.simplicity.generator.model.SqlSchemaType;
import org.pensatocode.simplicity.generator.model.UserDefinedSchemaType;
import org.pensatocode.simplicity.generator.util.StringUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Log4j2
public enum JavaClassService {

    SINGLETON;

    private final DirectoryService directoryService;

    JavaClassService() {
        directoryService = DirectoryService.SINGLETON;
    }

    public List<ClassOrInterfaceDeclaration> getEntities() throws GeneratorConfigurationException {
        File modelsDir = directoryService.getOriginDirectory();
        return listJavaClasses(modelsDir, true);
    }

    public List<ClassOrInterfaceDeclaration> getJavaClasses() throws GeneratorConfigurationException {
        File modelsDir = directoryService.getOriginDirectory();
        return listJavaClasses(modelsDir, false);
    }

    private List<ClassOrInterfaceDeclaration> listJavaClasses(File projectDir, boolean checkForEntity) {
        List<File> javaFiles = this.listJavaFiles(projectDir);
        List<ClassOrInterfaceDeclaration> javaClasses = new ArrayList<>();
        for (File file: Objects.requireNonNull(javaFiles)) {
            Optional<ClassOrInterfaceDeclaration> javaClass = this.extractJavaClass(file);
            if(javaClass.isEmpty()) {
                log.info(String.format("Java class %s is empty", file.getName()));
                continue;
            }
            if (checkForEntity && !javaClass.get().isAnnotationPresent(Entity.class)) {
                log.info(String.format("Java class %s is not an Entity", file.getName()));
                continue;
            }
            javaClasses.add(javaClass.get());
        }
        return javaClasses;
    }

    private List<File> listJavaFiles(File projectDir) {
        if (projectDir == null || !projectDir.isDirectory() || projectDir.listFiles() == null) {
            return Collections.emptyList();
        }
        List<File> javaFiles = new ArrayList<>();
        for (File file: Objects.requireNonNull(projectDir.listFiles())) {
            if (!file.getPath().endsWith(".java")) {
                continue;
            }
            javaFiles.add(file);
        }
        return javaFiles;
    }

    private Optional<ClassOrInterfaceDeclaration> extractJavaClass(File javaFile) {
        if(javaFile == null) {
            return Optional.empty();
        }
        String javaClassName = javaFile.getName();
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(javaFile);
            return compilationUnit.getClassByName(javaClassName.replaceAll("(.java)$",""));
        } catch (FileNotFoundException | ParseProblemException e) {
            log.warn(String.format("Unable to extract JavaClass %s. Error: %s", javaClassName, e.getMessage() ));
            return Optional.empty();
        }
    }

    public VariableDeclarator extractId(ClassOrInterfaceDeclaration entity) {
        for(FieldDeclaration field: entity.getFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getVariable(0);
            }
        }
        log.warn(String.format("Id not found in %s entity", entity.getNameAsString()));
        return null;
    }

    public List<VariableDeclarator> listVariables(ClassOrInterfaceDeclaration entity) {
        List<VariableDeclarator> variables = new ArrayList<>();
        for(FieldDeclaration field: entity.getFields()) {
            variables.add(field.getVariable(0));
        }
        return variables;
    }

    public List<MapperVariable> listMapperVariables(ClassOrInterfaceDeclaration entity) {
        List<MapperVariable> mapperVariables = new ArrayList<>();
        for(VariableDeclarator variable: listVariables(entity)) {
            SchemaType schemaType = SqlSchemaType.convertFromJavaParserType(variable.getType());
            if (schemaType == null) {
                schemaType = new UserDefinedSchemaType(variable.getTypeAsString());
            }
            String name = variable.getNameAsString();
            mapperVariables.add(new MapperVariable(name, StringUtil.capitalize(name), schemaType));
        }
        return mapperVariables;
    }

}
