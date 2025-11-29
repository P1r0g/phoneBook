package com.example.phonebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    
    // Простой поиск по имени
    Optional<Department> findByName(String name);

    // Проверка существования
    boolean existsByName(String name);

    // Поиск с условием и сортировкой
    List<Department> findByBudgetGreaterThanOrderByBudgetDesc(Double minBudget);

    // Custom Query для полнотекстового поиска
    @Query("SELECT c FROM Company c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Department> searchByNameOrDescription(@Param("searchTerm") String searchTerm);

}