package com.college.attendance.Attendance.API.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordAttendanceDto {
    String name;
    String studentCode;
    String lectureTitle;
    LocalDateTime recordedAt;
}
