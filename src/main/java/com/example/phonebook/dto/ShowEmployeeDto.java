package com.example.phonebook.dto;

import com.example.phonebook.models.enums.DepartmentType;

import java.io.Serializable;

public class ShowEmployeeDto implements Serializable {
    private String firstName;
    private String lastName;
    private String middleName;
    private String officeNumber;
    private String workPhone;
    private String personalPhone;
    private String email;
    private String statusNote;
    private String additionalInfo;
    private String departmentShortName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public String getDepartmentShortName() {
        return departmentShortName;
    }
    
    public void setDepartmentShortName(String departmentShortName) {
        this.departmentShortName = departmentShortName;
    }
}
