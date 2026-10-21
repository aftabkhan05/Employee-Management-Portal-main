package com.hr.repository;

import com.hr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    public Employee findByIdAndPassword(int empId,String password);

    public Employee findByIdAndPasswordAndRole(int empId,String password,String role);

    @Query(value = "SELECT * FROM employee WHERE MONTH(STR_TO_DATE(date_of_birth, '%Y-%m-%d')) = MONTH(CURDATE())", nativeQuery = true)
    List<Employee> findBirthdayThisMonth();



}
