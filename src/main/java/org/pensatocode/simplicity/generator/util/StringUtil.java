package org.pensatocode.simplicity.generator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

    public final static String ALL = "*";

    private StringUtil() {
        // Util
    }

    /*
        Static methods
     */

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    public static boolean isNotEmpty(Object str) {
        return (str != null && !"".equals(str));
    }

    public static String decapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String convertToCamelCase(String input) {
        return convertToCamelCase(input, false);
    }

    public static String convertToCamelCase(String input, boolean capitalize) {
        final Pattern pattern = Pattern.compile("(_)([a-z])");
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(2).toUpperCase());
        }
        matcher.appendTail(sb);
        if (capitalize) {
            return capitalize(sb.toString());
        }
        return sb.toString();
    }

    public static String convertToSnakeCase(String input) {
        final Pattern pattern = Pattern.compile("(.)(\\p{Upper})");
        final String replacementPattern = "$1_$2";
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll(replacementPattern).toLowerCase();
    }
}
