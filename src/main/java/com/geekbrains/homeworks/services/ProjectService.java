package com.geekbrains.homeworks.services;

import com.geekbrains.homeworks.model.Project;
import com.geekbrains.homeworks.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProjectService {
    ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.getProjectById(id);
    }

    public Project saveProject(Project project) {
        return projectRepository.saveProject(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteProjectById(id);
    }

    public Optional<Project> updateProject(Long id, Project project) {
        return projectRepository.updateProject(id, project);
    }
}
