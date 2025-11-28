package com.example.phonebook.models.enums;

public enum UserRole {
    ADMIN(1), MODERATOR(2);

    private int value;

    UserRole (int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
