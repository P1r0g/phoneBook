package com.example.phonebook.dto;

import com.example.phonebook.models.enums.DepartmentType;

public class UpdateDepartmentDto {
    private Long id;
    private String shortName;
    private String fullName;
    private DepartmentType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public DepartmentType getType() {
        return type;
    }

    public void setType(DepartmentType type) {
        this.type = type;
    }
}
