package com.example.phonebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.phonebook.models.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{
@Query("SELECT e FROM Employee AS e LEFT JOIN FETCH e.department_id WHERE CONCAT(e.last_name, ' ', e.first_name, ' ', e.middle_name) = :fullName")
    Employee findEmployeeByFullName(String fullName);

@Modifying
    @Transactional
    @Query("DELETE FROM Employee AS e WHERE CONCAT(e.last_name, ' ', e.middle_name, ' ', e.first_name) = :fullName")
    void deleteEmployeeByFullName(String fullName);
}
