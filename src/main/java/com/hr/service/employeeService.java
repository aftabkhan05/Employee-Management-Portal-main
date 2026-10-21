package com.hr.service;

import com.hr.entity.Employee;
import com.hr.repository.employeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class employeeService {
    @Autowired
    employeeRepository employeeRepository;

    public List<Object[]> getDepartmentCount() {
        return employeeRepository.countByDepartment();
    }

    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }


}


//    public List<Employee> filterByDeptAndSalary(String department, Integer minSalary, Integer maxSalary) {
//        return employeeRepository.filterByDeptAndSalary(department, minSalary, maxSalary);
//    }
//    }

