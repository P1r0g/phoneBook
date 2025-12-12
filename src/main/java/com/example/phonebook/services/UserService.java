package com.example.phonebook.services;

import com.example.phonebook.dto.ShowUserDto;
import com.example.phonebook.dto.UpdateUserDto;
import com.example.phonebook.models.entities.UserAccount;

import java.util.List;

public interface UserService {
    List<ShowUserDto> allNonAdminUsers();
    void updateUser(Long id, UpdateUserDto dto);
    UserAccount findUserById(Long id);
    UserAccount findUserByUsername(String username);
    void deleteUserById(Long id);
}