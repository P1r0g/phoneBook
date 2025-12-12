package com.example.phonebook.controllers;

import com.example.phonebook.dto.ShowDepartmentInfoDto;
import com.example.phonebook.dto.ShowUserDto;
import com.example.phonebook.dto.UpdateUserDto;
import com.example.phonebook.models.entities.UserAccount;
import com.example.phonebook.services.DepartmentService;
import com.example.phonebook.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/moderators")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final DepartmentService departmentService;

    public UserController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
        log.info("UserController заупущен");
    }

    @GetMapping("/all")
    public String showAllUsers(@RequestParam(required = false) String search,
                               @RequestParam(required = false) Long department,
                               Model model) {

        List<ShowDepartmentInfoDto> departments = departmentService.allDepartments();
        model.addAttribute("departments", departments);

        if (search != null && !search.trim().isEmpty()) {
            List<ShowUserDto> users = userService.allNonAdminUsers();

            List<ShowUserDto> filtered = users.stream()
                    .filter(u ->
                            (u.getUsername() != null &&
                                    u.getUsername().toLowerCase().contains(search.toLowerCase()))
                                    ||
                                    (u.getRole() != null &&
                                            u.getRole().toString().toLowerCase().contains(search.toLowerCase()))
                    )
                    .toList();

            model.addAttribute("allUsers", filtered);
            model.addAttribute("search", search);
            return "moderators-all";
        }

        if (department != null) {
            List<ShowUserDto> users = userService.allNonAdminUsers();

            List<ShowUserDto> filtered = users.stream()
                    .filter(u -> u.getDepartmentId() != null &&
                            u.getDepartmentId().equals(department))
                    .toList();

            model.addAttribute("allUsers", filtered);
            model.addAttribute("selectedDepartment", department);
            return "moderators-all";
        }

        model.addAttribute("allUsers", userService.allNonAdminUsers());
        return "moderators-all";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        UserAccount user = userService.findUserById(id);

        if(user == null) {
            return "redirect:/moderators/all";
        }

        UpdateUserDto dto = new UpdateUserDto();
        dto.setDepartmentId(user.getDepartment() != null ? user.getDepartment().getId() : null);
        dto.setRole(user.getRole());
        dto.setUsername(user.getUsername());

        model.addAttribute("userModel", dto);
        model.addAttribute("departments", departmentService.allDepartments());
        model.addAttribute("id", id);

        return "moderators-update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("employeeModel") UpdateUserDto userDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userModel", userDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.employeeModel",
                    bindingResult);
            return "redirect:/moderators/update/" + id;
        }
        userService.updateUser(id, userDto);
        return "redirect:/moderators/all";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return "redirect:/moderators/all";
    }
}