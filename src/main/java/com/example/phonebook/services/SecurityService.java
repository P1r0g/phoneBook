package com.example.phonebook.services;

import com.example.phonebook.dto.SecurityCheckDto;
import com.example.phonebook.models.entities.Employee;
import com.example.phonebook.models.entities.UserAccount;
import com.example.phonebook.models.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SecurityService {

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

    private final UserService userService;
    private final EmployeeService employeeService;

    public SecurityService(UserService userService, @Lazy EmployeeService employeeService) {
        this.userService = userService;
        this.employeeService = employeeService;
    }

    public UserAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String username = authentication.getName();
        return userService.findUserByUsername(username);
    }

    @Transactional(readOnly = true)
    public SecurityCheckDto canEditEmployee(Long employeeId) {
        UserAccount currentUser = getCurrentUser();

        if (currentUser == null) {
            return SecurityCheckDto.denied("Требуется авторизация");
        }

        Employee employee = employeeService.findById(employeeId);
        if (employee == null || !employee.IsActive()) {
            return SecurityCheckDto.denied("Сотрудник не найден");
        }

        if (currentUser.getRole() == UserRole.ADMIN) {
            return SecurityCheckDto.admin();
        }

        if (currentUser.getRole() == UserRole.MODERATOR) {
            if (currentUser.getDepartment() != null &&
                    employee.getDepartment() != null &&
                    currentUser.getDepartment().getId().equals(employee.getDepartment().getId())) {
                return SecurityCheckDto.moderator();
            }
            return SecurityCheckDto.denied("Модератор может редактировать только сотрудников своего отдела");
        }

        return SecurityCheckDto.denied("Нет прав для редактирования");
    }

    public boolean canAddEmployee() {
        UserAccount currentUser = getCurrentUser();
        return currentUser != null &&
                (currentUser.getRole() == UserRole.MODERATOR ||
                        currentUser.getRole() == UserRole.ADMIN);
    }

    public boolean canManageUsers() {
        UserAccount currentUser = getCurrentUser();
        return currentUser != null && currentUser.getRole() == UserRole.ADMIN;
    }

    public SecurityCheckDto canDeleteEmployee(Long employeeId) {
        return canEditEmployee(employeeId);
    }
}