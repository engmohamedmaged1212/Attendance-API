package com.college.attendance.Attendance.API.dtos;

import com.college.attendance.Attendance.API.entities.Role;
import lombok.Data;

@Data
public class UserDto {
    Long id ;
    Role role;
    String email;
}
