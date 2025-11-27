package com.college.attendance.Attendance.API.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendManually {
    @NotNull(message = "المحاااااضرة ايييييييييييييه ؟؟؟")
    Long lectureId;
    @NotNull(message = "مييييييييييين الطالب ؟؟؟؟؟؟؟؟؟؟")
    String studentCode;
}
