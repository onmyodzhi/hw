package com.geekbrains.homeworks.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class TimeSheet {
    Long id;
    Long projectId;
    Integer minutes;
    LocalDate createdAt;
}
