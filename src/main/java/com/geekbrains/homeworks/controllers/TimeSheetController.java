package com.geekbrains.homeworks.controllers;

import com.geekbrains.homeworks.model.TimeSheet;
import com.geekbrains.homeworks.model.dtos.TimeSheetDto;
import com.geekbrains.homeworks.services.TimesheetService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/time-sheets")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TimeSheetController {

    final TimesheetService timesheetService;

    @GetMapping
    public ResponseEntity<List<TimeSheet>> getAllTimesheets(@RequestParam(required = false, value = "created-at") LocalDate date) {
        if (date != null){
            List<TimeSheet> timeSheets = timesheetService.getTimeSheetsByCreatedAtIsAfter(date);
            if (timeSheets.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(timeSheets);
        }
        return ResponseEntity.ok().body(timesheetService.getAllTimeSheets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeSheetDto> getTimesheetById(@PathVariable Long id) {
        Optional<TimeSheetDto> foundTimeSheet = timesheetService.getTimeSheetById(id);
        return foundTimeSheet.map(timeSheet -> ResponseEntity.ok().body(timeSheet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TimeSheetDto> createTimesheet(@RequestBody TimeSheetDto timeSheetDto) {
        Optional<TimeSheetDto> existingTimeSheet = timesheetService.saveTimeSheet(timeSheetDto);
        if (existingTimeSheet.isPresent()) {
            TimeSheetDto savedTimeSheetDto = existingTimeSheet.get();

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/time-sheets/{id}")
                    .buildAndExpand(savedTimeSheetDto.getId())
                    .toUri();

            return ResponseEntity.created(location).body(savedTimeSheetDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/time-sheets/{id}")
    public ResponseEntity<Void> deleteTimesheet(@PathVariable Long id) {
        timesheetService.deleteTimeSheet(id);
        return ResponseEntity.noContent().build();
    }
}
