package com.ahmed.springintensive.Service;


import com.ahmed.springintensive.DTO.EmployeeSearchRequest;
import com.ahmed.springintensive.Entity.Employee;
import com.ahmed.springintensive.Exception.EmployeeNotFoundException;
import com.ahmed.springintensive.Repository.EmployeeRepository;
import com.ahmed.springintensive.Specification.EmployeeSpecification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> getEmployees(Pageable pageable, EmployeeSearchRequest searchRequest) {
        Specification<Employee> specification = Specification
                .where(EmployeeSpecification.hasDepartment(searchRequest.getDepartment()))
                .and(EmployeeSpecification.nameContains(searchRequest.getName()).
                        and(EmployeeSpecification.salaryGreaterThanOrEqual(searchRequest.getMinSalary())).
                        and(EmployeeSpecification.salaryLessThanOrEqual(searchRequest.getMaxSalary())));

        return employeeRepository.findAll(specification, pageable);
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));


        employee.setName(updatedEmployee.getName());
        employee.setDepartment(updatedEmployee.getDepartment());
        employee.setSalary(updatedEmployee.getSalary());

        return employeeRepository.save(employee);

    }

    public void deleteEmployee(Long id) {

        employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        employeeRepository.deleteById(id);
    }
}
