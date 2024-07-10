package com.geekbrains.homeworks.repositories;

import com.geekbrains.homeworks.model.TimeSheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TimeSheetRepositoryTest {

    TimeSheetRepository timeSheetRepository;

    @BeforeEach
    void setUp() {
        timeSheetRepository = new TimeSheetRepository();

        Field sequenceFailed = null;
        try {
            sequenceFailed = TimeSheetRepository.class.getDeclaredField("sequence");
            sequenceFailed.setAccessible(true);
            sequenceFailed.set(null, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        TimeSheet timeSheet = new TimeSheet();
        TimeSheet timeSheet2 = new TimeSheet();

        timeSheet.setCreatedAt(LocalDate.now());
        timeSheet.setMinutes(120);
        timeSheet.setProjectId(1L);

        timeSheet2.setCreatedAt(LocalDate.now());
        timeSheet2.setMinutes(130);
        timeSheet2.setProjectId(2L);

        timeSheetRepository.save(timeSheet);
        timeSheetRepository.save(timeSheet2);
    }

    @Test
    void getById() {
        Optional<TimeSheet> timeSheet = timeSheetRepository.getById(1L);

        assertTrue(timeSheet.isPresent());
        assertEquals(timeSheet.get().getCreatedAt(), LocalDate.now());
        assertEquals(timeSheet.get().getMinutes(), 120);
        assertEquals(timeSheet.get().getProjectId(), 1L);
    }

    @Test
    void getAll() {
        List<TimeSheet> timeSheets = timeSheetRepository.getAll();
        assertFalse(timeSheets.isEmpty());
        assertEquals(2, timeSheets.size());
    }

    @Test
    void getTimeSheetsByProjectId() {
        List<TimeSheet> timeSheets = timeSheetRepository.getTimeSheetsByProjectId(1L);
        assertFalse(timeSheets.isEmpty());
        assertEquals(1, timeSheets.size());
    }

    @Test
    void getTimeSheetsByCreatedAtIsAfter() {
        List<TimeSheet> timeSheetsCorrect = timeSheetRepository.getTimeSheetsByCreatedAtIsAfter(LocalDate.of(2024,7,4));
        List<TimeSheet> timeSheetsFailed = timeSheetRepository.getTimeSheetsByCreatedAtIsAfter(LocalDate.of(2027,7,4));

        assertFalse(timeSheetsCorrect.isEmpty());
        assertEquals(2, timeSheetsCorrect.size());
        assertTrue(timeSheetsFailed.isEmpty());
    }

    @Test
    void save() {
        TimeSheet timeSheet = new TimeSheet();

        timeSheet.setCreatedAt(LocalDate.now());
        timeSheet.setMinutes(120);
        timeSheet.setProjectId(1L);

        timeSheetRepository.save(timeSheet);

        Optional<TimeSheet> timeSheetOptional = timeSheetRepository.getById(timeSheet.getId());

        assertTrue(timeSheetOptional.isPresent());
        assertEquals(timeSheetOptional.get().getCreatedAt(), LocalDate.now());
        assertEquals(timeSheetOptional.get().getMinutes(), 120);
        assertEquals(timeSheetOptional.get().getProjectId(), 1L);
    }

    @Test
    void deleteById() {
        timeSheetRepository.deleteById(1L);
        Optional<TimeSheet> timeSheetOptional = timeSheetRepository.getById(1L);
        assertFalse(timeSheetOptional.isPresent());
    }
}