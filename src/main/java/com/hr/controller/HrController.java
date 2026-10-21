package com.hr.controller;


import com.hr.entity.*;
import com.hr.repository.*;
import com.hr.service.HrService;
import com.hr.service.employeeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Controller
public class HrController {

    @Autowired
     private HrService service;

    @Autowired
    private EmployeeRepo employeeRepo;

   @Autowired
    private ComposeRepo composeRepo;



    @Autowired
    private CreatePostRepo createPostRepo;

    @Autowired
    private employeeService employeeService;

    @Autowired
    private AttendanceRepo attendanceRepo;

//    @Autowired
//    private ChatRepo chatRepo;
@Autowired
private PayrollRepo payrollRepo;


    @GetMapping("/login")
    public String login() {


        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgetPassword() {
        return "forgot-password";
    }

    @GetMapping("/home")
    public String home(@RequestParam("username") String username, @RequestParam("password") String password, Model model,HttpSession session) {

        try {
            String empId = username.substring(3);
            Employee employee = employeeRepo.findByIdAndPassword(Integer.parseInt(empId), password);
            if (employee != null) {
                model.addAttribute("error", false);
                session.setAttribute("userId", Integer.parseInt(empId));
                session.setAttribute("name", employee.getEmployeeName());
                session.setAttribute("desg", employee.getDesignation());


                if (employee.getRole().equals("USER")) {
                    session.setAttribute("role", "USER");
                    // Add here for USER
//                    session.setAttribute("role", "USER");
                    //  Add this — load profile image on login
                    if (employee.getProfileImage() != null) {
                        session.setAttribute("profileImage",
                                employee.getProfileImage());
                    }
                    return "redirect:/user-dashboard";

                }
                else if (employee.getRole().equals("ADMIN")) {
//                    session.setAttribute("role", "ADMIN");
//                    session.setAttribute("adminId", Integer.parseInt(empId));
                    // Add this — load profile image on login
                    if (employee.getProfileImage() != null) {
                        session.setAttribute("profileImage", employee.getProfileImage());
                    }
                    List<Compose> findAll=composeRepo.findAll();
                    findAll.forEach(k -> {
                        int id = k.getParentUkid();

                        String designation = employeeRepo.findById(id)
                                .map(Employee::getDesignation)
                                .orElse("Not Found");   // 

                        k.setPosition(designation);
                    });
                    model.addAttribute("statusList",findAll);
                    model.addAttribute("desgCounts", employeeService.getDepartmentCount());
                    model.addAttribute("statusCounts", getStatusCountMap());
                    //  Add only this line
                    model.addAttribute("birthdays", employeeRepo.findBirthdayThisMonth());
                    return "dash-board";
                }
            else {
                return "redirect:/login";
            }
        }else {
                model.addAttribute("error",true);
                return "login";
            }
        } catch (NumberFormatException e) {
//            e.printStackTrace();
             System.err.println(e.getMessage());
            return "redirect:/login";

        }

    }


    @GetMapping("/dash-board")
    public String dashBoard(Model model) {
        model.addAttribute("desgCounts", employeeService.getDepartmentCount());

        model.addAttribute("statusCounts", getStatusCountMap());

        model.addAttribute("birthdays", employeeRepo.findBirthdayThisMonth());
        List<Compose> findAll = composeRepo.findAll();
        findAll.forEach(k -> {
            int id = k.getParentUkid();
            String designation = employeeRepo.findById(id)
                    .map(Employee::getDesignation)
                    .orElse("Not Found");
            k.setPosition(designation);
        });
        model.addAttribute("statusList", findAll);  

        return "dash-board";
    }

    @GetMapping("/add-employee")
    public String addEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        return "add-employee";
    }

          @GetMapping("/all-employee")
           public String allEmployee(Model model,
                          @RequestParam(required = false) Integer minSalary,
                          @RequestParam(required = false) Integer maxSalary) {

    List<Employee> allEmployee;

    if (minSalary != null && maxSalary != null) {
        allEmployee = service.filterByDeptAndSalary(null, minSalary, maxSalary);
    } else {
        allEmployee = service.getAllEmployee();
    }

    model.addAttribute("allEmployee", allEmployee);
    model.addAttribute("minSalary", minSalary);
    model.addAttribute("maxSalary", maxSalary);
    return "all-employee";
}

    @GetMapping("/create-post")
    public String createPost(Model model){
        List<CreatePost> findAll= createPostRepo.findAll();
        model.addAttribute("post",findAll);
        return "create-post";

    }

    @GetMapping({"/status"})
    public String status(Model model){
        List<Compose> findAll=composeRepo.findAll();

        findAll.forEach(k -> {
            int id = k.getParentUkid();

            String designation = employeeRepo.findById(id)
                    .map(Employee::getDesignation)
                    .orElse("Not Found");   

            k.setPosition(designation);
        });
        model.addAttribute("statusList",findAll);

        return "status";
    }

    @GetMapping({"/my-profile"})
    public String myProfile(HttpSession session,Model model){
        Object attribute=session.getAttribute("userId");
        int userId=Integer.parseInt(attribute.toString());

        Employee employee = employeeRepo.findById(userId).get();
        model.addAttribute("employee",employee);
        if (employee.getProfileImage() != null) {
            session.setAttribute("profileImage", employee.getProfileImage());
        }

        return "my-profile";
    }

    @GetMapping({"/setting"})
    public String setting(){

        return "setting";
    }

    @PostMapping("/save-employee")
    public String saveEmployee(@Valid @ModelAttribute Employee employee , BindingResult result,Model model){
       if(result.hasErrors()){
           model.addAttribute("employee",employee);

           return "add-employee";
       }

        employee.setPassword(employee.getDateOfBirth());

        Employee save=service.addEmployee(employee);
        return "redirect:/dash-board";
    }

     @PostMapping("/save-post")
    public String savePost(@ModelAttribute CreatePost createPost ){
        createPost.setAddedDate(LocalDateTime.now().toString());
        CreatePost addPost=service.addPost(createPost);
        return "redirect:/create-post";
    }
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("password") String password,@RequestParam("newPassword1") String newPassword1,@RequestParam("newPassword2") String newPassword2,HttpSession session,Model model) {
        Object attribute = session.getAttribute("userId");
        int userId = Integer.parseInt(attribute.toString());

        Employee employee = employeeRepo.findByIdAndPassword(userId, password);

        if(employee != null && newPassword1.equals(newPassword2)) {
            employee.setPassword(newPassword2);
            employeeRepo.save(employee);
            model.addAttribute("error", false);
        } else {
            model.addAttribute("error", true);
            return "setting";
        }
        return "redirect:/login";
    }

    @GetMapping("/edit-record")
    public String editRecord(@RequestParam("id") int id,Model model){
        Employee employee=employeeRepo.findById(id).get();
        model.addAttribute("employee",employee);
        return "edit-record";
    }
    @PostMapping("/edit-employee")
    public String updateRecord(@ModelAttribute Employee employee){

        int id=employee.getId();
        Employee getEmp=employeeRepo.findById(id).get();
        if(getEmp!=null){
            employeeRepo.save(employee);
        }
        return "redirect:/all-employee";
    }
    @GetMapping("/deleteRecord-byId")
    public String deleteRecordById(@RequestParam("id") int id){
        employeeRepo.deleteById(id);
        return "redirect:/all-employee";

    }
    @GetMapping("/user-dashboard")
    public String userDashBoard(Model model,HttpSession session){
        model.addAttribute("birthdays", employeeRepo.findBirthdayThisMonth());
        model.addAttribute("desgCounts", employeeService.getDepartmentCount());

        model.addAttribute("statusCounts", getStatusCountMap());


        Object attribute = session.getAttribute("userId");
        int userId = Integer.parseInt(attribute.toString());
       // Add this — refresh profile image in session
        Employee employee = employeeRepo.findById(userId).get();
        if (employee.getProfileImage() != null) {
            session.setAttribute("profileImage",
                    employee.getProfileImage());
        }
        List<Compose> findAll=composeRepo.findByParentUkid(userId);

        findAll.forEach(k -> {
            int id = k.getParentUkid();

            String designation = employeeRepo.findById(id)
                    .map(Employee::getDesignation)
                    .orElse("Not Found");   // 👈 safe fallback

            k.setPosition(designation);
        });
        model.addAttribute("statusList", findAll);


        return "user-dash-board";
    }
    @GetMapping({"/user-profile"})
    public String userProfile(HttpSession session, Model model) {
        Object attribute = session.getAttribute("userId");
        int userId = Integer.parseInt(attribute.toString());

        Employee employee = employeeRepo.findById(userId).get();
        model.addAttribute("employee", employee);

        //  Set profile image in session
        if (employee.getProfileImage() != null) {
            session.setAttribute("profileImage", employee.getProfileImage());
        }

        return "user-profile";
    }
    @GetMapping("/user-setting")
    public String userSetting(){

        return "user-setting";
    }

    @GetMapping("/user-compose")
    public String compose(){

        return "user-compose";
    }
    @PostMapping("/compose")
    public String addCompose(@RequestParam("subject") String subject,@RequestParam("text") String text,HttpSession session ){
        try {
            Object attribute = session.getAttribute("userId");
            int userId = Integer.parseInt(attribute.toString());
            Employee employee=employeeRepo.findById(userId).get();

            Compose com=new Compose();
            com.setEmpName(employee.getEmployeeName());
            com.setSubject(subject);
            com.setText(text);
            com.setParentUkid(userId);
            com.setAddedDate(new Date().toString());
            com.setStatus("PENDING");
            Compose save=composeRepo.save(com);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "redirect:/user-compose";
    }
   @GetMapping("/approve-byId")
    public String approve(@RequestParam("id") int id,@RequestParam("type") String type){
    Compose compose=composeRepo.findById(id).get();
    compose.setStatus(type);
    composeRepo.save(compose);
    return "redirect:/status";


    }
    // Helper method to reuse in multiple mappings
    private Map<String, Long> getStatusCountMap() {
        Map<String, Long> statusCount = new LinkedHashMap<>();
        statusCount.put("Pending",   composeRepo.countPending());
        statusCount.put("Approved",  composeRepo.countApproved());
        statusCount.put("Cancelled", composeRepo.countCancelled());
        statusCount.put("Denied",    composeRepo.countDenied());
        statusCount.put("All",       composeRepo.countAll());

        return statusCount;
    }

    //  REST endpoint for AJAX call
    @GetMapping("/status-count")
    @ResponseBody
    public Map<String, Long> getStatusCount() {
        return getStatusCountMap();
    }

    //  REST endpoint for department AJAX call
    @GetMapping("/department-count")
    @ResponseBody
    public List<Object[]> getDepartmentCount() {
        return employeeService.getDepartmentCount();
    }

    @PostMapping("/upload-profile-image")
    public String uploadImage(@RequestParam("image") MultipartFile file, HttpSession session) {
        try {
            Object attribute = session.getAttribute("userId");
            int userId = Integer.parseInt(attribute.toString());

            String uploadDir = System.getProperty("user.home") + "/employee-images/";
            File folder = new File(uploadDir);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = "profile_" + userId + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());

            Employee employee = employeeRepo.findById(userId).get();
            employee.setProfileImage(fileName);
            employeeRepo.save(employee);

            //  Update session immediately after upload
            session.setAttribute("profileImage", fileName);
            String role = (String) session.getAttribute("role");
            if ("ADMIN".equals(role)) {
                return "redirect:/my-profile";
            } else {
                return "redirect:/user-profile"; //  User goes to user-profile
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/my-profile";
    }


    //  Show Attendance Marking Page (Admin)
    @GetMapping("/attendance")
    public String attendance(Model model) {
        List<Employee> allEmployee = service.getAllEmployee();
        model.addAttribute("allEmployee", allEmployee);
        model.addAttribute("today", LocalDate.now().toString());

        // Check already marked today
        List<Attendance> todayAttendance = attendanceRepo.findByAttendanceDate(LocalDate.now());
        model.addAttribute("todayAttendance", todayAttendance);

        return "attendance";
    }

    //  Save Attendance
    @PostMapping("/save-attendance")
    public String saveAttendance(
            @RequestParam("employeeId") int employeeId,
            @RequestParam("status") String status) {

        // Check if already marked today
        Attendance existing = attendanceRepo.findByEmployeeIdAndAttendanceDate(
                employeeId, LocalDate.now());

        Employee employee = employeeRepo.findById(employeeId).get();

        if (existing != null) {
            // Update existing
            existing.setStatus(status);
            attendanceRepo.save(existing);
        } else {
            // Create new
            Attendance attendance = new Attendance();
            attendance.setEmployeeId(employeeId);
            attendance.setEmployeeName(employee.getEmployeeName());
            attendance.setDepartment(employee.getDepartment());
            attendance.setStatus(status);
            attendance.setAttendanceDate(LocalDate.now());
            attendanceRepo.save(attendance);
        }

        return "redirect:/attendance";
    }

    //  Monthly Report Page
    @GetMapping("/attendance-report")
    public String attendanceReport(
            @RequestParam(value = "month", defaultValue = "0") int month,
            @RequestParam(value = "year", defaultValue = "0") int year,
            Model model) {

        if (month == 0) month = LocalDate.now().getMonthValue();
        if (year == 0) year = LocalDate.now().getYear();

        List<Attendance> report = attendanceRepo.findByMonth(month, year);
        model.addAttribute("report", report);
        model.addAttribute("month", month);
        model.addAttribute("year", year);

        // Count summary
        long totalPresent = report.stream().filter(a -> a.getStatus().equals("PRESENT")).count();
        long totalAbsent  = report.stream().filter(a -> a.getStatus().equals("ABSENT")).count();
        long totalHalfDay = report.stream().filter(a -> a.getStatus().equals("HALF_DAY")).count();
        long totalLeave   = report.stream().filter(a -> a.getStatus().equals("LEAVE")).count();

        model.addAttribute("totalPresent", totalPresent);
        model.addAttribute("totalAbsent",  totalAbsent);
        model.addAttribute("totalHalfDay", totalHalfDay);
        model.addAttribute("totalLeave",   totalLeave);

        return "attendance-report";
    }



    //  Admin Payroll Page
    @GetMapping("/payroll")
    public String payroll(Model model) {
        List<Employee> allEmployees = service.getAllEmployee();
        model.addAttribute("allEmployees", allEmployees);

        // Current month and year
        String currentMonth = LocalDate.now().getMonth().toString();
        int currentYear = LocalDate.now().getYear();
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("currentYear", currentYear);

        // Get generated payrolls for this month
        List<Payroll> payrolls = payrollRepo.findByMonthAndYear(
                currentMonth, currentYear);
        model.addAttribute("payrolls", payrolls);

        return "payroll";
    }

    //  Generate Salary Slip
    @PostMapping("/generate-payroll")
    public String generatePayroll(
            @RequestParam("employeeId") int employeeId,
            @RequestParam("month") String month,
            @RequestParam("year") int year) {

        Employee employee = employeeRepo.findById(employeeId).get();

        // Check if already generated
        Payroll existing = payrollRepo.findByEmployeeAndMonth(
                employeeId, month, year);
        if (existing != null) {
            return "redirect:/payroll";
        }

        //  Calculate salary components
        Double basic = Double.parseDouble(employee.getSalary());
        Double hra          = basic * 0.40;  // 40%
        Double allowance    = basic * 0.20;  // 20%
        Double gross        = basic + hra + allowance;
        Double pf           = basic * 0.12;  // 12%
        Double tax          = basic * 0.10;  // 10%
        Double totalDeduct  = pf + tax;
        Double net          = gross - totalDeduct;

        Payroll payroll = new Payroll();
        payroll.setEmployeeId(employeeId);
        payroll.setEmployeeName(employee.getEmployeeName());
        payroll.setDepartment(employee.getDepartment());
        payroll.setDesignation(employee.getDesignation());
        payroll.setBasicSalary(basic);
        payroll.setHra(hra);
        payroll.setAllowance(allowance);
        payroll.setGrossSalary(gross);
        payroll.setPfDeduction(pf);
        payroll.setTaxDeduction(tax);
        payroll.setTotalDeduction(totalDeduct);
        payroll.setNetSalary(net);
        payroll.setMonth(month);
        payroll.setYear(year);
        payroll.setStatus("GENERATED");

        payrollRepo.save(payroll);
        return "redirect:/payroll";
    }

    //  View Salary Slip
    @GetMapping("/salary-slip")
    public String salarySlip(
            @RequestParam("id") int id,
            Model model) {
        Payroll payroll = payrollRepo.findById(id).get();
        model.addAttribute("payroll", payroll);
        return "salary-slip";
    }

    // Mark as Paid
    @GetMapping("/mark-paid")
    public String markPaid(@RequestParam("id") int id) {
        Payroll payroll = payrollRepo.findById(id).get();
        payroll.setStatus("PAID");
        payrollRepo.save(payroll);
        return "redirect:/payroll";
    }

    //  User View Own Payroll
    @GetMapping("/user-payroll")
    public String userPayroll(Model model, HttpSession session) {
        Object attribute = session.getAttribute("userId");
        int userId = Integer.parseInt(attribute.toString());

        List<Payroll> myPayrolls = payrollRepo.findByEmployeeId(userId);
        model.addAttribute("myPayrolls", myPayrolls);

        return "user-payroll";
    }

    //  User View Own Salary Slip
    //  Fix — return user-salary-slip instead of salary-slip
    @GetMapping("/user-salary-slip")
    public String userSalarySlip(
            @RequestParam("id") int id,
            Model model,
            HttpSession session) {
        Object attribute = session.getAttribute("userId");
        int userId = Integer.parseInt(attribute.toString());

        Payroll payroll = payrollRepo.findById(id).get();

        //  Security — only own slip
        if (payroll.getEmployeeId() != userId) {
            return "redirect:/user-payroll";
        }

        model.addAttribute("payroll", payroll);
        return "user-salary-slip"; // Changed from salary-slip
    }

    @GetMapping("/user-attendance")
    public String userAttendance(Model model, HttpSession session,
                                 @RequestParam(value = "month", defaultValue = "0") int month,
                                 @RequestParam(value = "year", defaultValue = "0") int year) {

        Object attribute = session.getAttribute("userId");
        int userId = Integer.parseInt(attribute.toString());

        if (month == 0) month = LocalDate.now().getMonthValue();
        if (year == 0)  year  = LocalDate.now().getYear();

        //  Get only this user's attendance
        List<Attendance> myAttendance = attendanceRepo
                .findByEmployeeAndMonth(userId, month, year);

        //  Count summary
        long present  = myAttendance.stream()
                .filter(a -> a.getStatus().equals("PRESENT")).count();
        long absent   = myAttendance.stream()
                .filter(a -> a.getStatus().equals("ABSENT")).count();
        long halfDay  = myAttendance.stream()
                .filter(a -> a.getStatus().equals("HALF_DAY")).count();
        long leave    = myAttendance.stream()
                .filter(a -> a.getStatus().equals("LEAVE")).count();

        model.addAttribute("myAttendance", myAttendance);
        model.addAttribute("present",  present);
        model.addAttribute("absent",   absent);
        model.addAttribute("halfDay",  halfDay);
        model.addAttribute("leave",    leave);
        model.addAttribute("month",    month);
        model.addAttribute("year",     year);

        return "user-attendance";
    }







}
