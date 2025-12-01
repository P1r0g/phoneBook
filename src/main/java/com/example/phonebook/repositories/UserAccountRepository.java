package com.example.phonebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.phonebook.models.entities.UserAccount;
import com.example.phonebook.models.enums.UserRole;

import java.util.Optional;


@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    Optional<UserRole> findRoleByName()    
}