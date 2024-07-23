package com.geekbrains.homeworks.model.dtos;

import lombok.Data;

import java.io.Serializable;


@Data
public class EmployeeDto implements Serializable {
    Long id;
    String name;
}