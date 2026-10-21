
package com.hr.entity;

import jakarta.persistence.*;
        import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer employeeId;
    private String employeeName;
    private String department;
    private String designation;

    private Double basicSalary;
    private Double hra;           // 40% of basic
    private Double allowance;     // 20% of basic
    private Double grossSalary;   // basic + hra + allowance

    private Double pfDeduction;   // 12% of basic
    private Double taxDeduction;  // 10% of basic
    private Double totalDeduction;

    private Double netSalary;     // gross - deductions

    private String month;
    private Integer year;
    private String status;        // GENERATED, PAID

    @CreationTimestamp
    private LocalDateTime createdDate;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getHra() { return hra; }
    public void setHra(Double hra) { this.hra = hra; }

    public Double getAllowance() { return allowance; }
    public void setAllowance(Double allowance) { this.allowance = allowance; }

    public Double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(Double grossSalary) { this.grossSalary = grossSalary; }

    public Double getPfDeduction() { return pfDeduction; }
    public void setPfDeduction(Double pfDeduction) { this.pfDeduction = pfDeduction; }

    public Double getTaxDeduction() { return taxDeduction; }
    public void setTaxDeduction(Double taxDeduction) { this.taxDeduction = taxDeduction; }

    public Double getTotalDeduction() { return totalDeduction; }
    public void setTotalDeduction(Double totalDeduction) { this.totalDeduction = totalDeduction; }

    public Double getNetSalary() { return netSalary; }
    public void setNetSalary(Double netSalary) { this.netSalary = netSalary; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}