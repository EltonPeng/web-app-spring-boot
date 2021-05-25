package com.zijian.java.web.spring.webapp.ui.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    @GetMapping
    public String getUser()  {
        return "you get a user!";
    }

    @PostMapping
    public String createUser() {
        return "you create a user!";
    }

    @PutMapping
    public String updateUser() {
        return "you update a user!";
    }

    @DeleteMapping
    public String deleteUser() {
        return "you delete a user";
    }
}