package com.robottx.springtodoapp.application.util;

import java.util.Arrays;

public class AppUtils {
    public static boolean doesClassContainField(Class<?> objClass, String fieldName) {
        return Arrays.stream(objClass.getDeclaredFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }
}
