package com.college.attendance.Attendance.API.mapper;

import com.college.attendance.Attendance.API.dtos.RecordAttendanceDto;
import com.college.attendance.Attendance.API.entities.AttendanceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceRecordMapper {
    @Mapping(target = "name", source = "student.name")
    @Mapping(target = "studentCode", source = "student.studentCode")
    @Mapping(target = "lectureTitle", source = "lecture.title")
    @Mapping(target = "recordedAt", source = "recordedAt")
    RecordAttendanceDto toDto(AttendanceRecord attendanceRecord);
}
