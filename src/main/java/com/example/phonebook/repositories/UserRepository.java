package com.example.phonebook.repositories;

import com.example.phonebook.models.entities.UserAccount;
import com.example.phonebook.models.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);

    @Query("SELECT u FROM UserAccount u ORDER BY u.username ASC")
    List<UserAccount> findAllOrdered();

    @Modifying
    @Transactional
    @Query("UPDATE UserAccount u SET " +
            "u.username = :username, " +
            "u.department.id = :departmentId, " +
            "u.role = :role " +
            "WHERE u.id = :id")
    void updateUserById(
            @Param("id") Long id,
            @Param("username") String username,
            @Param("departmentId") Long departmentId,
            @Param("role") UserRole role
    );

    @Query("""
    SELECT u FROM UserAccount u
    WHERE u.role <> com.example.phonebook.models.enums.UserRole.ADMIN
    ORDER BY u.username ASC
    """)
    List<UserAccount> findAllExceptAdmin();

    @Modifying
    @Transactional
    @Query("DELETE FROM UserAccount u WHERE u.id = :id")
    void deleteUserById(@Param("id") Long id);
}