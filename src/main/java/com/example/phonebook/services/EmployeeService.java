package com.example.phonebook.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.dto.ShowEmployeeDto;
import com.example.phonebook.dto.UpdateEmployeeDto;
import com.example.phonebook.models.entities.Employee;
import com.example.phonebook.models.entities.UserAccount;

public interface EmployeeService {
    
    void addEmployee(AddEmployeeDto employeeDto);

    List<ShowEmployeeDto> allEmployees();

    Page<ShowEmployeeDto> allEmployeesPaginated(Pageable pageable);

    List<ShowEmployeeDto> searchEmployees(String searchTerm);

    Employee findByFullName(String fullName);
    
    List<ShowEmployeeDto> findEmployeesByDepartment(Long departmentId);

    void updateEmployee(String fullName, UpdateEmployeeDto dto);
    
    List<ShowEmployeeDto> searchEmployeesInDepartment(String searchTerm, Long departmentId);

    void fireEmployee(String employeeFullName);

    UserAccount getCurrentUser();

    Long getCurrentUserDepartmentId();

    boolean isCurrentUserAdmin();

    boolean isCurrentUserModerator();

    boolean canCurrentUserEditEmployee(Long employeeId);

    boolean canCurrentUserEditEmployee(String employeeFullName);
}
