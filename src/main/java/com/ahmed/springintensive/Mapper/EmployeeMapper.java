package com.ahmed.springintensive.Mapper;

import com.ahmed.springintensive.DTO.EmployeeRequest;
import com.ahmed.springintensive.DTO.EmployeeResponse;
import com.ahmed.springintensive.Entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest request){
        Employee employee = new Employee();


        employee.setName(request.getName());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        return employee;
    }

    public EmployeeResponse toResponse(Employee employee) {

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getDepartment(),
                employee.getSalary()
        );
    }
}
