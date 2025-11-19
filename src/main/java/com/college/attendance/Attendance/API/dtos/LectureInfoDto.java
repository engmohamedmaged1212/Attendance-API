package com.college.attendance.Attendance.API.dtos;

import com.college.attendance.Attendance.API.entities.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LectureInfoDto {
    Long id;
    boolean active ;
    String title;
    LocalDateTime createdAt;
    String  doctorName;
}
