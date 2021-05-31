package com.zijian.java.web.spring.webapp.ui.controller;

import com.zijian.java.web.spring.webapp.service.UserService;
import com.zijian.java.web.spring.webapp.shared.dto.UserDto;
import com.zijian.java.web.spring.webapp.ui.model.request.UserRequestModel;
import com.zijian.java.web.spring.webapp.ui.model.response.UserRest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService; 

    @GetMapping(path="/{id}", produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id)  {
        UserRest result = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, result);

        return result;
    }

    @PostMapping(
        consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, 
        produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserRequestModel userRequestModel) {        
        UserRest result = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequestModel, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, result);

        return result;
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