package com.sidorov.prac.hw1;

import lombok.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;


@Getter
@Setter
public class Homework {
    public static void main(String[] args) {
        Department department1 = new Department("IT");
        Department department2 = new Department("HR");

        Person person1 = new Person("Alice", 30, 70000, department1);
        Person person2 = new Person("Bob", 25, 50000, department2);
        Person person3 = new Person("Charlie", 35, 60000, department1);
        Person person4 = new Person("Diana", 28, 55000, department2);

        List<Person> people = Arrays.asList(person1, person2, person3, person4);

        System.out.println("""
                /**
                 * Найти самого молодого сотрудника
                 */
                """);
        findMostYoungestPerson(people).ifPresent(System.out::println);

        System.out.println("""
                /**
                 * Найти департамент, в котором работает сотрудник с самой большой зарплатой
                 */
                """);
        findMostExpensiveDepartment(people).ifPresent(System.out::println);

        System.out.println("""
                /**
                 * Сгруппировать сотрудников по департаментам
                 */
                """);
        System.out.println(groupByDepartment(people));

        System.out.println("""
                /**
                 * Сгруппировать сотрудников по названиям департаментов
                 */
                """);
        System.out.println(groupByDepartmentName(people));

        System.out.println("""
                /**
                 * В каждом департаменте найти самого старшего сотрудника
                 */
                """);
        System.out.println(getDepartmentOldestPerson(people));

        System.out.println("""
                /**
                 * *Найти сотрудников с минимальными зарплатами в своем отделе
                 * (прим. можно реализовать в два запроса)
                 */
                """);
        System.out.println(cheapPersonsInDepartment(people));



    }

    @Getter
    @Setter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static class Department {
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static class Person {
        private String name;
        private int age;
        private double salary;
        private Department depart;
    }

    /**
     * Найти самого молодого сотрудника
     */
    static Optional<Person> findMostYoungestPerson(List<Person> people) {
        return people.stream()
                .min(Comparator.comparingInt(Person::getAge));
    }

    /**
     * Найти департамент, в котором работает сотрудник с самой большой зарплатой
     */
    static Optional<Department> findMostExpensiveDepartment(List<Person> people) {
        return people.stream().max(Comparator.comparingDouble(Person::getSalary)).map(Person::getDepart);
    }

    /**
     * Сгруппировать сотрудников по департаментам
     */
    static Map<Department, List<Person>> groupByDepartment(List<Person> people) {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getDepart));
    }
    /**
     * Сгруппировать сотрудников по названиям департаментов
     */
    static Map<String, List<Person>> groupByDepartmentName(List<Person> people) {
        return people.stream()
                .collect(Collectors.groupingBy(p -> p.getDepart().getName()));
    }

    /**
     * В каждом департаменте найти самого старшего сотрудника
     */
    static Map<String, Person> getDepartmentOldestPerson(List<Person> people) {
        return people.stream()
                .collect(Collectors.toMap(p -> p.getDepart().getName(),
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingInt(Person::getAge))));
    }

    /**
     * *Найти сотрудников с минимальными зарплатами в своем отделе
     * (прим. можно реализовать в два запроса)
     */
    static List<Person> cheapPersonsInDepartment(List<Person> people) {
        return people.stream()
                .collect(Collectors.toMap(Person::getDepart, Function.identity(),
                        BinaryOperator.minBy(Comparator.comparingDouble(Person::getSalary))))
                .values().stream()
                .toList();
    }
}
