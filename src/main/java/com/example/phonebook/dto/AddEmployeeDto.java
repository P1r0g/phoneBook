package com.example.phonebook.dto;

import com.example.phonebook.models.enums.DepartmentType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class AddEmployeeDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private DepartmentType departmentType;
    private String officeNumber;
    private String workPhone;
    private String personalPhone;
    private String email;
    private String statusNote;
    private String additionalInfo;


    @NotEmpty(message = "Имя не должно быть пустым!")
    @Size(min = 2, message = "Имя должно содержать не менее 2 символов!")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @NotEmpty(message = "Фамилия не должна быть пустой!")
    @Size(min = 2, message = "Фамилия должна содержать не менее 2 символов!")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @Size(min = 2, message = "Отчество должно содержать не менее 2 символов!")
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @NotNull(message = "Выберите подразделение!")
    public DepartmentType getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(DepartmentType departmentType) {
        this.departmentType = departmentType;
    }
    @NotEmpty(message = "Номер кабинета не может быть пустым")
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
