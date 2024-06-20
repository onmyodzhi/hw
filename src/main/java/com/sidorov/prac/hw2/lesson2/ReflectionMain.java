package com.sidorov.prac.hw2.lesson2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class ReflectionMain {

  public static void main(String[] args) throws Exception {
    // Reflection (рефлексия)

    Person unnamed = new Person();
    Person person = new Person("personName");

    System.out.println(unnamed);
    System.out.println(person);

    Class<Person> personClass = Person.class;
    Constructor<Person> constructor = personClass.getConstructor();
    Person viaReflectPerson = constructor.newInstance();
    System.out.println(viaReflectPerson);

    System.out.println(unnamed.getName());
    Method getName = Person.class.getMethod("getName");
    System.out.println(getName.invoke(unnamed)); // unnamed
    System.out.println(getName.invoke(person)); // personName

    Method setAge = Person.class.getMethod("setAge", int.class);
    setAge.invoke(unnamed, 10); // unnamed.setAge(10);
    System.out.println(unnamed);

    System.out.println("Counter = " + Person.getCounter());
    Method getCounter = Person.class.getMethod("getCounter");
    System.out.println("Counter (via reflect) = " + getCounter.invoke(null));

    Field name = Person.class.getDeclaredField("name");
    System.out.println("Name field = " + name.get(unnamed)); // unnamed

//    name.set(unnamed, "new name");
//    System.out.println("Name field = " + name.get(unnamed)); // new name

    Method declaredConstructor = ExtPerson.class.getMethod("getName");

    Class<Person> personClass1 = Person.class;
    Class<? super ExtPerson> superclass = ExtPerson.class.getSuperclass();

    System.out.println(personClass1);
    System.out.println(superclass);

//    ExtPerson extPerson = new ExtPerson();
//    extPerson.getName();

    Class<?>[] interfaces = ArrayList.class.getInterfaces();
    Arrays.stream(interfaces).forEach(System.out::println);


  }

  private static class ExtPerson extends Person {

    @Override
    public String getName() {
      return super.getName();
    }
  }

  private static class Person {

    private static long counter = 0L;

    private final String name;
    private int age;

    public Person() {
      this("unnamed");
    }

    public Person(String name) {
      this.name = name;
      counter++;
    }

    public static long getCounter() {
      return counter;
    }

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    @Override
    public String toString() {
      return "Person{" +
        "name='" + name + '\'' +
        ", age=" + age +
        '}';
    }
  }

}
