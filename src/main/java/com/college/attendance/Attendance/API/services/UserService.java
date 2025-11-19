package com.college.attendance.Attendance.API.services;
import com.college.attendance.Attendance.API.dtos.*;
import com.college.attendance.Attendance.API.entities.*;
import com.college.attendance.Attendance.API.exception.*;
import com.college.attendance.Attendance.API.mapper.AttendanceRecordMapper;
import com.college.attendance.Attendance.API.mapper.UserMapper;
import com.college.attendance.Attendance.API.repositories.AttendanceRecordRepository;
import com.college.attendance.Attendance.API.repositories.LectureRepository;
import com.college.attendance.Attendance.API.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AttendanceRecordMapper attendanceRecordMapper;
    private final String userNotFoundMessage = "User was not found";
    private final String lectureNotFoundMessage = "Lecture was not found";
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final LectureRepository lectureRepository;

    private User findStudentByCode(String code) {
        return userRepository.findByStudentCode(code)
                .orElseThrow(() -> new UserNotFoundException(userNotFoundMessage + " with code: " + code));
    }

    public String findStudentEmailByCode(String code) {
       var user = userRepository.findByStudentCode(code)
                .orElseThrow(() -> new UserNotFoundException(userNotFoundMessage + " with code: " + code));
       return user.getEmail();
    }
    public List<UserDto> getAllStudents(String sort){
        return userRepository.findUsersWithRole(Role.STUDENT)
                .stream()
                .map(userMapper:: toDto)
                .toList();
    }

    public UserDto getStudent(String code){
        var student = findStudentByCode(code);
        return userMapper.toDto(student);
    }


    public void changePassword(long id, ChangePasswordDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(userNotFoundMessage));


        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    public UserDto createStudent(RegisterStudentDto registerStudentDto){

        User user = userMapper.toUser(registerStudentDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.STUDENT);
        user.setStudentCode(registerStudentDto.getCode());
        user.setRole(Role.valueOf(registerStudentDto.getRole()));
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void updateInfo(UpdateInfoDto updateInfoDto , long id){
        var user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(userNotFoundMessage));
        user.setPassword(passwordEncoder.encode(updateInfoDto.getNewPassword()));
        user.setEmail(updateInfoDto.getEmail());
        userRepository.save(user);
    }
    public RecordAttendanceDto attend(AttendRequestDto attendRequestDto, Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Student not found with ID: " + studentId));

        Lecture lecture = lectureRepository.findById(attendRequestDto.getLectureId())
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found with ID: " + attendRequestDto.getLectureId()));


        if (attendanceRecordRepository.existsByStudentAndLecture(student, lecture)) {
            throw new AttendanceAlreadyRecordedException("Attendance already recorded for this lecture.");
        }

        if (!lecture.isActive()) {
            throw new LectureNotActiveException("Attendance recording is not active for this lecture.");
        }

        if (!student.getRole().equals(Role.STUDENT)) {
            throw new InvalidUserRoleException("Only students can record attendance.");
        }
        AttendanceRecord record = new AttendanceRecord();
        record.setStudent(student);
        record.setLecture(lecture);
        record.setRecordedAt(LocalDateTime.now());

        AttendanceRecord savedRecord = attendanceRecordRepository.save(record);

        return attendanceRecordMapper.toDto(savedRecord);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByStudentCode(username).orElseThrow(()->new UserNotFoundException(userNotFoundMessage));

    }
}
