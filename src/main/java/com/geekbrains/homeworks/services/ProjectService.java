package com.geekbrains.homeworks.services;

import com.geekbrains.homeworks.model.Project;
import com.geekbrains.homeworks.model.dtos.ProjectDto;
import com.geekbrains.homeworks.model.dtos.TimeSheetDto;
import com.geekbrains.homeworks.model.mapers.ProjectMapper;
import com.geekbrains.homeworks.model.mapers.TimeSheetMapper;
import com.geekbrains.homeworks.repositories.ProjectRepository;
import com.geekbrains.homeworks.repositories.TimeSheetRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProjectService {
    ProjectRepository projectRepository;
    TimeSheetRepository timeSheetRepository;

    public List<ProjectDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(ProjectMapper.INSTANCE::toProjectDto).toList();
    }

    public Optional<ProjectDto> getProjectById(Long id) {
        return projectRepository.findById(id).map(ProjectMapper.INSTANCE::toProjectDto);
    }

    public Optional<ProjectDto> saveProject(ProjectDto projectDto) {
        Project project = ProjectMapper.INSTANCE.toProject(projectDto);
        Project savedProject = projectRepository.save(project);
        return Optional.of(ProjectMapper.INSTANCE.toProjectDto(savedProject));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public Optional<ProjectDto> updateProject(Long id, ProjectDto projectDto) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            project.get().setName(projectDto.getName());
            projectRepository.save(project.get());
            return Optional.of(ProjectMapper.INSTANCE.toProjectDto(project.get()));
        }
        return Optional.empty();
    }

    public List<TimeSheetDto> getTimeSheetsByProjectId(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            return timeSheetRepository.findAllByProjectId(projectId)
                    .stream().map(TimeSheetMapper.INSTANCE::timeSheetToTimeSheetDto).toList();
        }
        else throw new NoSuchElementException("Project with id = " + id + " does not exists");
    }
}
