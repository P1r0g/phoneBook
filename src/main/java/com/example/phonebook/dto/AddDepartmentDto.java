package com.example.phonebook.dto;

import com.example.phonebook.models.enums.DepartmentType;
import jakarta.validation.constraints.NotNull;

public class AddDepartmentDto {
    @NotNull(message = "Короткое имя не должно быть пустым")
    private String shortName;
    @NotNull(message = "Полное имя не должно быть пустым")
    private String fullName;
    @NotNull(message = "Необходимо выбрать тип")
    private DepartmentType type;

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
