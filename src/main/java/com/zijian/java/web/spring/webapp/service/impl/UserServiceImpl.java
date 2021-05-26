package com.zijian.java.web.spring.webapp.service.impl;

import com.zijian.java.web.spring.webapp.UserRepository;
import com.zijian.java.web.spring.webapp.io.entity.UserEntity;
import com.zijian.java.web.spring.webapp.service.UserService;
import com.zijian.java.web.spring.webapp.shared.dto.UserDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto user) {
        
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        userEntity.setEncryptedPassword("test");
        userEntity.setUserId("123");

        UserEntity storedUser = userRepository.save(userEntity);

        UserDto result = new UserDto();
        BeanUtils.copyProperties(storedUser, result);

        return result;
    }
    
}
