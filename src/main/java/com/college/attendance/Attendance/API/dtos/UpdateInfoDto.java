package com.college.attendance.Attendance.API.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateInfoDto {
    @NotBlank(message = "الايميل يا معلم ")
    @Email(message = "الايميييييييييييل يا معلم")
    String email;
    @NotNull(message = "باسورد وياريت متنساهوش")
    @Size(min = 6, message = "ايه الباسورد العبيط ده، يجب أن لا يقل عن 6 أحرف")
    String newPassword ;
}
