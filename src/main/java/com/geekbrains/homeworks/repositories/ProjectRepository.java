package com.geekbrains.homeworks.repositories;

import com.geekbrains.homeworks.model.Project;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectRepository {
    static Long sequence = 1L;
    final List<Project> projects = new ArrayList<>();

    public List<Project> getAllProjects() {
        return List.copyOf(projects);
    }

    public Optional<Project> getProjectById(Long id) {
        return projects.stream().filter(project -> project.getId().equals(id)).findFirst();
    }

    public Project saveProject(Project project) {
        project.setId(sequence++);
        projects.add(project);
        return project;
    }

    public void deleteProjectById(Long id) {
        projects.removeIf(project -> project.getId().equals(id));
    }

    public Optional<Project> updateProject(Long id, Project project) {
        Optional<Project> projectOptional = getProjectById(id);
        projectOptional.ifPresent(value -> value.setName(project.getName()));
        return projectOptional;
    }
}
