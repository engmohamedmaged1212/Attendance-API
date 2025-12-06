package com.college.attendance.Attendance.API.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class LecturesIdsListRequestDto {
    @NotNull(message = "array yaaaaaaaaa m3lm")
    private List<Long> lectureIds;
}
