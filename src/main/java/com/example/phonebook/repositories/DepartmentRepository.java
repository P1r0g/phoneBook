package com.example.phonebook.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.phonebook.models.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Простой поиск по имени
    List<Department> findByShortName(String shortName);

    // Проверка существования
    boolean existsByShortName(String shortName);
}