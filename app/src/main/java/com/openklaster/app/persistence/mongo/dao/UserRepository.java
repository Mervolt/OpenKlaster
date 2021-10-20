package com.openklaster.app.persistence.mongo.dao;

import com.openklaster.app.model.entities.user.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

}
