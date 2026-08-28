package com.ahmed.springintensive.DTO;

import org.springframework.stereotype.Component;

@Component
public class EmployeeResponse {

    private Long id;
    private String name;
    private String department;
    private Double salary;

    public EmployeeResponse(){

    }

    public EmployeeResponse(Long id, String name, String department,Double salary){
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public Double getSalary() {
        return salary;
    }
}
