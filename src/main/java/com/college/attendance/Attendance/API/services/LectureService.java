package com.college.attendance.Attendance.API.services;

import com.college.attendance.Attendance.API.dtos.*;
import com.college.attendance.Attendance.API.exception.AuthorizationException;
import com.college.attendance.Attendance.API.exception.LectureNotFoundException;
import com.college.attendance.Attendance.API.exception.UserNotFoundException;
import com.college.attendance.Attendance.API.mapper.LectureMapper;
import com.college.attendance.Attendance.API.repositories.LectureRepository;
import com.college.attendance.Attendance.API.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final LectureMapper lectureMapper;
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

}
