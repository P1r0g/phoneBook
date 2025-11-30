package com.example.phonebook.dto;

import com.example.phonebook.models.entities.Department;
import com.example.phonebook.models.enums.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {
    private String username;
    private String password;
    private String confirmPassword;
    private UserRole role;
    private Department department;

    public UserRegistrationDto() {
    }

    @NotEmpty(message = "Имя не должно быть пустым!")
    @Size(min = 2, max = 20, message = "Имя должно быть от 2 до 20 символов!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 5, max = 20, message = "Пароль должен быть от 5 до 20 символов!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @NotEmpty(message = "Подтверждение пароля не должно быть пустым!")
    @Size(min = 5, max = 20, message = "Подтверждение пароля должно быть от 5 до 20 символов!")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    @NotEmpty(message = "Необходимо выбрать роль!")
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
    @NotEmpty(message = "Необходимо указать подразделение!")
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
