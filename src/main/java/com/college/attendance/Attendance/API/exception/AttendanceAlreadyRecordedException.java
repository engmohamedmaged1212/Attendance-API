package com.college.attendance.Attendance.API.exception;

public class AttendanceAlreadyRecordedException extends RuntimeException {
    public AttendanceAlreadyRecordedException(String message) {
        super(message);
    }
}
