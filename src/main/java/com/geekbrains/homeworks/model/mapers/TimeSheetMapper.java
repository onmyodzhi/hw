package com.geekbrains.homeworks.model.mapers;

import com.geekbrains.homeworks.model.TimeSheet;
import com.geekbrains.homeworks.model.dtos.TimeSheetDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TimeSheetMapper {

    TimeSheetMapper INSTANCE = Mappers.getMapper(TimeSheetMapper.class);

    TimeSheetDto timeSheetToTimeSheetDto(TimeSheet timeSheet);

    TimeSheet timeSheetDtoToTimeSheet(TimeSheetDto timeSheetDto);
}
