package org.pensatocode.simplicity.generator.util;

import java.io.File;

public final class PathUtil {

    private PathUtil() {
        // Util
    }

    // Separators
    private static final char DOT = '.';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char LINUX_SEPARATOR = '/';

    public static String fixPath(String str) {
        if (str == null) {
            return File.separator;
        }
        String tmpStr = str.replace(loadWrongSeparator(), File.separatorChar);
        tmpStr = tmpStr.replace(DOT, File.separatorChar);
        if (tmpStr.charAt(str.length()-1) == File.separatorChar) {
            return tmpStr;
        }
        return tmpStr + File.separator;
    }

    public static char loadWrongSeparator() {
        if (File.separatorChar == WINDOWS_SEPARATOR) {
            return LINUX_SEPARATOR;
        } else {
            return WINDOWS_SEPARATOR;
        }
    }
}
