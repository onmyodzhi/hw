package com.sidorov.prac.hw2.lesson2.anno.lib;

import java.lang.reflect.Constructor;

public class ObjectCreator {

  public static <T> T createObj(Class<T> tClass) {
    try {
      Constructor<T> constructor = tClass.getDeclaredConstructor();

      T obj = constructor.newInstance();
      RandomDateAnnotationProcessor.randomDateAnnotationProcessor(obj);
      return obj;
    } catch (Exception e) {
      System.err.println("ниче не получилось: " + e.getMessage());
      return null; // throw new IllegalStateException
    }
  }

}
