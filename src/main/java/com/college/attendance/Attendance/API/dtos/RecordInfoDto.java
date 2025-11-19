package com.college.attendance.Attendance.API.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordInfoDto {
    String studentName;
    String studentCode;
    LocalDateTime recordedAt;
}
