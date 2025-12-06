package com.college.attendance.Attendance.API.controller;

import com.college.attendance.Attendance.API.dtos.*;
import com.college.attendance.Attendance.API.entities.User;
import com.college.attendance.Attendance.API.services.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("lectures")
@RequiredArgsConstructor
public class LectureController {
   private final LectureService lectureService;


    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/create")
    public ResponseEntity<?> createLecture(
            @RequestBody @Valid LectureRequestDto lectureRequestDto ,
            @AuthenticationPrincipal User currentDoctor){
        var doctorId = currentDoctor.getId();
        var lecture = lectureService.createLecture(lectureRequestDto , doctorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(lecture);
    }

    @PutMapping("end-lecture")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> endLecture(
            @RequestBody @Valid EndLectureRequestDto endLectureRequestDto,
            @AuthenticationPrincipal User user
            ){
        lectureService.endLecture(endLectureRequestDto , user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/{lectureId}/info")
    public ResponseEntity<?> getInfoAboutLecture(@PathVariable Long lectureId, @AuthenticationPrincipal User user){
        var records = lectureService.recordsForLecture(lectureId , user.getId());
        return ResponseEntity.ok(records);
    }

    @GetMapping("get-all-lectures")
    public ResponseEntity<List<LectureInfoDto>> getAllLecturesForDr(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(lectureService.getLecturesForDr(user.getId()));
    }

    @PostMapping("/get-sheet")
    public ResponseEntity<List<LectureAttendanceDto>> getRecordsForLectures(@RequestBody @Valid LecturesIdsListRequestDto request, @AuthenticationPrincipal User user){
        var response = lectureService.getRecordsForLectures(request, user.getId());
        return ResponseEntity.ok(response);
    }
}
