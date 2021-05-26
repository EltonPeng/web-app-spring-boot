package com.zijian.java.web.spring.webapp;

import com.zijian.java.web.spring.webapp.io.entity.UserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
