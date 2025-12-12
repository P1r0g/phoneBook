package com.example.phonebook.services;

import com.example.phonebook.dto.ShowUserDto;
import com.example.phonebook.dto.UpdateUserDto;
import com.example.phonebook.models.entities.UserAccount;
import com.example.phonebook.repositories.DepartmentRepository;
import com.example.phonebook.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository,
                           DepartmentRepository departmentRepository,
                           ModelMapper mapper) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.mapper = mapper;
        log.info("UserServiceImpl инициализирован");
    }

    @Override
    public List<ShowUserDto> allNonAdminUsers() {
        log.debug("Получение списка всех пользователей кроме администраторов");
        List<UserAccount> users = userRepository.findAllExceptAdmin();

        return users.stream()
                .map(user -> {
                    ShowUserDto dto = mapper.map(user, ShowUserDto.class);
                    if (user.getDepartment() != null) {
                        dto.setDepartmentShortName(user.getDepartment().getShortName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void updateUser(Long id, UpdateUserDto dto) {
        log.debug("Обновление пользователя с ID: {}", id);

        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());

        if (dto.getDepartmentId() != null) {
            departmentRepository.findById(dto.getDepartmentId())
                    .ifPresent(user::setDepartment);
        } else {
            user.setDepartment(null);
        }

        userRepository.save(user);
        log.info("Пользователь обновлен: ID={}, username={}", id, dto.getUsername());
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public UserAccount findUserById(Long id) {
        log.debug("Поиск пользователя по ID: {}", id);
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserAccount findUserByUsername(String username) {
        log.debug("Поиск пользователя по username: {}", username);
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void deleteUserById(Long id) {
        log.debug("Удаление пользователя по ID: {}", id);

        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        userRepository.delete(user);
        log.info("Пользователь удален: ID={}, username={}", id, user.getUsername());
    }
}