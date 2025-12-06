package com.college.attendance.Attendance.API.services;

import com.college.attendance.Attendance.API.dtos.*;
import com.college.attendance.Attendance.API.entities.Lecture;
import com.college.attendance.Attendance.API.entities.Role;
import com.college.attendance.Attendance.API.entities.User;
import com.college.attendance.Attendance.API.exception.AuthorizationException;
import com.college.attendance.Attendance.API.exception.LectureNotFoundException;
import com.college.attendance.Attendance.API.exception.UserNotFoundException;
import com.college.attendance.Attendance.API.mapper.LectureMapper;
import com.college.attendance.Attendance.API.repositories.AttendanceRecordRepository;
import com.college.attendance.Attendance.API.repositories.LectureRepository;
import com.college.attendance.Attendance.API.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final LectureMapper lectureMapper;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final String userNotFoundMessage = "User was not found";
    private final String lectureNotFoundMessage = "Lecture was not found";

    public LectureInfoDto createLecture(LectureRequestDto lectureRequestDto , Long doctorId){
        var doctor = userRepository.findById(doctorId)
                .orElseThrow(()->new UserNotFoundException("Doctor not found with ID: " + doctorId));
        var lecture = lectureMapper.toLecture(lectureRequestDto);
        lecture.setDoctor(doctor);

        lectureRepository.save(lecture);
        return lectureMapper.toLectureInfoDto(lecture);
    }

    public void endLecture(EndLectureRequestDto endLectureRequestDto , Long doctorId){
        var doctor = userRepository.findById(doctorId).orElseThrow(()->new UserNotFoundException(userNotFoundMessage));
        var lecture = lectureRepository.findById(endLectureRequestDto.getLectureId())
                .orElseThrow(()->new LectureNotFoundException("Lecture was not found"));
        if (!lecture.getDoctor().getId().equals(doctorId)) {
            throw new AuthorizationException("Doctor is not authorized to end this lecture.");
        }
        lecture.setActive(endLectureRequestDto.isActive());
        lectureRepository.save(lecture);
    }

    public List<RecordInfoDto> recordsForLecture( Long lectureId , Long doctorId){
        var doctor = userRepository.findById(doctorId).orElseThrow(()->new UserNotFoundException(userNotFoundMessage));
        var lecture = lectureRepository.findById(lectureId)
                .orElseThrow(()->new LectureNotFoundException("Lecture was not found"));
        if (!lecture.getDoctor().getId().equals(doctorId)) {
            throw new AuthorizationException("Doctor is not authorized to end this lecture.");
        }
        return lecture.getAttendanceRecords().stream()
                .map(lectureMapper::toRecordInfoDto).toList();
    }

    public List<LectureInfoDto> getLecturesForDr(Long doctorId){
        userRepository.findById(doctorId).orElseThrow(()->new UserNotFoundException(userNotFoundMessage));
        var lectures = lectureRepository.findAllByDoctorId(doctorId);
        return lectures.stream().map(lectureMapper::toLectureInfoDto).toList();
    }
    public List<LectureAttendanceDto> getRecordsForLectures(LecturesIdsListRequestDto request, Long doctorId) {

        userRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException(userNotFoundMessage));

        List<Long> lectureIds = request.getLectureIds();

        Map<Long, Lecture> lecturesMap = new HashMap<>();

        for (Long lectureId : lectureIds) {
            Lecture lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new LectureNotFoundException("Lecture with id: " + lectureId + " is not found"));

            if (!lecture.getDoctor().getId().equals(doctorId)) {
                throw new AuthorizationException("Doctor is not authorized for this lecture");
            }

            lecturesMap.put(lectureId, lecture);
        }

        var students = userRepository.findUsersWithRole(Role.STUDENT);

        List<LectureAttendanceDto> response = new ArrayList<>();

        for (Long lectureId : lectureIds) {

            LectureAttendanceDto oneLecture = new LectureAttendanceDto();
            Lecture lecture = lecturesMap.get(lectureId);

            for (var student : students) {

                IsAttendOrNotDto isAttendOrNotDto = new IsAttendOrNotDto();

                isAttendOrNotDto.setStudentCode(student.getStudentCode());

                boolean isAttend =
                        attendanceRecordRepository.existsByStudentAndLecture(student, lecture);

                isAttendOrNotDto.setIsAttend(isAttend);

                oneLecture.getInfo().add(isAttendOrNotDto);
            }

            response.add(oneLecture);
        }

        return response;
    }

}
