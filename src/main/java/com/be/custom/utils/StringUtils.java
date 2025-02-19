package com.be.custom.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class StringUtils extends org.springframework.util.StringUtils {

    public static boolean isValidStrWithMaxLength(String str, int maxLength) {
        if (str == null) {
            return false;
        }
        return str.length() <= maxLength;
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNumberOrNull(String str) {
        return isNull(str) || isNumber(str);
    }

    public static boolean isNumberOrEmpty(String str) {
        return isEmpty(str) || isNumber(str);
    }

    public static boolean isNull(String str) {
        return str == null || "null".equals(str);
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    public static String genUrlUUIDFromName(String name) {
        return UUID.randomUUID() + name.replaceAll("\\s+", "-");
    }

}


