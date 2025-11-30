package com.example.phonebook.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.phonebook.models.entities.Department;
import com.example.phonebook.repositories.DepartmentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ModelMapper mapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ModelMapper mapper) {
        this.departmentRepository = departmentRepository;
        this.mapper = mapper;
        log.info("DepartmentServiceImpl инициализирован");
    }

    @Override
    @Cacheable(value = "departments", key = "'all'")
    public List<ShowDepartmentInfoDto> allDepartments() {
        log.debug("Получение списка всех отделов");
        List<ShowDepartmentInfoDto> departments = departmentRepository.findAll().stream()
                .map(department -> mapper.map(department, ShowDepartmentInfoDto.class))
                .collect(Collectors.toList());
        log.info("Найдено отделов: {}", departments.size());
        return departments;
    }

    @Override
    public List<ShowDepartmentInfoDto> searchDepartments(String searchTerm) {
        log.debug("Поиск кафедр по запросу: {}", searchTerm);
        List<ShowDepartmentInfoDto> results = departmentRepository.searchByShortName(searchTerm).stream()
                .map(department -> mapper.map(department, ShowDepartmentInfoDto.class))
                .collect(Collectors.toList());
        log.info("По запросу '{}' найдено отделов: {}", searchTerm, results.size());
        return results;
    }
    
}
