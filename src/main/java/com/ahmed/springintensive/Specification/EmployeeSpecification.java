package com.ahmed.springintensive.Specification;

import com.ahmed.springintensive.Entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {
    public static Specification<Employee> hasDepartment(String department) {
        return (root, query, criteriaBuilder) -> {
            if (department == null || department.isBlank()) {
                return null;
            }

            return criteriaBuilder.equal(root.get("department"), department);
        };
    }

    public static Specification<Employee> nameContains(String name) {

        return (root, query, criteriaBuilder) -> {

            if (name == null || name.isBlank()) {
                return null;
            }

            return criteriaBuilder.like(
                    root.get("name"),
                    "%" + name + "%"
            );
        };
    }

    public static Specification<Employee> salaryGreaterThanOrEqual(Double minSalary) {

        return (root, query, criteriaBuilder) -> {

            if (minSalary == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("salary"),
                    minSalary
            );
        };
    }

    public static Specification<Employee> salaryLessThanOrEqual(Double maxSalary) {

        return (root, query, criteriaBuilder) -> {

            if (maxSalary == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("salary"),
                    maxSalary
            );
        };
    }
}
