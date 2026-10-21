package com.hr.repository;

import com.hr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface employeeRepository extends JpaRepository<Employee,Integer> {
//    @Query("SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department")
//    List<Object[]> countByDepartment();
    @Query("SELECT e.department, COUNT(e) FROM Employee e " +
            "WHERE e.department IN ('Development', 'QA & Automation Testing', 'Analyst', 'HR Team', 'Security', 'Sales & Marketing') " +
            "GROUP BY e.department")
    List<Object[]> countByDepartment();

    @Query("SELECT e FROM Employee e WHERE " +
            "(:department IS NULL OR e.department = :department) AND " +
            "(:minSalary IS NULL OR e.salary >= :minSalary) AND " +
            "(:maxSalary IS NULL OR e.salary <= :maxSalary)")
        List<Employee> filterByDeptAndSalary(
            @Param("department") String department,
            @Param("minSalary") Integer minSalary,
            @Param("maxSalary") Integer maxSalary);
}
