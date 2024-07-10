package com.geekbrains.homeworks.repositories;

import com.geekbrains.homeworks.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProjectRepositoryTest {

    ProjectRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ProjectRepository();
        Project project = new Project();
        Project project2 = new Project();

        // я понимаю что это является хрупким решением,
        // но по другому у меня ни как не получится
        // сбрасывать статическую переменную, без этого тест падает
        // когда мы подключим свою базу данный код изменится
        Field sequenceField = null;
        try {
            sequenceField = ProjectRepository.class.getDeclaredField("sequence");
            sequenceField.setAccessible(true);
            sequenceField.set(null, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        project.setName("Test Project 1");
        project2.setName("Test Project 2");

        repository.saveProject(project);
        repository.saveProject(project2);
    }

    @Test
    void getAllProjects() {
        List<Project> projects = repository.getAllProjects();

        assertEquals(2, projects.size());
        assertEquals("Test Project 1", projects.get(0).getName());
        assertEquals("Test Project 2", projects.get(1).getName());
    }

    @Test
    void getProjectById() {
        Optional<Project> projectOptional = repository.getProjectById(1L);
        Optional<Project> projectOptional2 = repository.getProjectById(2L);

        assertTrue(projectOptional.isPresent());
        assertEquals("Test Project 1", projectOptional.get().getName());

        assertTrue(projectOptional2.isPresent());
        assertEquals("Test Project 2", projectOptional2.get().getName());
    }

    @Test
    void saveProject() {
        Project project = new Project();

        project.setName("Test save Project 1");

        Project savedproject = repository.saveProject(project);
        assertNotNull(savedproject.getId());
        assertEquals("Test save Project 1", savedproject.getName());

        Optional<Project> projectOptional = repository.getProjectById(savedproject.getId());
        assertTrue(projectOptional.isPresent());
        assertEquals(project.getId(), projectOptional.get().getId());
        assertEquals("Test save Project 1", projectOptional.get().getName());
    }

    @Test
    void deleteProjectById() {
        Optional<Project> projectOptional = repository.getProjectById(1L);
        assertTrue(projectOptional.isPresent());

        repository.deleteProjectById(1L);
        Optional<Project> projectOptional2 = repository.getProjectById(1L);
        assertFalse(projectOptional2.isPresent());
    }

    @Test
    void updateProject() {
        Optional<Project> projectOptional = repository.getProjectById(1L);
        assertTrue(projectOptional.isPresent());

        Project updatedProject = projectOptional.get();
        updatedProject.setName("Project 1 Updated");

        repository.updateProject(1L, updatedProject);

        Optional<Project> projectOptional2 = repository.getProjectById(1L);

        assertTrue(projectOptional2.isPresent());
        assertEquals(updatedProject.getId(), projectOptional2.get().getId());
        assertEquals("Project 1 Updated", projectOptional2.get().getName());
    }
}