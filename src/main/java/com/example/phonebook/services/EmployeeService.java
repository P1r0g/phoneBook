package com.example.phonebook.services;

import java.util.List;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.dto.ShowEmployeeDto;

public interface EmployeeService {
    
    void addEmployee(AddEmployeeDto employeeDto);

    List<ShowEmployeeDto> allEmployees();

    List<ShowEmployeeDto> searchEmployees(String searchTerm);

    void fireEmployee(String employeeFullName);
}
