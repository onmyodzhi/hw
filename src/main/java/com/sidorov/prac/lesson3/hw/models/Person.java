package com.sidorov.prac.lesson3.hw.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Person {

    Long id;

    String name;

    Integer age;

    Boolean active;

    Department department;
}
