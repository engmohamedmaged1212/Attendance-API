package com.college.attendance.Attendance.API.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class IsAttendOrNotDto {
    public String studentCode;
    public Boolean isAttend;
}
