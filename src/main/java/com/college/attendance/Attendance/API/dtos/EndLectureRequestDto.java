package com.college.attendance.Attendance.API.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EndLectureRequestDto {
    @NotNull(message = "LectureId mustn't be empty")
    long lectureId;
    boolean active = false;
}
