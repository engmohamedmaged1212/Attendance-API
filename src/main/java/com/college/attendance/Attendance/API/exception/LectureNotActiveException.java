package com.college.attendance.Attendance.API.exception;

public class LectureNotActiveException extends RuntimeException {
    public LectureNotActiveException(String message) {
        super(message);
    }
}
