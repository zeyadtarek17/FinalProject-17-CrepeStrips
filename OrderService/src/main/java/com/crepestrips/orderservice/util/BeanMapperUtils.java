package com.crepestrips.orderservice.util;

import java.lang.reflect.Field;

/**
 * Utility to copy matching fields from one object to another using reflection.
 * Only fields with the same name and type will be copied.
 */
public class BeanMapperUtils {

    private BeanMapperUtils() {}

    public static <S, T> void copyFields(S source, T target) {
        Field[] sourceFields = source.getClass().getDeclaredFields();

        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            try {
                Field targetField = target.getClass().getDeclaredField(sourceField.getName());
                targetField.setAccessible(true);
                if (targetField.getType().equals(sourceField.getType())) {
                    targetField.set(target, sourceField.get(source));
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
    }
}
