package com.example.phonebook.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.dto.ShowEmployeeDto;
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

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, ModelMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.mapper = mapper;
        log.info("EmployeeServiceImpl инициализорован");
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "employees", allEntries = true)
    public void addEmployee(AddEmployeeDto employeeDto) {
        log.debug("Добавление нового сотрудника: {} {} {}", employeeDto.getLastName(), employeeDto.getMiddleName(), employeeDto.getFirstName());

        Employee employee = mapper.map(employeeDto, Employee.class);
        employee.setDepartment(departmentRepository.findById(employeeDto.getDepartment()).orElse(null));

        employeeRepository.saveAndFlush(employee);
        log.info("Сотрудник успешно добавлен: {} {} {}", employeeDto.getLastName(), employeeDto.getMiddleName(), employeeDto.getFirstName());
    }

    @Override
    @Cacheable(value = "employees", key = "'all'")
    public List<ShowEmployeeDto> allEmployees() {
        log.debug("Получение списка всех сотрудников");
        List<ShowEmployeeDto> employees = employeeRepository.findAll().stream()
                .map(employee -> mapper.map(employee, ShowEmployeeDto.class))
                .collect(Collectors.toList());
        log.debug("Найдено сотрудников: {}", employees.size());
        return employees;
        }
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
}   