package com.college.attendance.Attendance.API.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmailRequestDto {
    @NotNull(message = "el code please")
    String studentCode;
}
