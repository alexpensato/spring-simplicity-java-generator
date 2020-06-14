package org.pensatocode.simplicity.generator.services;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pensatocode.simplicity.generator.components.Packages;
import org.pensatocode.simplicity.generator.components.Paths;
import org.pensatocode.simplicity.generator.exceptions.GeneratorConfigurationException;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.model.SchemaType;
import org.pensatocode.simplicity.generator.util.ComponentBinder;
import org.pensatocode.simplicity.generator.util.GeneratorUtil;
import org.pensatocode.simplicity.generator.util.PlatformUtil;
import org.pensatocode.simplicity.generator.util.StringUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;

@Log4j2
public enum JavaClassService {

    SINGLETON;

    private final DirectoryService directoryService;

    private final Map<String, String> COMMON_TYPES = Map.of(
            "BigDecimal", "java.math.BigDecimal",
            "LocalDate","java.time.LocalDate",
            "LocalDateTime","java.time.LocalDateTime",
            "LocalTime","java.time.LocalTime",
            "Date","java.util.Date"
    );

    JavaClassService() {
        directoryService = DirectoryService.SINGLETON;
    }

    public List<ClassOrInterfaceDeclaration> getEntities() throws GeneratorConfigurationException {
        File modelsDir = directoryService.getOriginDirectory();
        return this.getEntities(modelsDir, true);
    }

    public List<ClassOrInterfaceDeclaration> getEntities(File modelsDir, boolean checkForEntityAnnotation) {
        return listJavaClasses(modelsDir, checkForEntityAnnotation);
    }

    private List<ClassOrInterfaceDeclaration> listJavaClasses(File projectDir, boolean checkForEntityAnnotation) {
        List<File> javaFiles = this.listJavaFiles(projectDir);
        List<ClassOrInterfaceDeclaration> javaClasses = new ArrayList<>();
        for (File file: Objects.requireNonNull(javaFiles)) {
            Optional<ClassOrInterfaceDeclaration> javaClass = this.extractJavaClass(file);
            if(javaClass.isEmpty()) {
                log.info(String.format("Java class %s is empty", file.getName()));
                continue;
            }
            if (checkForEntityAnnotation && !javaClass.get().isAnnotationPresent(Entity.class)) {
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

    public List<MapperVariable> listMapperVariables(ClassOrInterfaceDeclaration entity, Platform platform) {
        List<MapperVariable> mapperVariables = new ArrayList<>();
        for(VariableDeclarator variable: listVariables(entity)) {
            SchemaType schemaType = PlatformUtil.convertFromJavaParserType(variable.getType(), platform);
            String name = variable.getNameAsString();
            String capitalizedName = StringUtil.capitalize(name);
            String schemaName = StringUtil.convertToSnakeCase(name);
            mapperVariables.add(new MapperVariable(name, capitalizedName, schemaName, schemaType));
        }
        return mapperVariables;
    }

    public void createEntitySource(ClassOrInterfaceDeclaration origin) {
        // Create class source code
        Packages packages = ComponentBinder.getPackages();
        CompilationUnit compilationUnit = new CompilationUnit(packages.getModelsPackage());
        ClassOrInterfaceDeclaration newEntity = compilationUnit
                .addClass(origin.getNameAsString())
                .setPublic(true);
        // annotations
        newEntity.addAnnotation(Entity.class);
        newEntity.addAnnotation(Data.class);
        newEntity.addAnnotation(NoArgsConstructor.class);
        newEntity.addAnnotation(AllArgsConstructor.class);
        // fields
        for(FieldDeclaration field: origin.getFields()) {
            VariableDeclarator variable = field.getVariable(0);
            FieldDeclaration newField = newEntity.addPrivateField(variable.getType(), variable.getNameAsString());
            if (field.isAnnotationPresent(Id.class)) {
                newField.addAnnotation(Id.class);
            }
            String typeNeedsImportDeclaration = COMMON_TYPES.get(variable.getTypeAsString());
            if (typeNeedsImportDeclaration != null) {
                compilationUnit.addImport(typeNeedsImportDeclaration);
            }
        }
        // source code
        Paths paths = ComponentBinder.getPaths();
        String entityAbsolutePath = paths.getModelPath() + origin.getNameAsString() + GeneratorUtil.JAVA_EXTENSION;
        log.info(String.format("Destiny entities directory is %s", entityAbsolutePath));
        Path entityPath = Path.of(entityAbsolutePath);
        compilationUnit.setStorage(entityPath);
        compilationUnit.getStorage().orElseThrow().save();
    }

}
