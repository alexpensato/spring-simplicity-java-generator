package org.pensatocode.simplicity.generator.writers;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

public interface JavaSourceWriter {
    boolean generateSourceCode(ClassOrInterfaceDeclaration entity, VariableDeclarator id);
}
