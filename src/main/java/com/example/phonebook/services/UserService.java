package com.example.phonebook.services;

import java.util.List;

import com.example.phonebook.dto.ShowUserDto;
import com.example.phonebook.dto.UpdateUserDto;
import com.example.phonebook.models.entities.UserAccount;

public interface UserService {

    List<ShowUserDto> allUsers();

    List<ShowUserDto> searchUsers(String searchTerm);

    List<ShowUserDto> searchUsersInDepartment(String searchTerm, Long departmentId);

    void updateUser(Long id, UpdateUserDto dto);

    public UserAccount findUserById(Long id);
    
    
}
