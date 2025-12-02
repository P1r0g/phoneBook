package com.example.phonebook.view;

import com.example.phonebook.models.enums.UserRole;

public class UserProfileView {
    private String userName;
    private UserRole role;
    private String departmentName;

    public UserProfileView(String userName, UserRole role) {
        this.userName = userName;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
