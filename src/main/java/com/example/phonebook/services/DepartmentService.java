 package com.example.phonebook.services;

 import com.example.phonebook.dto.ShowDepartmentInfoDto;

 import java.util.List;

 public interface DepartmentService {

     List<ShowDepartmentInfoDto> allDepartments();

     List<ShowDepartmentInfoDto> searchDepartments(String searchTerm);

 }
