package com.hr.controller;

import com.hr.entity.Employee;
import com.hr.repository.ComposeRepo;
import com.hr.repository.CreatePostRepo;
import com.hr.repository.EmployeeRepo;
import com.hr.service.HrService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static java.lang.reflect.Array.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(HrController.class)
public class HrControllerTest {

    @Autowired
    private MockMvc mockMvc;          // Spring manages this

    @MockitoBean
    private HrService service;

    @MockitoBean
    private CreatePostRepo createPostRepo;

    @MockitoBean
    private EmployeeRepo employeeRepo;

    @MockitoBean
    private ComposeRepo composeRepo;

    @Test
    public void testHome() throws Exception {

        Employee employee = new Employee();
        employee.setId(101);
        employee.setEmployeeName("Khan");
        employee.setDesignation("Developer");
        employee.setRole("USER");

        when(employeeRepo.findByIdAndPassword(101, "pass123")).thenReturn(employee);

        mockMvc.perform(get("/home")
                        .param("username", "HMP101")       // must match what controller parses
                        .param("password", "pass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user-dashboard"));
    }
    @Test
    public void testSavePost() throws Exception{

        mockMvc.perform(post("/save-post")
                .param("title","New Title")
                .param("comment","This is a comment")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/create-post"));
    }
}

