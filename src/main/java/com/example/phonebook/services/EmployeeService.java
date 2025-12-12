package com.example.phonebook.services;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.dto.ShowEmployeeDto;
import com.example.phonebook.dto.UpdateEmployeeDto;
import com.example.phonebook.models.entities.Employee;

import java.util.List;

public interface EmployeeService {
    void addEmployee(AddEmployeeDto employeeDto);
    List<ShowEmployeeDto> allEmployees();
    List<ShowEmployeeDto> searchEmployees(String searchTerm);
    Employee findById(Long id);
    List<ShowEmployeeDto> findEmployeesByDepartment(Long departmentId);
    void updateEmployee(Long id, UpdateEmployeeDto dto);
    List<ShowEmployeeDto> searchEmployeesInDepartment(String searchTerm, Long departmentId);
    void deactivateEmployee(Long id);
    List<ShowEmployeeDto> allInactiveEmployees();
    void activateEmployee(Long id);
}