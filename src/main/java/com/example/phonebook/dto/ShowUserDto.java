package com.example.phonebook.dto;

import java.io.Serializable;

import com.example.phonebook.models.enums.UserRole;

public class ShowUserDto implements Serializable {
    private Long id;
    private String username;
    private UserRole role;
    private Long departmentId;
    private String departmentShortName;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }
    public String getDepartmentShortName() {
        return departmentShortName;
    }
    public void setDepartmentShortName(String departmentShortName) {
        this.departmentShortName = departmentShortName;
    }
    
        
}
