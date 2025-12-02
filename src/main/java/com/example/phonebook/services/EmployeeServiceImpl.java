package com.example.phonebook.services;

import java.util.List;
import java.util.stream.Collectors;

import com.example.phonebook.dto.UpdateEmployeeDto;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.dto.ShowEmployeeDto;
import com.example.phonebook.models.entities.Department;
import com.example.phonebook.models.entities.Employee;
import com.example.phonebook.repositories.DepartmentRepository;
import com.example.phonebook.repositories.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper mapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, ModelMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.mapper = mapper;
        log.info("EmployeeServiceImpl инициализорован");
    }

    private ShowEmployeeDto convertToShowEmployeeDtoWithDepartment(Employee employee) {
        ShowEmployeeDto dto = mapper.map(employee, ShowEmployeeDto.class);
        
        // Добавляем shortName из Department
        if (employee.getDepartment() != null) {
            dto.setDepartmentShortName(employee.getDepartment().getShortName());
        }
        
        return dto;
    }
    
    @Override
    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
    public void addEmployee(AddEmployeeDto employeeDto) {
        log.debug("Добавление нового сотрудника: {} {} {}", employeeDto.getLastName(), employeeDto.getFirstName(), employeeDto.getMiddleName());

        Department department = departmentRepository.findById(employeeDto.getDepartmentId()).orElse(null);
        if (department == null) {
            log.warn("Попытка добавить сотрудника в несуществующий отдел");
            //throw new EmployeeNotFoundException("Сотрудник с именем '" + employeeFullName + "' не найден");
        }
        Employee employee = mapper.map(employeeDto, Employee.class);
        employee.setDepartment(department);

        employeeRepository.saveAndFlush(employee);
        log.info("Сотрудник успешно добавлен: {} {} {}", employeeDto.getLastName(), employeeDto.getMiddleName(), employeeDto.getFirstName());
    }

    @Override
    @Cacheable(value = "employees", key = "'all'")
    public List<ShowEmployeeDto> allEmployees() {
        log.debug("Получение списка всех сотрудников");
        List<ShowEmployeeDto> employees = employeeRepository.findAllOrderedByLastName().stream()
                .map(employee -> mapper.map(employee, ShowEmployeeDto.class))
                .collect(Collectors.toList());
        log.debug("Найдено сотрудников: {}", employees.size());
        return employees;
        }


    @Override
    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
    public void fireEmployee(String employeeFullName) {
        log.debug("Увольнение сотрудника: {}", employeeFullName);

        Employee employee = employeeRepository.findEmployeeByFullName(employeeFullName);
        if (employee == null) {
            log.warn("Попытка уволить несуществующего сотрудника: {}", employeeFullName);
            //throw new EmployeeNotFoundException("Сотрудник с именем '" + employeeFullName + "' не найден");
        }

        employeeRepository.deleteEmployeeByFullName(employeeFullName);
        log.info("Сотрудник уволен: {}", employeeFullName);
    }

    @Override
    public List<ShowEmployeeDto> searchEmployees(String searchTerm) {
        log.debug("Поиск сотрудников по запросу: {}", searchTerm);
        List<ShowEmployeeDto> results = employeeRepository.searchEmployees(searchTerm).stream()
                .map(employee -> mapper.map(employee, ShowEmployeeDto.class))
                .collect(Collectors.toList());
        log.info("По запросу '{}' найдено сотрудников: {}", searchTerm, results.size());        
        return results;
    }

    @Override
    public Employee findByFullName(String fullName) {
        return employeeRepository.findEmployeeByFullName(fullName);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
    public void updateEmployee(String fullName, UpdateEmployeeDto dto) {

        employeeRepository.updateEmployeeByFullName(
                fullName,
                dto.getDepartmentId(),
                dto.getOfficeNumber(),
                dto.getWorkPhone(),
                dto.getPersonalPhone(),
                dto.getEmail(),
                dto.getStatusNote(),
                dto.getAdditionalInfo()
        );

        log.info("Сотрудник обновлён: {}", fullName);
    }



}