package com.example.phonebook.services;

import com.example.phonebook.dto.UserRegistrationDto;
import com.example.phonebook.models.entities.UserAccount;
import com.example.phonebook.models.enums.UserRole;
import com.example.phonebook.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(UserRegistrationDto registrationDTO) {
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new RuntimeException("passwords.match");
        }

        var userRole = UserRole.MODERATOR;

        UserAccount user = new UserAccount(
                registrationDTO.getUsername(),
                passwordEncoder.encode(registrationDTO.getPassword())
        );

        user.setRole(userRole);

        userRepository.save(user);
    }

    @Override
    public UserAccount getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " was not found!"));
    }
}
