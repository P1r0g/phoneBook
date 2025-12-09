package com.example.phonebook.services;

import java.util.List;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.dto.ShowEmployeeDto;
import com.example.phonebook.dto.UpdateEmployeeDto;
import com.example.phonebook.models.entities.Employee;

public interface EmployeeService {
    
    void addEmployee(AddEmployeeDto employeeDto);

    List<ShowEmployeeDto> allEmployees();

    List<ShowEmployeeDto> searchEmployees(String searchTerm);

    Employee findByFullName(String fullName);
    
    List<ShowEmployeeDto> findEmployeesByDepartment(Long departmentId);

    void updateEmployee(String fullName, UpdateEmployeeDto dto);
    
    List<ShowEmployeeDto> searchEmployeesInDepartment(String searchTerm, Long departmentId);

    void fireEmployee(String employeeFullName);
}
