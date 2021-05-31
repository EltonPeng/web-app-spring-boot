package com.zijian.java.web.spring.webapp.ui.controller;

import java.util.ArrayList;

import java.util.List;

import com.zijian.java.web.spring.webapp.exceptions.UserServiceException;
import com.zijian.java.web.spring.webapp.service.UserService;
import com.zijian.java.web.spring.webapp.shared.dto.UserDto;
import com.zijian.java.web.spring.webapp.ui.model.request.UserRequestModel;
import com.zijian.java.web.spring.webapp.ui.model.response.ErrorMessages;
import com.zijian.java.web.spring.webapp.ui.model.response.OperationStatusModel;
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
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping(produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="limit", defaultValue="5") int limit)  {
        List<UserRest> results = new ArrayList<UserRest>();
        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest result = new UserRest();

            BeanUtils.copyProperties(userDto, result);
            results.add(result);
        }

        return results;
    }

    @PostMapping(
        consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, 
        produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserRequestModel userRequestModel) throws Exception {

        if(userRequestModel.getFirstName().isEmpty())  throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        if(userRequestModel.getLastName().isEmpty()) throw new NullPointerException("last name is empty. **");

        UserRest result = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequestModel, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, result);

        return result;
    }

    @PutMapping(path="/{id}", consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserRequestModel userRequestModel) {
        
        
        UserRest result = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequestModel, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, result);

        return result;
    }

    @DeleteMapping(path="/{id}", produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {

        userService.deleteUser(id);

        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName("DELETE");
        operationStatusModel.setOperationResult("SUCCESS");
        return operationStatusModel;
    }
}