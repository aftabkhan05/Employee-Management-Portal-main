

package com.hr.repository;

import com.hr.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PayrollRepo extends JpaRepository<Payroll, Integer> {

    // Get payroll by employee
    List<Payroll> findByEmployeeId(Integer employeeId);

    //  Get payroll by month and year
    List<Payroll> findByMonthAndYear(String month, Integer year);

    // Check if payroll already generated
    @Query("SELECT p FROM Payroll p WHERE p.employeeId = :empId " +
            "AND p.month = :month AND p.year = :year")
    Payroll findByEmployeeAndMonth(
            @Param("empId") int empId,
            @Param("month") String month,
            @Param("year") int year);
}