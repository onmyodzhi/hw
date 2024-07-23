package com.geekbrains.homeworks.services;

import com.geekbrains.homeworks.model.TimeSheet;
import com.geekbrains.homeworks.model.dtos.TimeSheetDto;
import com.geekbrains.homeworks.model.mapers.TimeSheetMapper;
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

    public Optional<TimeSheetDto> getTimeSheetById(Long id) {
        return timesheetRepository.findById(id).map(TimeSheetMapper.INSTANCE::timeSheetToTimeSheetDto);
    }

    public List<TimeSheet> getAllTimeSheets() {
        return timesheetRepository.findAll();
    }

    public List<TimeSheet> getTimeSheetsByCreatedAtIsAfter(LocalDate date) {
        return timesheetRepository.findAllByCreatedAtAfter(date);
    }

    public List<TimeSheetDto> getAllTimeSheetsByEmployeeId(Long id) {
        List<TimeSheet> timeSheets = timesheetRepository.findAllByEmployeeId(id);

        return timeSheets.stream()
                .map(TimeSheetMapper.INSTANCE::timeSheetToTimeSheetDto)
                .toList();
    }

    public Optional<TimeSheetDto> saveTimeSheet(TimeSheetDto timeSheetDto) {
        return projectService.getProjectById(timeSheetDto.getProjectId())
                .map(project -> TimeSheetMapper.INSTANCE.timeSheetDtoToTimeSheet(timeSheetDto))
                .map(timesheetRepository::save)
                .map(TimeSheetMapper.INSTANCE::timeSheetToTimeSheetDto);
    }

    public void deleteTimeSheet(Long id) {
        timesheetRepository.deleteById(id);
    }
}
