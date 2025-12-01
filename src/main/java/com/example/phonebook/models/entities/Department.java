package com.example.phonebook.models.entities;

import com.example.phonebook.models.enums.DepartmentType;
import jakarta.persistence.*;


@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepartmentType type;

    public Department() {
    }

    public String getFullName() {
        return fullName;
    }

    public Long getId() {
        return id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public DepartmentType getType() {
        return type;
    }

    public void setType(DepartmentType type) {
        this.type = type;
    }
}
