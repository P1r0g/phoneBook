package com.example.phonebook.services;

import com.example.phonebook.dto.AddEmployeeDto;

public interface EmployeeService {
    
    void addEmployee(AddEmployeeDto employeeDto);

    List<ShowEmployeeInfoDto> allEmployees();

    ShowDetailedEmployeeInfoDto employeeInfo(String employee);

    void fireEmployee(String employeeFullName);
}
