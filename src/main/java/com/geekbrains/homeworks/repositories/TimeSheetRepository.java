package com.geekbrains.homeworks.repositories;

import com.geekbrains.homeworks.model.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
    List<TimeSheet> findAllByProjectId(Long projectId);
    List<TimeSheet> findAllByCreatedAtAfter(LocalDate createdAt);
    List<TimeSheet> findAllByEmployeeId(Long employeeId);
}
