package com.example.phonebook.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.phonebook.models.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.isActive = true ORDER BY e.lastName ASC, e.firstName ASC")
    List<Employee> findAllActiveOrdered();

    @Query("SELECT e FROM Employee AS e WHERE e.id = :id AND e.isActive = true")
    Optional<Employee> findEmployeeById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.isActive = false WHERE e.id = :id")
    void deactivateById(@Param("id") Long id);

    @Query("SELECT e FROM Employee e WHERE e.isActive = true AND " +
            "(LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.officeNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.workPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.personalPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.statusNote) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.additionalInfo) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY e.lastName ASC, e.firstName ASC")
    List<Employee> searchActiveEmployees(@Param("searchTerm") String searchTerm);


    @Query("SELECT e FROM Employee e WHERE e.isActive = true AND e.department.id = :departmentId AND " +
            "(LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.officeNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.workPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.personalPhone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.statusNote) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.additionalInfo) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY e.lastName ASC, e.firstName ASC")
    List<Employee> searchActiveEmployeesInDepartment(@Param("searchTerm") String searchTerm,
                                                     @Param("departmentId") Long departmentId);

    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET " +
            "e.firstName =:firstName, " +
            "e.lastName =:lastName, " +
            "e.middleName =:middleName, " +
            "e.department.id=:departmentId," +
            "e.officeNumber =:officeNumber, " +
            "e.workPhone =:workPhone, " +
            "e.personalPhone =:personalPhone, " +
            "e.email =:email, " +
            "e.statusNote =:statusNote, " +
            "e.additionalInfo =:additionalInfo " +
            "WHERE e.id =:id")
    void updateEmployeeById(
            @Param("id") Long id,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("middleName") String middleName,
            @Param("departmentId") Long departmentId,
            @Param("officeNumber") String officeNumber,
            @Param("workPhone") String workPhone,
            @Param("personalPhone") String personalPhone,
            @Param("email") String email,
            @Param("statusNote") String statusNote,
            @Param("additionalInfo") String additionalInfo
    );

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND e.isActive = true ORDER BY e.lastName ASC, e.firstName ASC")
    List<Employee> findByDepartmentId(@Param("departmentId") Long departmentId);

}
