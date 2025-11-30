package com.example.phonebook.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.phonebook.models.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    @Query("SELECT e FROM Employee AS e LEFT JOIN FETCH e.department_id WHERE CONCAT(e.last_name, ' ', e.first_name, ' ', e.middle_name) = :fullName")
    Employee findEmployeeByFullName(String fullName);

    @Modifying
    @Transactional
    @Query("DELETE FROM Employee AS e WHERE CONCAT(e.last_name, ' ', e.middle_name, ' ', e.first_name) = :fullName")
    void deleteEmployeeByFullName(String fullName);

    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.lastname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.firstname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.officeNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.workPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.personalPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.emailaddress) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.statusNote) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.additionalInfo) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> searchEmployees(@Param("searchTerm") String searchTerm);
}
