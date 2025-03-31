package com.ezen.matzip.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/main")
    public String adminMain() {
        return "domain/admin/admin_main";
    }
}
