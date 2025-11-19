package com.college.attendance.Attendance.API.controller;

import com.college.attendance.Attendance.API.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String , String>> handleValidationErrors(
            MethodArgumentNotValidException exception
    ){
        var errors = new HashMap<String ,String>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField() , error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String , String>> handleUserNotFound(UserNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error" , ex.getMessage() ));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String , String>> handleInvalidPassword(InvalidPasswordException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error" , ex.getMessage() ));
    }

        @ExceptionHandler(AttendanceAlreadyRecordedException.class)
        public ResponseEntity<Map<String , String>> handleAttendanceAlreadyRecorded(AttendanceAlreadyRecordedException ex){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error" , ex.getMessage() ));
        }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        String error = "The request body is missing or contains malformed JSON. Please provide a valid JSON body.";

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //LectureHasAlreadyEndedException
    @ExceptionHandler(LectureHasAlreadyEndedException.class)
    public ResponseEntity<Map<String, String>> handleAttendanceAlreadyRecorded(LectureHasAlreadyEndedException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error" , ex.getMessage() ));
    }

    @ExceptionHandler(LectureNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLectureNotFound(LectureNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error" , ex.getMessage() ));
    }

    //LectureNotActiveException
    @ExceptionHandler(LectureNotActiveException.class)
    public ResponseEntity<Map<String, String>> handleLectureNotActive (LectureNotActiveException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error" , ex.getMessage() ));
    }

    //AuthorizationException
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Map<String, String>> handleAuthorization (AuthorizationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error" , ex.getMessage() ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {

        System.err.println("Uncaught Server Error: " + ex.getMessage());
        ex.printStackTrace(); // لترى السبب الجذري في الـ Console

        return new ResponseEntity<>("An internal server error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
