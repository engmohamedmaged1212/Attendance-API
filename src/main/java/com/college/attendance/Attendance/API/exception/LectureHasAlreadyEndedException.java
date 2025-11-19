package com.college.attendance.Attendance.API.exception;

public class LectureHasAlreadyEndedException extends RuntimeException {
    public LectureHasAlreadyEndedException(String message) {
        super(message);
    }
}
