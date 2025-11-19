package com.college.attendance.Attendance.API.mapper;

import com.college.attendance.Attendance.API.dtos.LectureInfoDto;
import com.college.attendance.Attendance.API.dtos.LectureRequestDto;
import com.college.attendance.Attendance.API.dtos.RecordInfoDto;
import com.college.attendance.Attendance.API.entities.AttendanceRecord;
import com.college.attendance.Attendance.API.entities.Lecture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LectureMapper {

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "isActive", source = "active")
    Lecture toLecture(LectureRequestDto lectureRequestDto);

    @Mapping(source = "doctor.name", target = "doctorName")
    @Mapping(target = "active", source = "active")
    LectureInfoDto toLectureInfoDto(Lecture lecture);


    @Mapping(target = "studentName", source = "student.name")      // ⬅️ جلب الاسم من كائن Student
    @Mapping(target = "studentCode", source = "student.studentCode") // ⬅️ جلب الكود من كائن Student
    @Mapping(target = "recordedAt", source = "recordedAt")          // ⬅️ جلب تاريخ التسجيل
    RecordInfoDto toRecordInfoDto(AttendanceRecord attendanceRecord);
}
