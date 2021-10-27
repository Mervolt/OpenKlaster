package com.openklaster.app.services;

import com.openklaster.app.model.entities.user.SessionTokenEntity;
import com.openklaster.app.model.entities.user.UserEntity;
import com.openklaster.app.persistence.mongo.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokensService tokensService;

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean authenticatePassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }


    public Optional<UserEntity> getUserAuthenticatedBySessionToken(String sessionToken) {
        return userRepository.findBySessionToken(sessionToken).map(this::refreshToken);
    }

    public Optional<UserEntity> getUserAuthenticatedByApiToken(String apiToken) {
        return userRepository.findByUserTokens(apiToken);
    }

    private UserEntity refreshToken(UserEntity userEntity) {
        SessionTokenEntity refreshedSessionToken = tokensService.getRefreshedSessionToken(userEntity.getSessionToken());

        UserEntity refreshedUserEntity = UserEntity.builder()
                .id(userEntity.getId())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .sessionToken(refreshedSessionToken)
                .userTokens(userEntity.getUserTokens())
                .build();

        userRepository.save(refreshedUserEntity);

        return refreshedUserEntity;
    }
}