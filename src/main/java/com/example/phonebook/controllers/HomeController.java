package com.example.phonebook.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage() {
        log.debug("Перевод на страницу сотрудников");
        return "redirect:/employees/all";
    }

    @GetMapping("/info")
    public String infoPage() {
        log.debug("Отображение страницы с информацией о проекте");
        return "index";
    }
    
}
