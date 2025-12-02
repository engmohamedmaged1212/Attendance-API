package com.college.attendance.Attendance.API.controller;

import com.college.attendance.Attendance.API.dtos.*;
import com.college.attendance.Attendance.API.entities.User;
import com.college.attendance.Attendance.API.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<UserDto> getAllStudents(
            @RequestParam(required = false , defaultValue = "" , name = "sort") String sortBy){
        if(!Set.of("studentCode" , "email" , "name").contains(sortBy))
        {sortBy ="studentCode";}
        return userService.getAllStudents(sortBy);
    }

    @PutMapping("/me/update-info")
    public ResponseEntity<Void> updateInfo(@Valid @RequestBody UpdateInfoDto updateInfoDto , @AuthenticationPrincipal User user){
        userService.updateInfo(updateInfoDto , user.getId());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/change-password")
    public ResponseEntity<Void> changeMyPassword(
            @Valid @RequestBody ChangePasswordDto changePasswordDto
    ) {
        userService.changePassword(changePasswordDto.getStudentCode(), changePasswordDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterStudentDto registerStudentDto){
        var userDto = userService.createStudent(registerStudentDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/attend")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<RecordAttendanceDto> attend(
            @Valid @RequestBody AttendRequestDto attendRequestDto,
            @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            System.out.println("problem from controller - UserDetails is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        RecordAttendanceDto attendanceRecord = userService.attend(attendRequestDto, user.getId());
        return ResponseEntity.ok(attendanceRecord);
    }
    @GetMapping("/{studentCode}/get-email")
    public ResponseEntity<GetEmailResponseDto> getEmail(@PathVariable String  studentCode){
        var email = userService.findStudentEmailByCode(studentCode);
        return ResponseEntity.ok(new GetEmailResponseDto(email));
    }
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/attend-manually")
    public ResponseEntity<?> attendManually(@Valid @RequestBody AttendManually request , @AuthenticationPrincipal User doctor){
       return ResponseEntity.accepted().body( userService.attendManually(doctor.getId(),  request));
    }
}
