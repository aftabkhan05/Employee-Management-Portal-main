package com.hr.service;

import com.hr.entity.CreatePost;
import com.hr.entity.Employee;
import com.hr.repository.CreatePostRepo;
import com.hr.repository.EmployeeRepo;
import com.hr.repository.employeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HrService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private CreatePostRepo createPostRepo;

    @Autowired
    private employeeRepository repo;


    public Employee addEmployee(Employee employee){

        Employee save= employeeRepo.save(employee);
        return save;
    }

    public List<Employee> getAllEmployee(){
        List<Employee> findAll = employeeRepo.findAll();
        return findAll;
    }
    public CreatePost addPost(CreatePost createPost){
        CreatePost save=createPostRepo.save(createPost);
       return save;
    }
    public List<Employee> filterByDeptAndSalary(String department, Integer minSalary, Integer maxSalary) {
        return repo.filterByDeptAndSalary(department, minSalary, maxSalary);
    }
}


