package com.zijian.java.web.spring.webapp.io.repositories;

import com.zijian.java.web.spring.webapp.io.entity.UserEntity;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);

    UserEntity findByUserId(String userId);

	UserEntity findUserByEmailVerificationToken(String token); 
}
