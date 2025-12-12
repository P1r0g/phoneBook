package com.example.phonebook.services;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.dto.ShowEmployeeDto;
import com.example.phonebook.dto.UpdateEmployeeDto;
import com.example.phonebook.models.entities.Department;
import com.example.phonebook.models.entities.Employee;
import com.example.phonebook.models.entities.UserAccount;
import com.example.phonebook.repositories.DepartmentRepository;
import com.example.phonebook.repositories.EmployeeRepository;
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
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper mapper;
    private final SecurityService securityService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository,
                               ModelMapper mapper,
                               SecurityService securityService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.mapper = mapper;
        this.securityService = securityService;
        log.info("EmployeeServiceImpl инициализирован");
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
    public void addEmployee(AddEmployeeDto employeeDto) {
        log.debug("Добавление нового сотрудника: {} {}", employeeDto.getLastName(), employeeDto.getFirstName());

        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Отдел не найден"));

        Employee employee = mapper.map(employeeDto, Employee.class);
        employee.setDepartment(department);
        employee.setActive(true);

        employeeRepository.save(employee);
        log.info("Сотрудник добавлен: {} {}", employeeDto.getLastName(), employeeDto.getFirstName());
    }

    @Override
    @Cacheable(value = "employees", key = "'all'")
    public List<ShowEmployeeDto> allEmployees() {
        log.debug("Получение списка всех активных сотрудников");
        List<Employee> employees = employeeRepository.findAllActiveOrdered();

        UserAccount currentUser = securityService.getCurrentUser();
        return employees.stream()
                .map(employee -> {
                    ShowEmployeeDto dto = mapper.map(employee, ShowEmployeeDto.class);
                    dto.setDepartmentShortName(employee.getDepartment().getShortName());
                    dto.setActive(employee.IsActive());

                    // Проверка прав
                    if (currentUser != null) {
                        com.example.phonebook.dto.SecurityCheckDto securityCheck = securityService.canEditEmployee(employee.getId());
                        dto.setCanEdit(securityCheck.isCanEdit());
                        dto.setCanDelete(securityCheck.isCanDelete());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowEmployeeDto> findEmployeesByDepartment(Long departmentId) {
        log.debug("Получение сотрудников отдела: {}", departmentId);
        List<Employee> employees = employeeRepository.findActiveByDepartmentId(departmentId);

        UserAccount currentUser = securityService.getCurrentUser();
        return employees.stream()
                .map(employee -> {
                    ShowEmployeeDto dto = mapper.map(employee, ShowEmployeeDto.class);
                    dto.setDepartmentShortName(employee.getDepartment().getShortName());
                    dto.setActive(employee.IsActive());

                    if (currentUser != null) {
                        com.example.phonebook.dto.SecurityCheckDto securityCheck = securityService.canEditEmployee(employee.getId());
                        dto.setCanEdit(securityCheck.isCanEdit());
                        dto.setCanDelete(securityCheck.isCanDelete());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
    public void deactivateEmployee(Long id) {
        log.debug("Деактивация сотрудника с ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));

        if (!employee.IsActive()) {
            throw new RuntimeException("Сотрудник уже деактивирован");
        }

        employeeRepository.deactivateById(id);
        log.info("Сотрудник деактивирован: ID={}", id);
    }

    @Override
    public List<ShowEmployeeDto> searchEmployeesInDepartment(String searchTerm, Long departmentId) {
        log.debug("Поиск сотрудников в отделе {}: {}", departmentId, searchTerm);
        List<Employee> employees = employeeRepository.searchActiveEmployeesInDepartment(searchTerm, departmentId);

        UserAccount currentUser = securityService.getCurrentUser();
        return employees.stream()
                .map(employee -> {
                    ShowEmployeeDto dto = mapper.map(employee, ShowEmployeeDto.class);
                    dto.setDepartmentShortName(employee.getDepartment().getShortName());
                    dto.setActive(employee.IsActive());

                    if (currentUser != null) {
                        com.example.phonebook.dto.SecurityCheckDto securityCheck = securityService.canEditEmployee(employee.getId());
                        dto.setCanEdit(securityCheck.isCanEdit());
                        dto.setCanDelete(securityCheck.isCanDelete());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowEmployeeDto> searchEmployees(String searchTerm) {
        log.debug("Поиск сотрудников: {}", searchTerm);
        List<Employee> employees = employeeRepository.searchActiveEmployees(searchTerm);

        UserAccount currentUser = securityService.getCurrentUser();
        return employees.stream()
                .map(employee -> {
                    ShowEmployeeDto dto = mapper.map(employee, ShowEmployeeDto.class);
                    dto.setDepartmentShortName(employee.getDepartment().getShortName());
                    dto.setActive(employee.IsActive());

                    if (currentUser != null) {
                        com.example.phonebook.dto.SecurityCheckDto securityCheck = securityService.canEditEmployee(employee.getId());
                        dto.setCanEdit(securityCheck.isCanEdit());
                        dto.setCanDelete(securityCheck.isCanDelete());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Employee findById(Long id) {
        log.debug("Поиск сотрудника по ID: {}", id);
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
    public void updateEmployee(Long id, UpdateEmployeeDto dto) {
        log.debug("Обновление сотрудника с ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));

        if (!employee.IsActive()) {
            throw new RuntimeException("Нельзя обновить деактивированного сотрудника");
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Отдел не найден"));

        employee.setDepartment(department);
        employee.setOfficeNumber(dto.getOfficeNumber());
        employee.setWorkPhone(dto.getWorkPhone());
        employee.setPersonalPhone(dto.getPersonalPhone());
        employee.setEmail(dto.getEmail());
        employee.setStatusNote(dto.getStatusNote());
        employee.setAdditionalInfo(dto.getAdditionalInfo());

        employeeRepository.save(employee);
        log.info("Сотрудник обновлен: ID={}", id);
    }

    @Override
    @Cacheable(value = "inactiveEmployees", key = "'all'")
    public List<ShowEmployeeDto> allInactiveEmployees() {
        log.debug("Получение списка неактивных сотрудников");
        List<Employee> employees = employeeRepository.findAllInactive();

        return employees.stream()
                .map(employee -> {
                    ShowEmployeeDto dto = mapper.map(employee, ShowEmployeeDto.class);
                    dto.setDepartmentShortName(employee.getDepartment().getShortName());
                    dto.setActive(false);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"employees", "inactiveEmployees"}, allEntries = true)
    public void activateEmployee(Long id) {
        log.debug("Активация сотрудника с ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));

        if (employee.IsActive()) {
            throw new RuntimeException("Сотрудник уже активен");
        }

        employee.setActive(true);
        employeeRepository.save(employee);
        log.info("Сотрудник активирован: ID={}", id);
    }
}