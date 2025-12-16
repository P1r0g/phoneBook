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
    @Query("SELECT e FROM Employee AS e LEFT JOIN FETCH e.department WHERE CONCAT(e.lastName, ' ', e.firstName, ' ', e.middleName) = :fullName")
    Employee findEmployeeByFullName(String fullName);

    @Modifying
    @Transactional
    @Query("DELETE FROM Employee AS e WHERE CONCAT(e.lastName, ' ', e.firstName, ' ', e.middleName) = :fullName")
    void deleteEmployeeByFullName(String fullName);

    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.officeNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.workPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.personalPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.statusNote) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.additionalInfo) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "ORDER BY e.lastName ASC, e.firstName ASC")
    List<Employee> searchEmployees(@Param("searchTerm") String searchTerm);

     @Query("SELECT e FROM Employee e ORDER BY e.lastName ASC, e.firstName ASC")
    List<Employee> findAllOrderedByLastName();

    @Query("SELECT e FROM Employee e WHERE " +
       "e.department.id = :departmentId AND (" +
       "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(e.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(e.officeNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(e.workPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(e.personalPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(e.statusNote) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(e.additionalInfo) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
       "ORDER BY e.lastName ASC, e.firstName ASC")
    List<Employee> searchEmployeesInDepartment( @Param("searchTerm") String searchTerm, @Param("departmentId") Long departmentId);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Employee e SET 
        e.department.id = :departmentId,
        e.officeNumber = :officeNumber,
        e.workPhone = :workPhone,
        e.personalPhone = :personalPhone,
        e.email = :email,
        e.statusNote = :statusNote,
        e.additionalInfo = :additionalInfo
    WHERE CONCAT(e.lastName, ' ', e.firstName, ' ', e.middleName) = :fullName
""")
    void updateEmployeeByFullName(
            @Param("fullName") String fullName,
            @Param("departmentId") Long departmentId,
            @Param("officeNumber") String officeNumber,
            @Param("workPhone") String workPhone,
            @Param("personalPhone") String personalPhone,
            @Param("email") String email,
            @Param("statusNote") String statusNote,
            @Param("additionalInfo") String additionalInfo
    );

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId ORDER BY e.lastName ASC, e.firstName ASC")
    List<Employee> findByDepartmentId(@Param("departmentId") Long departmentId);

}
