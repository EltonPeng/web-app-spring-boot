package com.zijian.java.web.spring.webapp.io.repositories;

import java.util.List;

import com.zijian.java.web.spring.webapp.io.entity.AddressEntity;
import com.zijian.java.web.spring.webapp.io.entity.UserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUser(UserEntity user);

	AddressEntity findByAddressId(String addressId);
}
