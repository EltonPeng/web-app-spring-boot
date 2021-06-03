package com.zijian.java.web.spring.webapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.zijian.java.web.spring.webapp.io.entity.AddressEntity;
import com.zijian.java.web.spring.webapp.io.entity.UserEntity;
import com.zijian.java.web.spring.webapp.io.repositories.AddressRepository;
import com.zijian.java.web.spring.webapp.io.repositories.UserRepository;
import com.zijian.java.web.spring.webapp.service.AddressService;
import com.zijian.java.web.spring.webapp.shared.dto.AddressDto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;
    
    @Override
    public List<AddressDto> getAddressesByUserId(String userId) {
        List<AddressDto> result = new ArrayList<>();
        UserEntity storedUser = userRepository.findByUserId(userId);
        if(storedUser == null) return result;

        Iterable<AddressEntity> addresses = addressRepository.findAllByUser(storedUser);
        ModelMapper modelMapper = new ModelMapper();
        for(AddressEntity addressEntity: addresses){
            result.add(modelMapper.map(addressEntity, AddressDto.class));
        }
        
        return result;
    }

    @Override
    public AddressDto getAddress(String addressId) {
        
        AddressEntity address = addressRepository.findByAddressId(addressId);

        if(address == null) return null;

        return new ModelMapper().map(address, AddressDto.class);
    }
    
}
