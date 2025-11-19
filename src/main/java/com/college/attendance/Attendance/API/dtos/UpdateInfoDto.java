package com.college.attendance.Attendance.API.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateInfoDto {
    @NotBlank(message = "الايميل يا معلم ")
    @Email(message = "الايميييييييييييل يا معلم")
    String email;
    @NotNull(message = "باسورد وياريت متنساهوش")
    @Min(message = "ايه الباسورد العبيط ده" , value = 8)
    String newPassword ;
}
