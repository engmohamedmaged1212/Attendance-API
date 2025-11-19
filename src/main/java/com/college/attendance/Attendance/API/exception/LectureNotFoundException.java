package com.college.attendance.Attendance.API.exception;

public class LectureNotFoundException extends RuntimeException {
    public LectureNotFoundException(String message) {
        super(message);
    }
}
