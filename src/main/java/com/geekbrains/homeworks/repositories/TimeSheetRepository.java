package com.geekbrains.homeworks.repositories;

import com.geekbrains.homeworks.model.TimeSheet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeSheetRepository {
    static Long sequence = 1L;
    final List<TimeSheet> timeSheets = new ArrayList<>();

    public Optional<TimeSheet> getById(Long id) {
        return timeSheets.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    public List<TimeSheet> getAll() {
        return List.copyOf(timeSheets);
    }

    public List<TimeSheet> getTimeSheetsByProjectId(Long projectId) {
        return timeSheets.stream()
                .filter(e -> e.getProjectId().equals(projectId))
                .toList();
    }

    public List<TimeSheet> getTimeSheetsByCreatedAtIsAfter(LocalDate date) {
        return timeSheets.stream()
                .filter(e -> e.getCreatedAt().isAfter(date))
                .toList();
    }

    public TimeSheet save(TimeSheet timeSheet) {
        timeSheet.setId(sequence++);
        timeSheet.setCreatedAt(LocalDate.now());
        timeSheets.add(timeSheet);
        return timeSheet;
    }

    public void deleteById(Long id) {
        timeSheets.removeIf(x -> x.getId().equals(id));
    }
}
