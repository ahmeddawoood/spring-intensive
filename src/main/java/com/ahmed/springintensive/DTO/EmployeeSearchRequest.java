package com.ahmed.springintensive.DTO;

public class EmployeeSearchRequest {

    private String department;
    private String name;
    private Double minSalary;
    private Double maxSalary;



    public  EmployeeSearchRequest(){

    }

    public EmployeeSearchRequest(String department,String name,Double minSalary,Double maxSalary){

        this.department = department;
        this.name = name;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }
}
