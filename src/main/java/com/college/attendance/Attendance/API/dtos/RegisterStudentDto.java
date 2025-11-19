package com.college.attendance.Attendance.API.dtos;

import com.college.attendance.Attendance.API.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterStudentDto {
    @NotBlank(message = "Name is required")
    @Size(max = 255 , message = "Name must be less than 255")
    String name;
    @NotBlank(message = "password is required")
    String password;
    @NotBlank(message = "code is required")
    String code;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email;
    @NotNull(message = "DOCTOR or  STUDENT")
    String role;
}
