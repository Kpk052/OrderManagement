package com.dbo.OrderManagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String dashboard(){
        return "Admin Dashboard";
    }
}
