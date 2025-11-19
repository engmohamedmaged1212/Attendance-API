package com.college.attendance.Attendance.API.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendRequestDto {
    @NotNull(message = "Scan ya m3lem ;)")
    Long lectureId;
}
