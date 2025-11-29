package com.example.phonebook.dto;

import com.example.phonebook.models.entities.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateEmployeeDto {
    private Department department;
    private String officeNumber;
    private String workPhone;
    private String personalPhone;
    private String email;
    private String statusNote;
    private String additionalInfo;

    @NotNull(message = "Выберите подразделение!")
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    @NotEmpty(message = "Номер кабинета не может быть пустым")
    @Size(min = 3, message = "Номер кабинета должен быть не менее 3 символов")
    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }
    @NotEmpty(message = "Рабочий телефон не должен быть пустым")
    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }
    @NotEmpty(message = "номер телефона не должен быть пустым")
    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }
    @Email
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
