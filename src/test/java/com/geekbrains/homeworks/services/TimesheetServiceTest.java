package com.geekbrains.homeworks.services;

import com.geekbrains.homeworks.model.Project;
import com.geekbrains.homeworks.model.TimeSheet;
import com.geekbrains.homeworks.repositories.TimeSheetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
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

        when(timesheetService.getTimeSheetById(1L))
                .thenReturn(Optional.of(timeSheet));

        Optional<TimeSheet> result = timesheetService.getTimeSheetById(1L);
        assertTrue(result.isPresent());
        assertEquals(timeSheet, result.get());
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
    void getTimeSheetsByProjectId() {
        TimeSheet timeSheet = new TimeSheet();
        TimeSheet timeSheet1 = new TimeSheet();

        timeSheet.setId(1L);
        timeSheet.setCreatedAt(LocalDate.now());
        timeSheet.setMinutes(120);
        timeSheet.setProjectId(1L);
        timeSheet1.setId(2L);
        timeSheet1.setProjectId(2L);
        timeSheet1.setMinutes(133);
        timeSheet1.setCreatedAt(LocalDate.now());

        List<TimeSheet> mockTimeSheets = Arrays.asList(timeSheet, timeSheet1);

        when(timesheetService.getTimeSheetsByProjectId(1L))
                .thenReturn(mockTimeSheets);

        List<TimeSheet> timeSheets = timesheetService.getTimeSheetsByProjectId(1L);
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

        when(timeSheetRepository.getTimeSheetsByCreatedAtIsAfter(dateForFilter))
                .thenReturn(expectedTimesheets);

        List<TimeSheet> result = timesheetService.getTimeSheetsByCreatedAtIsAfter(dateForFilter);
        assertEquals(expectedTimesheets, result);
    }

    @Test
    void saveTimeSheet() {
        TimeSheet timeSheet = new TimeSheet();
        Project project = new Project(1L, "Test Project");

        timeSheet.setId(1L);
        timeSheet.setCreatedAt(LocalDate.now());
        timeSheet.setMinutes(120);
        timeSheet.setProjectId(1L);

        when(projectService.getProjectById(1L))
                .thenReturn(Optional.of(project));
        when(timeSheetRepository.save(timeSheet))
                .thenReturn(timeSheet);

        Optional<TimeSheet> result = timesheetService.saveTimeSheet(timeSheet);

        assertTrue(result.isPresent());
        assertEquals(timeSheet, result.get());
    }

    @Test
    void deleteTimeSheet() {
        Long id = 1L;

        doNothing().when(timeSheetRepository).deleteById(id);

        timesheetService.deleteTimeSheet(id);

        verify(timeSheetRepository, times(1)).deleteById(id);
    }
}