package com.zijian.java.web.spring.webapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.zijian.java.web.spring.webapp.io.entity.UserEntity;
import com.zijian.java.web.spring.webapp.io.repositories.UserRepository;
import com.zijian.java.web.spring.webapp.service.UserService;
import com.zijian.java.web.spring.webapp.shared.Utils;
import com.zijian.java.web.spring.webapp.shared.dto.AddressDto;
import com.zijian.java.web.spring.webapp.shared.dto.UserDto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


        for (int i=0; i<user.getAddresses().size(); i++){
            AddressDto address = user.getAddresses().get(i);
            address.setAddressId(utils.generateAddressId());
            address.setUser(user);
            user.getAddresses().set(i, address);
        }

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        String publicUserId = utils.generateUserId();
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);

        UserEntity storedUser = userRepository.save(userEntity);

        UserDto result = modelMapper.map(storedUser, UserDto.class);

        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        UserEntity userEntity = userRepository.findUserByEmail(email);
        
        if(userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), 
            userEntity.getEmailVerificationStatus(), true, true, true, new ArrayList<>());
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

        UserDto result = new ModelMapper().map(storedUser, UserDto.class);

        return result;
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        UserEntity storedUser = userRepository.findByUserId(id);
        
        if(storedUser == null) throw new UsernameNotFoundException(id);

        storedUser.setFirstName(userDto.getFirstName());
        storedUser.setLastName(userDto.getLastName());

        UserEntity updatedUser = userRepository.save(storedUser);
        UserDto result = new UserDto();
        BeanUtils.copyProperties(updatedUser, result);

        return result;
    }

    @Override
    public void deleteUser(String id) {
        UserEntity storedUser = userRepository.findByUserId(id);
        
        if(storedUser == null) throw new UsernameNotFoundException(id);
        userRepository.delete(storedUser);
        
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> results = new ArrayList<>();

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> userEntities = userRepository.findAll(pageableRequest);

        List<UserEntity> userList = userEntities.getContent();

        for (UserEntity userEntity : userList){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            results.add(userDto);
        }

        return results;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if(userEntity != null){
            if(!Utils.hasTokenExpired(token)){
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                return true;
            }
        }

        return false;
    }
    
}
