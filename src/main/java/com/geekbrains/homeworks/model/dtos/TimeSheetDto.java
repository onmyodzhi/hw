package com.geekbrains.homeworks.model.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;


@Data
public class TimeSheetDto implements Serializable {
    Long id;
    Long projectId;
    Integer minutes;
    LocalDate createdAt;
}