package com.example.phonebook.controllers;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.dto.ShowDepartmentInfoDto;
import com.example.phonebook.dto.UpdateEmployeeDto;
import com.example.phonebook.models.enums.UserRole;
import com.example.phonebook.services.DepartmentService;
import com.example.phonebook.services.EmployeeService;
import com.example.phonebook.services.SecurityService;
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
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final SecurityService securityService;

    public EmployeeController(EmployeeService employeeService,
                              DepartmentService departmentService,
                              SecurityService securityService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.securityService = securityService;
        log.info("EmployeeController инициализирован");
    }

    @GetMapping("/add")
    public String addEmployeeForm(Model model) {
        if (!securityService.canAddEmployee()) {
            log.warn("Попытка добавления сотрудника без прав");
            return "redirect:/employees/all?error=Нет прав для добавления сотрудников";
        }

        if (!model.containsAttribute("employeeModel")) {
            model.addAttribute("employeeModel", new AddEmployeeDto());
        }

        model.addAttribute("departments", departmentService.allDepartments());
        return "employee-add";
    }

    @PostMapping("/add")
    public String addEmployee(
            @Valid @ModelAttribute("employeeModel") AddEmployeeDto employeeModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (!securityService.canAddEmployee()) {
            return "redirect:/employees/all?error=Нет прав для добавления сотрудников";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("employeeModel", employeeModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.employeeModel",
                    bindingResult);
            return "redirect:/employees/add";
        }

        employeeService.addEmployee(employeeModel);
        return "redirect:/employees/all";
    }

    @GetMapping("/all")
    public String showAllEmployees(@RequestParam(required = false) String search,
                                   @RequestParam(required = false) Long department,
                                   @RequestParam(required = false) String error,
                                   @RequestParam(required = false) String success,
                                   Model model) {

        List<ShowDepartmentInfoDto> departments = departmentService.allDepartments();
        model.addAttribute("departments", departments);

        List<com.example.phonebook.dto.ShowEmployeeDto> employees;
        if (search != null && !search.trim().isEmpty()) {
            if (department != null) {
                employees = employeeService.searchEmployeesInDepartment(search, department);
            } else {
                employees = employeeService.searchEmployees(search);
            }
            model.addAttribute("search", search);
            model.addAttribute("selectedDepartment", department);
        } else if (department != null) {
            employees = employeeService.findEmployeesByDepartment(department);
            model.addAttribute("selectedDepartment", department);
        } else {
            employees = employeeService.allEmployees();
        }

        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }

        model.addAttribute("allEmployees", employees);
        return "employee-all";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        com.example.phonebook.dto.SecurityCheckDto securityCheck = securityService.canEditEmployee(id);
        if (!securityCheck.isCanEdit()) {
            log.warn("Попытка редактирования без прав, ID: {}", id);
            return "redirect:/employees/all?error=" + securityCheck.getErrorMessage();
        }

        com.example.phonebook.models.entities.Employee employee = employeeService.findById(id);
        if (employee == null || !employee.IsActive()) {
            return "redirect:/employees/all?error=Сотрудник не найден";
        }

        UpdateEmployeeDto dto = new UpdateEmployeeDto();
        dto.setDepartmentId(employee.getDepartment().getId());
        dto.setOfficeNumber(employee.getOfficeNumber());
        dto.setWorkPhone(employee.getWorkPhone());
        dto.setPersonalPhone(employee.getPersonalPhone());
        dto.setEmail(employee.getEmail());
        dto.setStatusNote(employee.getStatusNote());
        dto.setAdditionalInfo(employee.getAdditionalInfo());

        model.addAttribute("employeeModel", dto);
        model.addAttribute("departments", departmentService.allDepartments());
        model.addAttribute("id", id);

        return "employee-update";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employeeModel") UpdateEmployeeDto employeeDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        com.example.phonebook.dto.SecurityCheckDto securityCheck = securityService.canEditEmployee(id);
        if (!securityCheck.isCanEdit()) {
            redirectAttributes.addFlashAttribute("error", securityCheck.getErrorMessage());
            return "redirect:/employees/all";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("employeeModel", employeeDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.employeeModel",
                    bindingResult);
            return "redirect:/employees/update/" + id;
        }

        employeeService.updateEmployee(id, employeeDto);
        redirectAttributes.addFlashAttribute("success", "Сотрудник успешно обновлен");
        return "redirect:/employees/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        com.example.phonebook.dto.SecurityCheckDto securityCheck = securityService.canDeleteEmployee(id);
        if (!securityCheck.isCanDelete()) {
            redirectAttributes.addFlashAttribute("error", securityCheck.getErrorMessage());
            return "redirect:/employees/all";
        }

        employeeService.deactivateEmployee(id);
        redirectAttributes.addFlashAttribute("success", "Сотрудник деактивирован");
        return "redirect:/employees/all";
    }

    @GetMapping("/inactive")
    public String showInactiveEmployees(Model model) {
        var currentUser = securityService.getCurrentUser();
        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
            return "redirect:/employees/all?error=Только администратор может просматривать неактивных сотрудников";
        }

        var employees = employeeService.allInactiveEmployees();
        model.addAttribute("inactiveEmployees", employees);
        return "employee-inactive";
    }

    @PostMapping("/activate/{id}")
    public String activateEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var currentUser = securityService.getCurrentUser();
        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
            redirectAttributes.addFlashAttribute("error", "Только администратор может активировать сотрудников");
            return "redirect:/employees/all";
        }

        employeeService.activateEmployee(id);
        redirectAttributes.addFlashAttribute("success", "Сотрудник активирован");
        return "redirect:/employees/inactive";
    }
}