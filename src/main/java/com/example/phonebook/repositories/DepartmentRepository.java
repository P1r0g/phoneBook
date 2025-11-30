package com.example.phonebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.phonebook.models.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    
    // Простой поиск по имени
    Optional<Department> findByShortName(String name);

    // Простой поиск по id
    Optional<Department> findById(Long id);

    // Проверка существования
    boolean existsByName(String name);

}