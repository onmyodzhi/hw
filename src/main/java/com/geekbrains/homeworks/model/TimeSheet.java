package com.geekbrains.homeworks.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "timesheet")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    Long id;

    @Column(name = "project_id", nullable = false)
    Long projectId;

    @Column(name = "employee_id")
    Long employeeId;

    @Column(name = "minutes")
    Integer minutes;

    @Column(name = "created_at")
    LocalDate createdAt;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "projects_employees",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employees_id"))
            @ToString.Exclude
    Set<Employee> employees;
}
