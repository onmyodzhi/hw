package com.sidorov.prac.hw2.lesson2.anno.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomDate {
    long min() default 1704067200000L;
    long max() default 1735689600000L;
}
