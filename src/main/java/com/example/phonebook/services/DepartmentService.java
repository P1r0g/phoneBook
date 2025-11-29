package com.example.phonebook.services;

import java.util.List;

public interface DepartmentService {

    List<ShowDepartmentInfoDto> allDepartments();

    List<ShowDepartmentInfoDto> searchDepartments(String searchTerm);

    ShowDetailedDepartmentInfoDto departmentDetails(String department);
    
}
