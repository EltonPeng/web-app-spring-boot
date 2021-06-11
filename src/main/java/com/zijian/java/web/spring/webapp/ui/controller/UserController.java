package com.zijian.java.web.spring.webapp.ui.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zijian.java.web.spring.webapp.exceptions.UserServiceException;
import com.zijian.java.web.spring.webapp.service.AddressService;
import com.zijian.java.web.spring.webapp.service.UserService;
import com.zijian.java.web.spring.webapp.shared.dto.AddressDto;
import com.zijian.java.web.spring.webapp.shared.dto.UserDto;
import com.zijian.java.web.spring.webapp.ui.model.request.UserRequestModel;
import com.zijian.java.web.spring.webapp.ui.model.response.AddressRest;
import com.zijian.java.web.spring.webapp.ui.model.response.ErrorMessages;
import com.zijian.java.web.spring.webapp.ui.model.response.OperationStatusModel;
import com.zijian.java.web.spring.webapp.ui.model.response.UserRest;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService; 

    @Autowired
    AddressService addressService;

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id)  {
        UserDto userDto = userService.getUserByUserId(id);

        return new ModelMapper().map(userDto, UserRest.class);
    }

    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CollectionModel<AddressRest> getUserAddresses(@PathVariable String id)  {
        List<AddressDto> addressesDto = addressService.getAddressesByUserId(id);
        java.lang.reflect.Type listType = new TypeToken<List<AddressRest>>() {}.getType();

        List<AddressRest> result = new ModelMapper().map(addressesDto, listType);
        for(AddressRest addressRest: result){
            Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserAddress(id, addressRest.getAddressId())).withSelfRel();
            addressRest.add(selfLink);
        }

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).withRel("user"); 
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(id)).withSelfRel();
        return CollectionModel.of(result, userLink, selfLink);
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EntityModel<AddressRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId)  {
        AddressDto addressDto = addressService.getAddress(addressId);

        AddressRest result = new ModelMapper().map(addressDto, AddressRest.class);

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");        
        Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
    
        return EntityModel.of(result, Arrays.asList(userLink, userAddressesLink, selfLink));
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "5") int limit)  {
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
        consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, 
        produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserRequestModel userRequestModel) throws Exception {

        if(userRequestModel.getFirstName().isEmpty())  throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        if(userRequestModel.getLastName().isEmpty()) throw new NullPointerException("last name is empty. **");

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userRequestModel, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        UserRest result = modelMapper.map(createdUser, UserRest.class);

        return result;
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserRequestModel userRequestModel) {
        
        
        UserRest result = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequestModel, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, result);

        return result;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {

        userService.deleteUser(id);

        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.DELETE.name());
        operationStatusModel.setOperationResult("SUCCESS");
        return operationStatusModel;
    }

    // http://localhost:8080/web-app/users/email-verification?token=xxxxxxxxxxx
    @GetMapping(path = "/email-verification", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token){

        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if(isVerified){
        operationStatusModel.setOperationResult("SUCCESS");
        } else {
            operationStatusModel.setOperationResult("ERROR");
        }
        
        return operationStatusModel;
    }
}