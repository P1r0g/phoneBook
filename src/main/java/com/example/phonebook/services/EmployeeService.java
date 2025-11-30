package com.example.phonebook.services;

import java.util.List;

import com.example.phonebook.dto.AddEmployeeDto;

public interface EmployeeService {
    
    void addEmployee(AddEmployeeDto employeeDto);

    List<ShowEmployeeInfoDto> allEmployees();

    ShowDetailedEmployeeInfoDto employeeInfo(String employeeFullName);

    void fireEmployee(String employeeFullName);
}
