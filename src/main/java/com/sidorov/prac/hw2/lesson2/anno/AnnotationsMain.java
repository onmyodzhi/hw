package com.sidorov.prac.hw2.lesson2.anno;

import com.sidorov.prac.hw2.lesson2.anno.lib.ObjectCreator;
import com.sidorov.prac.hw2.lesson2.anno.lib.RandomDate;
import com.sidorov.prac.hw2.lesson2.anno.lib.RandomDateAnnotationProcessor;
import lombok.Data;

import java.util.Date;

public class AnnotationsMain {

  public static void main(String[] args) {
    //Person rndPerson = ObjectCreator.createObj(Person.class);
    //System.out.println("age1 = " + rndPerson.age1);
    //System.out.println("age2 = " + rndPerson.age2);

    // extPerson.isAssignableFrom(ExtPerson.class) // true
    // extPerson.isAssignableFrom(Person.class) // false
    // person.isAssignableFrom(ExtPerson.class) // true

//    Person p = new Person();
//    Person ep = new ExtPerson();
//
//    System.out.println(p.getClass().isAssignableFrom(Person.class)); // true
//    System.out.println(p.getClass().isAssignableFrom(ExtPerson.class)); // true
//
//    System.out.println(ep.getClass().isAssignableFrom(Person.class)); // false
//    System.out.println(ep.getClass().isAssignableFrom(ExtPerson.class)); // true


    Person person = ObjectCreator.createObj(Person.class);
    System.out.println(person.getTime());

    RandomDateAnnotationProcessor.randomDateAnnotationProcessor(person);
    System.out.println(person.getTime());

  }

  public static class ExtPerson extends Person {

  }

  @Data
  public static class Person {
    @RandomDate
    Date time;

    // рандомное число в диапазоне [0, 100)
    private int age1;

    // рандомное число в диапазоне [50, 51) => 50
    private int age2;

    private String age3;

  }
}
