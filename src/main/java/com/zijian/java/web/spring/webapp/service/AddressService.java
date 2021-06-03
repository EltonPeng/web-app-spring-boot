package com.zijian.java.web.spring.webapp.service;

import java.util.List;

import com.zijian.java.web.spring.webapp.shared.dto.AddressDto;

public interface AddressService {
    List<AddressDto> getAddressesByUserId(String userId);

    AddressDto getAddress(String addressId);
}
