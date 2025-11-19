package com.college.attendance.Attendance.API.repositories;

import com.college.attendance.Attendance.API.entities.AttendanceRecord;
import com.college.attendance.Attendance.API.entities.AttendanceStatus;
import com.college.attendance.Attendance.API.entities.Lecture;
import com.college.attendance.Attendance.API.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findAllByLectureId(Long lectureId);

    List<AttendanceRecord> findAllByStudentId(Long studentId);

    boolean existsByStudentAndLecture(User student, Lecture lecture);

    Optional<AttendanceRecord> findByStudentIdAndLectureId(Long studentId, Long lectureId);

    @Query("SELECT COUNT(ar) FROM AttendanceRecord ar WHERE ar.student.id = :studentId AND ar.status = :status")
    long countByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") AttendanceStatus status);
}
