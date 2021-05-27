package com.zijian.java.web.spring.webapp.service.impl;

import java.util.ArrayList;

import com.zijian.java.web.spring.webapp.io.entity.UserEntity;
import com.zijian.java.web.spring.webapp.io.repositories.UserRepository;
import com.zijian.java.web.spring.webapp.service.UserService;
import com.zijian.java.web.spring.webapp.shared.Utils;
import com.zijian.java.web.spring.webapp.shared.dto.UserDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findUserByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists.");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId();
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity storedUser = userRepository.save(userEntity);

        UserDto result = new UserDto();
        BeanUtils.copyProperties(storedUser, result);

        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        UserEntity userEntity = userRepository.findUserByEmail(email);
        
        if(userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity storedUser = userRepository.findUserByEmail(email);
        
        if(storedUser == null) throw new UsernameNotFoundException(email);

        UserDto result = new UserDto();
        BeanUtils.copyProperties(storedUser, result);

        return result;
    }

    @Override
    public UserDto getUserByUserId(String id) {
        UserEntity storedUser = userRepository.findByUserId(id);
        
        if(storedUser == null) throw new UsernameNotFoundException(id);

        UserDto result = new UserDto();
        BeanUtils.copyProperties(storedUser, result);

        return result;
    }
    
}
