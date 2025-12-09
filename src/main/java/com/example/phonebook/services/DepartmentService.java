 package com.example.phonebook.services;

 import com.example.phonebook.dto.ShowDepartmentInfoDto;

import java.util.List;
import java.util.Optional;

 public interface DepartmentService {

    Optional<ShowDepartmentInfoDto> getDepartmentById(Long departmentId);
    
    List<ShowDepartmentInfoDto> allDepartments();

    List<ShowDepartmentInfoDto> searchDepartments(String searchTerm);

 }
