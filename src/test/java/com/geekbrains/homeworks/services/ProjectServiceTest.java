package com.geekbrains.homeworks.services;

import com.geekbrains.homeworks.model.Project;
import com.geekbrains.homeworks.model.TimeSheet;
import com.geekbrains.homeworks.model.dtos.ProjectDto;
import com.geekbrains.homeworks.model.dtos.TimeSheetDto;
import com.geekbrains.homeworks.model.mapers.ProjectMapper;
import com.geekbrains.homeworks.model.mapers.TimeSheetMapper;
import com.geekbrains.homeworks.repositories.ProjectRepository;
import com.geekbrains.homeworks.repositories.TimeSheetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    TimeSheetRepository timeSheetRepository;

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
        when(projectRepository.findAll()).thenReturn(mockProjects);

        List<ProjectDto> expectedProjectDtos = mockProjects.stream()
                .map(ProjectMapper.INSTANCE::toProjectDto)
                .collect(Collectors.toList());

        List<ProjectDto> projects = projectService.getAllProjects();
        assertEquals(expectedProjectDtos, projects);
    }

    @Test
    void getProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectDto expectedProjectDto = ProjectMapper.INSTANCE.toProjectDto(project);

        Optional<ProjectDto> projectOptional = projectService.getProjectById(1L);
        assertTrue(projectOptional.isPresent());
        assertEquals(expectedProjectDto, projectOptional.get());
    }

    @Test
    void getTimeSheetsByProjectId() {
        TimeSheet timeSheet = new TimeSheet();
        TimeSheet timeSheet1 = new TimeSheet();

        timeSheet.setId(1L);
        timeSheet.setCreatedAt(LocalDate.now());
        timeSheet.setMinutes(120);
        timeSheet.setProjectId(1L);
        timeSheet1.setId(2L);
        timeSheet1.setProjectId(2L);
        timeSheet1.setMinutes(133);
        timeSheet1.setCreatedAt(LocalDate.now());

        List<TimeSheet> mockTimeSheets = Arrays.asList(timeSheet, timeSheet1);
        List<TimeSheetDto> expectedTimeSheets = Stream.of(timeSheet, timeSheet1)
                .map(TimeSheetMapper.INSTANCE::timeSheetToTimeSheetDto).toList();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(new Project()));
        when(timeSheetRepository.findAllByProjectId(1L))
                .thenReturn(mockTimeSheets);

        List<TimeSheetDto> timeSheetsDtos = projectService.getTimeSheetsByProjectId(1L);
        assertEquals(expectedTimeSheets, timeSheetsDtos);
    }

    @Test
    void saveProject() {
        ProjectDto sourceProjectDto = new ProjectDto();
        sourceProjectDto.setName("Test Project");

        Project projectAfterSave = new Project();
        projectAfterSave.setId(1L);
        projectAfterSave.setName("Test Project");

        when(projectRepository.save(any(Project.class)))
                .thenReturn(projectAfterSave);

        ProjectDto expectedProjectDto = ProjectMapper.INSTANCE.toProjectDto(projectAfterSave);

        Optional<ProjectDto> result = projectService.saveProject(sourceProjectDto);

        assertEquals(Optional.of(expectedProjectDto), result);
    }


    @Test
    void deleteProject() {
        Long projectId = 1L;

        doNothing().when(projectRepository).deleteById(projectId);
        projectService.deleteProject(projectId);
        verify(projectRepository, times(1)).deleteById(projectId);
    }
}