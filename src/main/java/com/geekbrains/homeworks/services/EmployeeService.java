package com.geekbrains.homeworks.services;

import com.geekbrains.homeworks.model.Employee;
import com.geekbrains.homeworks.model.dtos.EmployeeDto;
import com.geekbrains.homeworks.model.mapers.EmployeeMapper;
import com.geekbrains.homeworks.repositories.EmployeeRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EmployeeService {

    final EmployeeRepository employeeRepository;

    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(EmployeeMapper.INSTANCE::toEmployeeDto).toList();
    }

    public Optional<EmployeeDto> getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(EmployeeMapper.INSTANCE::toEmployeeDto);
    }

    public Optional<EmployeeDto> createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.INSTANCE.toEmployee(employeeDto);
        employeeRepository.save(employee);
        return Optional.of(EmployeeMapper.INSTANCE.toEmployeeDto(employee));
    }

    public Optional<EmployeeDto> updateEmployee(Long id, EmployeeDto employeeDto) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employee.get().setName(employeeDto.getName());
            return Optional.of(EmployeeMapper.INSTANCE.toEmployeeDto(employeeRepository.save(employee.get())));
        }
        return Optional.empty();
    }

    public Optional<EmployeeDto> deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.deleteById(id);
            return Optional.of(EmployeeMapper.INSTANCE.toEmployeeDto(employee.get()));
        }
        return Optional.empty();
    }
}
