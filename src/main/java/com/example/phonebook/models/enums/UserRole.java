package com.example.phonebook.models.enums;

public enum UserRole {
    USER(1), ADMIN(2), MODERATOR(3);

    private int value;

    UserRole (int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
