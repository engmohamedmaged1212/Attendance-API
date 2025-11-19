package com.college.attendance.Attendance.API.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotNull
    private String studentCode;
    @NotNull
    private String password;
}
