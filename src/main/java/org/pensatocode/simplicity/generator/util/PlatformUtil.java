package org.pensatocode.simplicity.generator.util;

import com.github.javaparser.ast.type.Type;
import lombok.NonNull;
import org.pensatocode.simplicity.generator.model.SchemaType;
import org.pensatocode.simplicity.generator.model.UserDefinedSchemaType;
import org.pensatocode.simplicity.generator.services.Platform;
import org.pensatocode.simplicity.generator.services.impl.DefaultPlatform;
import org.pensatocode.simplicity.generator.services.impl.PostgresPlatform;

public final class PlatformUtil {

    private PlatformUtil() {
        // Util
    }

    public static SchemaType convertFromJavaParserType(@NonNull Type type, Platform platform) {
        SchemaType result = platform.getSchemaType(type);
        if (result != null) {
            return result;
        }
        return new UserDefinedSchemaType(type.asString());
    }

    public static Platform createPlatformUsing(String platformName) {
        if ("postgres".equals(platformName)) {
            return new PostgresPlatform();
        }
        return new DefaultPlatform();
    }
}
