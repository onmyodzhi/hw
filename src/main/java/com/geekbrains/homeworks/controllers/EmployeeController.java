package com.geekbrains.homeworks.controllers;

import com.geekbrains.homeworks.model.dtos.EmployeeDto;
import com.geekbrains.homeworks.model.dtos.TimeSheetDto;
import com.geekbrains.homeworks.services.EmployeeService;
import com.geekbrains.homeworks.services.TimesheetService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeController {

    final EmployeeService employeeService;
    final TimesheetService timesheetService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok().body(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable long id) {
        Optional<EmployeeDto> employee = employeeService.getEmployeeById(id);
        return employee.map(employeeDto -> ResponseEntity.ok().body(employeeDto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<TimeSheetDto>> getAllTimesheetsByEmployeeId(@PathVariable Long id){
        List<TimeSheetDto> timeSheetsDto = timesheetService.getAllTimeSheetsByEmployeeId(id);
                return ResponseEntity.ok().body(timeSheetsDto);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employee) {
        Optional<EmployeeDto> employeeForCreate = employeeService.createEmployee(employee);

        if (employeeForCreate.isPresent()) {

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(employeeForCreate.get().getId())
                    .toUri();

            return ResponseEntity.created(location).body(employeeForCreate.get());
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employee) {
        Optional<EmployeeDto> employeeForUpdate = employeeService.updateEmployee(id, employee);
        return employeeForUpdate.map(employeeDto -> ResponseEntity.ok().body(employeeDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDto> deleteEmployee(@PathVariable Long id) {
        Optional<EmployeeDto> employeeForDelete = employeeService.deleteEmployee(id);
        if (employeeForDelete.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
