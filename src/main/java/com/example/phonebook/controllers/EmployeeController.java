package com.example.phonebook.controllers;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.dto.UpdateEmployeeDto;
import com.example.phonebook.models.entities.Employee;
import com.example.phonebook.services.DepartmentService;
import com.example.phonebook.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        log.info("EmployeeController инициализирован");
    }

    // -------------------------------------------
    //                ADD EMPLOYEE
    // -------------------------------------------

    @GetMapping("/add")
    public String addEmployeeForm(Model model) {
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
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("employeeModel", employeeModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.employeeModel",
                    bindingResult
            );
            return "redirect:/employees/add";
        }

        employeeService.addEmployee(employeeModel);
        return "redirect:/employees/all";
    }

    // -------------------------------------------
    //             SHOW ALL EMPLOYEES
    // -------------------------------------------

    @GetMapping("/all")
    public String showAllEmployees(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("allEmployees", employeeService.searchEmployees(search));
        } else {
            model.addAttribute("allEmployees", employeeService.allEmployees());
        }
        
        model.addAttribute("search", search);
        return "employee-all";
    }

    // -------------------------------------------
    //              DELETE EMPLOYEE
    // -------------------------------------------

    @DeleteMapping("/employee-delete/{employee-full-name}")
    public String fireEmployee(@PathVariable("employee-full-name") String employeeFullName) {
        employeeService.fireEmployee(employeeFullName);
        return "redirect:/employees/all";
    }

    // -------------------------------------------
    //             UPDATE EMPLOYEE (FORM)
    // -------------------------------------------

    @GetMapping("/update/{fullName}")
    public String showUpdateForm(
            @PathVariable String fullName,
            Model model
    ) {
        Employee employee = employeeService.findByFullName(fullName);

        if (employee == null) {
            return "redirect:/employees/all";
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
        model.addAttribute("fullName", fullName);

        return "employee-update";
    }

    // -------------------------------------------
    //           UPDATE EMPLOYEE (SUBMIT)
    // -------------------------------------------

    @PostMapping("/update/{fullName}")
    public String updateEmployee(
            @PathVariable String fullName,
            @Valid @ModelAttribute("employeeModel") UpdateEmployeeDto employeeDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("employeeModel", employeeDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.employeeModel",
                    bindingResult
            );
            return "redirect:/employees/update/" + fullName;
        }

        employeeService.updateEmployee(fullName, employeeDto);

        return "redirect:/employees/all";
    }
}
