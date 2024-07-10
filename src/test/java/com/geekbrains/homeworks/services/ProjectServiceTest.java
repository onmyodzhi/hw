package com.geekbrains.homeworks.services;

import com.geekbrains.homeworks.model.Project;
import com.geekbrains.homeworks.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProjects() {
        Project project = new Project();
        Project project1 = new Project();

        project.setId(1L);
        project.setName("Project 1");
        project1.setId(2L);
        project1.setName("Project 2");

        List<Project> mockProjects = Arrays.asList(project, project1);
        when(projectRepository.getAllProjects())
        .thenReturn(mockProjects);

        List<Project> projects = projectService.getAllProjects();
        assertEquals(mockProjects, projects);
    }

    @Test
    void getProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");

        when(projectRepository.getProjectById(1L))
                .thenReturn(Optional.of(project));

        Optional<Project> projectOptional = projectService.getProjectById(1L);
        assertTrue(projectOptional.isPresent());
        assertEquals(project, projectOptional.get());
    }

    @Test
    void saveProject() {
        Project project = new Project();
        project.setName("Project 1");
        when(projectRepository.saveProject(project))
        .thenReturn(project);

        Project savedProject = projectService.saveProject(project);
        assertEquals(project, savedProject);

        verify(projectRepository, times(1)).saveProject(project);
    }

    @Test
    void deleteProject() {
        Long projectId = 1L;

        doNothing().when(projectRepository).deleteProjectById(projectId);
        projectService.deleteProject(projectId);
        verify(projectRepository, times(1)).deleteProjectById(projectId);
    }

    @Test
    void updateProject() {
        Long projectId = 1L;
        Project project = new Project();
        project.setName("Project 1");

        when(projectRepository.updateProject(projectId,project))
                .thenReturn(Optional.of(project));

        Optional<Project> updatedProject = projectService.updateProject(projectId, project);
        assertTrue(updatedProject.isPresent());
        assertEquals(project, updatedProject.get());

        verify(projectRepository, times(1)).updateProject(projectId,project);
    }
}