package com.example.phonebook.services;

import com.example.phonebook.dto.ShowDepartmentInfoDto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.phonebook.repositories.DepartmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

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
        List<ShowDepartmentInfoDto> results = departmentRepository.findByShortName(searchTerm).stream()
                .map(department -> mapper.map(department, ShowDepartmentInfoDto.class))
                .collect(Collectors.toList());
        log.info("По запросу '{}' найдено отделов: {}", searchTerm, results.size());
        return results;
    }

    @Override
    public Optional<ShowDepartmentInfoDto> getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(dept -> mapper.map(dept, ShowDepartmentInfoDto.class));
    }
}