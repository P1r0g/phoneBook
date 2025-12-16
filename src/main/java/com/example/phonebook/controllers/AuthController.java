package com.example.phonebook.controllers;

import com.example.phonebook.dto.UserRegistrationDto;
import com.example.phonebook.models.entities.UserAccount;
import com.example.phonebook.services.AuthService;
import com.example.phonebook.services.DepartmentService;
import com.example.phonebook.view.UserProfileView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Slf4j
@Controller
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;
    private final DepartmentService departmentService;

    public AuthController(AuthService authService, DepartmentService departmentService) {
        this.authService = authService;
        this.departmentService = departmentService;
        log.info("AuthController инициализирован");
    }

    @ModelAttribute("userRegistrationDto")
    public UserRegistrationDto initForm() {
        return new UserRegistrationDto();
    }

    @GetMapping("/register")
    public String register(Model model) {
        log.debug("Отображение страницы создания пользователя");
        model.addAttribute("departments", departmentService.allDepartments());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid UserRegistrationDto userRegistrationDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.debug("Обработка создания пользователя: {}", userRegistrationDto.getUsername());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при создании пользователя: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("userRegistrationDto", userRegistrationDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);

            return "redirect:/users/register";
        }

        this.authService.register(userRegistrationDto);
        log.info("Пользователь успешно зарегистрирован: {}", userRegistrationDto.getUsername());

        return "redirect:/moderators/all";
    }

    @GetMapping("/login")
    public String login() {
        log.debug("Отображение страницы входа");
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {

        log.warn("Неудачная попытка входа для пользователя: {}", username);
        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("badCredentials", true);

        return "redirect:/users/login";
    }


    @GetMapping("/profile")
    @Transactional
    public String profile(Principal principal, Model model) {
        String username = principal.getName();
        log.debug("Отображение профиля пользователя: {}", username);

        UserAccount user = authService.getUser(username);

        UserProfileView userProfileView = new UserProfileView(
                username,
                user.getRole()
        );

        userProfileView.setDepartmentName(user.getDepartment().getFullName());
        model.addAttribute("user", userProfileView);

        return "profile";
    }
}
