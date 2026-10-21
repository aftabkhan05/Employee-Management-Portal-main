package com.hr.service;

import com.hr.entity.CreatePost;
import com.hr.entity.Employee;
import com.hr.repository.CreatePostRepo;
import com.hr.repository.EmployeeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HrServiceTest {     // Unit testing ( alag alg test krna each method )

    @Mock
    private EmployeeRepo employeeRepo;


    @Mock
    private CreatePostRepo createPostRepo;

    @InjectMocks
    private HrService hrService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testAddEmployee() {

        Employee employee = new Employee(); // Creating dummy object (test input)
        employee.setId(1);
        employee.setEmployeeName("Aftab");
        employee.setEmail("ak@gmail.com");

        when(employeeRepo.save(employee)).thenReturn(employee);
        Employee saveEmployee = hrService.addEmployee(employee);

        verify(employeeRepo).save(employee);

        assertEquals(employee, saveEmployee);
//        assertEquals(employee,new Employee()); // emppty object

    }

    @Test
    void testGetAllEmployee() {

        List<Employee> employeeList = new ArrayList<>();
        Employee employee = new Employee();
        employee.setId(1);
        employee.setEmployeeName("Khan");
        employee.setEmail("aak@gmail.com");

        Employee employee1 = new Employee();
        employee.setId(2);
        employee.setEmployeeName("Arhaan");
        employee.setEmail("arh@gmail.com");

        employeeList.add(employee);
        employeeList.add(employee1);

        when(employeeRepo.findAll()).thenReturn(employeeList);

        List<Employee> result = hrService.getAllEmployee();

        assertEquals(employeeList, result);


    }
    @Test
    void testaddPost(){
        CreatePost createPost=new CreatePost();
        createPost.setId(1);
        createPost.setTitle("Application for leave");
        createPost.setComment("I want 3 days leave");

        when(createPostRepo.save(createPost)).thenReturn(createPost);
        CreatePost savepost=hrService.addPost(createPost);
        verify(createPostRepo).save(createPost);
        assertEquals(createPost, savepost);




    }
}



