package org.pensatocode.simplicity.generator.services;

import com.github.javaparser.ast.type.Type;
import org.pensatocode.simplicity.generator.model.MapperVariable;
import org.pensatocode.simplicity.generator.model.SchemaType;

public interface Platform {
    SchemaType getSchemaType(Type type);
    boolean isSequenceNecessary();
    boolean areGrantsNecessary();
    void appendPrimaryKey(String tableName, MapperVariable id, StringBuilder sb);
}
