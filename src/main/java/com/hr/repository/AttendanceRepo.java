package com.hr.repository;

import com.hr.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Integer> {

    //  Get all attendance for today
    List<Attendance> findByAttendanceDate(LocalDate date);

    // Get attendance by employee and month
    @Query("SELECT a FROM Attendance a WHERE a.employeeId = :empId AND MONTH(a.attendanceDate) = :month AND YEAR(a.attendanceDate) = :year")
    List<Attendance> findByEmployeeAndMonth(int empId, int month, int year);

    // Check if already marked today
    Attendance findByEmployeeIdAndAttendanceDate(Integer employeeId, LocalDate date);

    // Count present days in month
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.employeeId = :empId AND a.status = 'PRESENT' AND MONTH(a.attendanceDate) = :month AND YEAR(a.attendanceDate) = :year")
    Long countPresentDays(int empId, int month, int year);

    //  Count absent days in month
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.employeeId = :empId AND a.status = 'ABSENT' AND MONTH(a.attendanceDate) = :month AND YEAR(a.attendanceDate) = :year")
    Long countAbsentDays(int empId, int month, int year);

    //  Get monthly report for all employees
    @Query("SELECT a FROM Attendance a WHERE MONTH(a.attendanceDate) = :month AND YEAR(a.attendanceDate) = :year")
    List<Attendance> findByMonth(int month, int year);
}