package com.example.phonebook.services;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.phonebook.dto.AddEmployeeDto;
import com.example.phonebook.models.entities.Employee;
import com.example.phonebook.repositories.DepartmentRepository;
import com.example.phonebook.repositories.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl {
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
    employee.setDepartment(departmentRepository.findById(employeeDto.getDepartmentId()));
}
    
}
