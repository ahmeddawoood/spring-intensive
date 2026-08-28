package com.ahmed.springintensive.Controller;


import com.ahmed.springintensive.DTO.EmployeeResponse;
import com.ahmed.springintensive.DTO.EmployeeSearchRequest;
import com.ahmed.springintensive.Entity.Employee;
import com.ahmed.springintensive.Mapper.EmployeeMapper;
import com.ahmed.springintensive.Service.EmployeeService;
import com.ahmed.springintensive.DTO.EmployeeRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public Page<EmployeeResponse> getEmployees(Pageable pageable, @ModelAttribute EmployeeSearchRequest searchRequest) {
        return employeeService.getEmployees(pageable, searchRequest).map(employeeMapper::toResponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);

        return employeeMapper.toResponse(employee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public EmployeeResponse addEmployee(@Valid @RequestBody EmployeeRequest request) {

        Employee employee = employeeMapper.toEntity(request);
        Employee savedEmployee = employeeService.addEmployee(employee);

        return employeeMapper.toResponse(savedEmployee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        Employee employee = employeeMapper.toEntity(request);

        Employee updatedEmployee = employeeService.updateEmployee(id, employee);

        return employeeMapper.toResponse(updatedEmployee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
