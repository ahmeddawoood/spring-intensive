package com.ahmed.springintensive.Repository;


import com.ahmed.springintensive.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>,
        JpaSpecificationExecutor<Employee>{


}
