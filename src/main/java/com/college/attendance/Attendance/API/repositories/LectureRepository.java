package com.college.attendance.Attendance.API.repositories;

import com.college.attendance.Attendance.API.entities.Lecture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture , Long> {
    List<Lecture> findAllByDoctorId(Long doctorId);

    @EntityGraph(attributePaths = "attendanceRecords")
    Optional<Lecture> findById(Long lectureId);
}
