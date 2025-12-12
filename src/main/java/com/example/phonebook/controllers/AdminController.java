package com.example.phonebook.controllers;

import com.example.phonebook.services.EmployeeService;
import com.example.phonebook.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final EmployeeService employeeService;
    private final SecurityService securityService;

    public AdminController(EmployeeService employeeService, SecurityService securityService) {
        this.employeeService = employeeService;
        this.securityService = securityService;
    }

    @GetMapping("/employees/inactive")
    public String showInactiveEmployees(Model model) {
        if (!securityService.canManageUsers()) {
            return "redirect:/employees/all?error=Доступ запрещен";
        }

        var employees = employeeService.allInactiveEmployees();
        model.addAttribute("inactiveEmployees", employees);
        return "admin/inactive-employees";
    }

    @PostMapping("/employees/{id}/activate")
    public String activateEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!securityService.canManageUsers()) {
            redirectAttributes.addFlashAttribute("error", "Доступ запрещен");
            return "redirect:/employees/all";
        }

        try {
            employeeService.activateEmployee(id);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно активирован");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/employees/inactive";
    }
}