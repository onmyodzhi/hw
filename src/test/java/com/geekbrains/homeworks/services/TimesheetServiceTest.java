package com.geekbrains.homeworks.services;

import com.geekbrains.homeworks.model.Employee;
import com.geekbrains.homeworks.model.TimeSheet;
import com.geekbrains.homeworks.model.dtos.ProjectDto;
import com.geekbrains.homeworks.model.dtos.TimeSheetDto;
import com.geekbrains.homeworks.model.mapers.TimeSheetMapper;
import com.geekbrains.homeworks.repositories.TimeSheetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimesheetServiceTest {
    @Mock
    TimeSheetRepository timeSheetRepository;

    @Mock
    ProjectService projectService;

    @InjectMocks
    TimesheetService timesheetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTimeSheetById() {
        TimeSheet timeSheet = new TimeSheet();
        timeSheet.setId(1L);
        timeSheet.setCreatedAt(LocalDate.now());
        timeSheet.setMinutes(120);
        timeSheet.setProjectId(1L);


        when(timeSheetRepository.findById(1L))
                .thenReturn(Optional.of(timeSheet));

        TimeSheetDto expectedTimeSheetDto = TimeSheetMapper.INSTANCE.timeSheetToTimeSheetDto(timeSheet);

        Optional<TimeSheetDto> result = timesheetService.getTimeSheetById(1L);
        assertTrue(result.isPresent());
        assertEquals(expectedTimeSheetDto, result.get());
    }

    @Test
    void getAllTimeSheets() {
        TimeSheet timeSheet = new TimeSheet();
        TimeSheet timeSheet1 = new TimeSheet();

        timeSheet.setProjectId(1L);
        timeSheet.setMinutes(120);
        timeSheet.setCreatedAt(LocalDate.now());
        timeSheet.setId(1L);
        timeSheet1.setProjectId(2L);
        timeSheet1.setMinutes(120);
        timeSheet1.setCreatedAt(LocalDate.now());
        timeSheet1.setId(2L);

        List<TimeSheet> mockTimeSheets = Arrays.asList(timeSheet, timeSheet1);

        when(timesheetService.getAllTimeSheets())
                .thenReturn(mockTimeSheets);

        List<TimeSheet> timeSheets = timesheetService.getAllTimeSheets();
        assertEquals(mockTimeSheets, timeSheets);
    }

    @Test
    void getTimeSheetsByCreatedAtIsAfter() {
        TimeSheet timeSheet = new TimeSheet();
        TimeSheet timeSheet1 = new TimeSheet();

        LocalDate currentDate = LocalDate.now();
        LocalDate dateForFilter = LocalDate.now().minusYears(10);

        timeSheet.setId(1L);
        timeSheet.setCreatedAt(dateForFilter);
        timeSheet.setMinutes(120);
        timeSheet.setProjectId(1L);
        timeSheet1.setId(2L);
        timeSheet1.setProjectId(2L);
        timeSheet1.setCreatedAt(currentDate);
        timeSheet1.setMinutes(130);

        List<TimeSheet> expectedTimesheets = List.of(timeSheet1);

        when(timeSheetRepository.findAllByCreatedAtAfter(dateForFilter))
                .thenReturn(expectedTimesheets);

        List<TimeSheet> result = timesheetService.getTimeSheetsByCreatedAtIsAfter(dateForFilter);
        assertEquals(expectedTimesheets, result);
    }

    @Test
    void getTimesheetsByProjectId() {
        TimeSheet timeSheet = new TimeSheet();
        TimeSheet timeSheet1 = new TimeSheet();

        timeSheet.setId(1L);
        timeSheet.setCreatedAt(LocalDate.now());
        timeSheet.setMinutes(120);
        timeSheet.setProjectId(1L);
        timeSheet.setEmployeeId(1L);
        timeSheet1.setId(2L);
        timeSheet1.setProjectId(2L);
        timeSheet1.setEmployeeId(2L);
        timeSheet1.setCreatedAt(LocalDate.now());
        timeSheet1.setMinutes(130);

        List<TimeSheet> expectedTimesheets = List.of(timeSheet);

        when(timeSheetRepository.findAllByProjectId(1L))
        .thenReturn(expectedTimesheets);

        List<TimeSheet> result = timeSheetRepository.findAllByProjectId(1L);

        assertEquals(expectedTimesheets, result);
    }

    @Test
    void getTimesheetsByEmployeeIdNegative() {
        TimeSheet timeSheet = new TimeSheet();
        TimeSheet timeSheet1 = new TimeSheet();

        timeSheet.setId(1L);
        timeSheet.setCreatedAt(LocalDate.now());
        timeSheet.setMinutes(120);
        timeSheet.setProjectId(1L);
        timeSheet1.setId(2L);
        timeSheet1.setProjectId(2L);
        timeSheet1.setCreatedAt(LocalDate.now());
        timeSheet1.setMinutes(130);

        List<TimeSheet> expectedTimesheets = new ArrayList<>();

        when(timeSheetRepository.findAllByEmployeeId(1L))
        .thenReturn(expectedTimesheets);

        List<TimeSheet> result = timeSheetRepository.findAllByEmployeeId(1L);

        assertEquals(expectedTimesheets, result);
    }

    @Test
    void saveTimeSheet() {
        TimeSheetDto timeSheetDto = new TimeSheetDto();
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Test Project");

        timeSheetDto.setId(1L);
        timeSheetDto.setCreatedAt(LocalDate.now());
        timeSheetDto.setMinutes(120);
        timeSheetDto.setProjectId(1L);

        TimeSheet timeSheetToSave = TimeSheetMapper.INSTANCE.timeSheetDtoToTimeSheet(timeSheetDto);
        timeSheetToSave.setId(1L);

        when(projectService.getProjectById(1L))
                .thenReturn(Optional.of(projectDto));
        when(timeSheetRepository.save(any(TimeSheet.class)))
                .thenReturn(timeSheetToSave);

        Optional<TimeSheetDto> result = timesheetService.saveTimeSheet(timeSheetDto);

        assertTrue(result.isPresent());
        assertEquals(timeSheetDto, result.get());
    }

    @Test
    void deleteTimeSheet() {
        Long id = 1L;

        doNothing().when(timeSheetRepository).deleteById(id);

        timesheetService.deleteTimeSheet(id);

        verify(timeSheetRepository, times(1)).deleteById(id);
    }
}