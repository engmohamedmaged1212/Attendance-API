package com.college.attendance.Attendance.API.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@Data
public class LectureAttendanceDto {
    Long lectureId;
    List<IsAttendOrNotDto> info = new ArrayList<>();
}
