package com.geekbrains.homeworks.model.mapers;

import com.geekbrains.homeworks.model.Project;
import com.geekbrains.homeworks.model.dtos.ProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectDto toProjectDto(Project project);

    Project toProject(ProjectDto projectDto);
}
