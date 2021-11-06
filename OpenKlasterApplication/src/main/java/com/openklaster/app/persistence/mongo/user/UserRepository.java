package com.openklaster.app.persistence.mongo.user;

import com.openklaster.app.model.entities.user.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    @Query("{ 'userTokens.data' : ?0 }")
    Optional<UserEntity> findByUserTokens(String userToken);

    @Query("{ 'sessionToken.data' : ?0 }")
    Optional<UserEntity> findBySessionToken(String sessionToken);
}
