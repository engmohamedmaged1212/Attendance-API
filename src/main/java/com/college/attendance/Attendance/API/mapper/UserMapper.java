package com.college.attendance.Attendance.API.mapper;

import com.college.attendance.Attendance.API.dtos.RegisterStudentDto;
import com.college.attendance.Attendance.API.dtos.UserDto;
import com.college.attendance.Attendance.API.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toUser(UserDto userDto);
    User toUser(RegisterStudentDto registerStudentDto);
}
