package com.example.phonebook.dto;

public class SecurityCheckDto {
    private boolean canEdit = false;
    private boolean canDelete = false;
    private String errorMessage;

    public SecurityCheckDto() {
    }

    public static SecurityCheckDto admin() {
        SecurityCheckDto dto = new SecurityCheckDto();
        dto.setCanEdit(true);
        dto.setCanDelete(true);
        return dto;
    }

    public static SecurityCheckDto moderator() {
        SecurityCheckDto dto = new SecurityCheckDto();
        dto.setCanEdit(true);
        dto.setCanDelete(true);
        return dto;
    }

    public static SecurityCheckDto denied(String message) {
        SecurityCheckDto dto = new SecurityCheckDto();
        dto.setErrorMessage(message);
        return dto;
    }

    // Геттеры и сеттеры
    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}