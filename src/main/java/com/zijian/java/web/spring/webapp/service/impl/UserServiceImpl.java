package com.zijian.java.web.spring.webapp.service.impl;

import com.zijian.java.web.spring.webapp.UserRepository;
import com.zijian.java.web.spring.webapp.io.entity.UserEntity;
import com.zijian.java.web.spring.webapp.service.UserService;
import com.zijian.java.web.spring.webapp.shared.Utils;
import com.zijian.java.web.spring.webapp.shared.dto.UserDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findUserByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists.");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId();
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword("test");

        UserEntity storedUser = userRepository.save(userEntity);

        UserDto result = new UserDto();
        BeanUtils.copyProperties(storedUser, result);

        return result;
    }
    
}
