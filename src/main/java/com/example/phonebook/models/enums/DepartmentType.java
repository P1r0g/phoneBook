package com.example.phonebook.models.enums;

public enum DepartmentType {
    DEPARTMENT(1), ADMINISTRATION(2), OFFICE(3);

    private int value;

    DepartmentType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
