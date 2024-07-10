package com.geekbrains.homeworks.controllers;

import com.geekbrains.homeworks.model.TimeSheet;
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
/*@RequestMapping("/time-sheets/")пришлось убрать эту анотацию
  так как при выполнениии задания 4 у меня получался путь
  http://localhost:8080/time-sheets/projects/4/time-sheets
  а не http://localhost:8080/projects/4/time-sheets
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TimeSheetController {

    final TimesheetService timesheetService;

    @GetMapping("/time-sheets")
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

    @GetMapping("/time-sheets/{id}")
    public ResponseEntity<TimeSheet> getTimesheetById(@PathVariable Long id) {
        Optional<TimeSheet> foundTimeSheet = timesheetService.getTimeSheetById(id);
        return foundTimeSheet.map(timeSheet -> ResponseEntity.ok().body(timeSheet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     я бы сделал другой путь - /time-sheets/projects/{id}
     так как он больше отражает то что именно мы хотим получить
     где /time-sheets изначальный результат того что мы хотим получить
         /projects по какому критерию ищем
         /{id} id того элемента который должен быть
     а путь /projects/{id}/time-sheets
      выглядит так как будто мы ищем проект по id если он находится в time-sheets
      хотя при этом мы не можем создать time-sheets без привязки к проекту
      тоесть по сути этот url выглядит как то что мы пытаемся проверить
      что у нас проект привязан к какомунибудь time-sheep
      по крайней мере я так понимаю этот путь
     */
    @GetMapping("/projects/{id}/time-sheets")
    public ResponseEntity<List<TimeSheet>> getTimesheetsByProjectId(@PathVariable Long id) {
        List<TimeSheet> timeSheets = timesheetService.getTimeSheetsByProjectId(id);
        if (timeSheets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(timeSheets);
    }

    @PostMapping("/time-sheets")
    public ResponseEntity<TimeSheet> createTimesheet(@RequestBody TimeSheet timeSheet) {
        Optional<TimeSheet> existingTimeSheet = timesheetService.saveTimeSheet(timeSheet);
        if (existingTimeSheet.isPresent()) {
            TimeSheet savedTimeSheet = existingTimeSheet.get();

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/time-sheets/{id}")
                    .buildAndExpand(savedTimeSheet.getId())
                    .toUri();

            return ResponseEntity.created(location).body(savedTimeSheet);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/time-sheets/{id}")
    public ResponseEntity<Void> deleteTimesheet(@PathVariable Long id) {
        timesheetService.deleteTimeSheet(id);
        return ResponseEntity.noContent().build();
    }
}
