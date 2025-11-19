package com.college.attendance.Attendance.API.dtos;

import com.college.attendance.Attendance.API.entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LectureRequestDto {

    boolean active = true;
    @NotNull(message = "Title is required")
    String title;
    LocalDateTime createdAt =LocalDateTime.now();

}
