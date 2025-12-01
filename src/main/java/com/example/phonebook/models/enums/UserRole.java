package com.example.phonebook.models.enums;

public enum UserRole {
    USER(1), MODERATOR(2), ADMIN(3);

    private int value;

    UserRole (int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
