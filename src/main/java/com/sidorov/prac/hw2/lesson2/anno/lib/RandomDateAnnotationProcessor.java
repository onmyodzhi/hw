package com.sidorov.prac.hw2.lesson2.anno.lib;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class RandomDateAnnotationProcessor {
    public static void randomDateAnnotationProcessor(Object object) {
        java.util.Random rand = new java.util.Random();

        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(RandomDate.class) &&
                    (field.getType().isAssignableFrom(Date.class) ||
                            field.getType().isAssignableFrom(LocalDate.class) ||
                            field.getType().isAssignableFrom(LocalDateTime.class))){
                RandomDate annotation = field.getAnnotation(RandomDate.class);
                long max = annotation.max();
                long min = annotation.min();
                try {
                    field.setAccessible(true);
                    if (max > min) {
                        long randomValue = min + (long) (rand.nextDouble() * (max - min));

                        if (field.getType().isAssignableFrom(Date.class)) {
                            field.set(object, new Date(randomValue));
                        } else if (field.getType().isAssignableFrom(LocalDate.class)) {
                            field.set(object, LocalDate.ofEpochDay(randomValue / (24 * 60 * 60 * 1000)));
                        } else if (field.getType().isAssignableFrom(LocalDateTime.class)) {
                            field.set(object, LocalDateTime.ofEpochSecond(randomValue / 1000, 0, java.time.ZoneOffset.UTC));
                        }
                    } else {
                        throw new IllegalArgumentException("max must be greater than min");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
