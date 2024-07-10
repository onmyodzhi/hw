package com.geekbrains.homeworks.services;

import com.geekbrains.homeworks.model.Project;
import com.geekbrains.homeworks.model.TimeSheet;
import com.geekbrains.homeworks.repositories.TimeSheetRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TimesheetService {

    final TimeSheetRepository timesheetRepository;
    ProjectService projectService;

    public Optional<TimeSheet> getTimeSheetById(Long id) {
        return timesheetRepository.getById(id);
    }

    public List<TimeSheet> getAllTimeSheets() {
        return timesheetRepository.getAll();
    }

    public List<TimeSheet> getTimeSheetsByProjectId(Long projectId) {
        return timesheetRepository.getTimeSheetsByProjectId(projectId);
    }

    public List<TimeSheet> getTimeSheetsByCreatedAtIsAfter(LocalDate date) {
        return timesheetRepository.getTimeSheetsByCreatedAtIsAfter(date);
    }

    public Optional<TimeSheet> saveTimeSheet(TimeSheet timeSheet) {
        Project project = projectService.getProjectById(timeSheet.getProjectId()).orElse(null);

        if (project == null) {
            return Optional.empty();
        }

        return Optional.of(timesheetRepository.save(timeSheet));
    }

    public void deleteTimeSheet(Long id) {
        timesheetRepository.deleteById(id);
    }
}
