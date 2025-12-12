package com.example.phonebook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateEmployeeDto {
    @NotNull(message = "Выберите подразделение!")
    private Long departmentId;

    @NotEmpty(message = "Номер кабинета не может быть пустым")
    @Size(min = 3, message = "Кабинет — минимум 3 символа")
    private String officeNumber;

    @NotEmpty(message = "Рабочий телефон обязателен")
    private String workPhone;

    @NotEmpty(message = "Личный телефон обязателен")
    private String personalPhone;

    @Email(message = "Некорректный email")
    @NotEmpty(message = "Email обязателен")
    private String email;

    private String statusNote;
    private String additionalInfo;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatusNote() {
        return statusNote;
    }

    public void setStatusNote(String statusNote) {
        this.statusNote = statusNote;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}