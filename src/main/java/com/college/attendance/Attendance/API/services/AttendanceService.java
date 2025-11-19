package com.college.attendance.Attendance.API.services;

import com.college.attendance.Attendance.API.entities.AttendanceRecord;
import com.college.attendance.Attendance.API.entities.AttendanceStatus;
import com.college.attendance.Attendance.API.entities.Lecture;
import com.college.attendance.Attendance.API.entities.User;
import com.college.attendance.Attendance.API.exception.LectureNotFoundException;
import com.college.attendance.Attendance.API.mapper.UserMapper;
import com.college.attendance.Attendance.API.repositories.AttendanceRecordRepository;
import com.college.attendance.Attendance.API.repositories.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final LectureRepository lectureRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    public void recordAttendance(User student, Long lectureId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with id: " + lectureId));


        Optional<AttendanceRecord> existingRecord = attendanceRecordRepository
                .findByStudentIdAndLectureId(student.getId(), lectureId);

        if (existingRecord.isPresent()) {
            throw new IllegalStateException("You have already attended this lecture.");
        }


         AttendanceRecord newRecord = AttendanceRecord.builder()
                .student((student))
                .lecture(lecture)
                .status(AttendanceStatus.PRESENT)
                .build();


        attendanceRecordRepository.save(newRecord);
    }
}
