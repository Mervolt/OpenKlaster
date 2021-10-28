package com.openklaster.app.services;

import com.openklaster.app.model.entities.user.SessionTokenEntity;
import com.openklaster.app.model.entities.user.TokenEntity;
import com.openklaster.app.model.entities.user.UserEntity;
import com.openklaster.app.model.requests.ApiTokenRequest;
import com.openklaster.app.model.requests.LoginRequest;
import com.openklaster.app.model.requests.RegisterRequest;
import com.openklaster.app.model.requests.UpdateUserRequest;
import com.openklaster.app.model.responses.TokenResponse;
import com.openklaster.app.persistence.mongo.user.UserContextAccessor;
import com.openklaster.app.persistence.mongo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokensService tokensService;

    @Autowired
    private UserContextAccessor userContextAccessor;

    public Optional<UserEntity> getUser() {
        return Optional.ofNullable(userContextAccessor.getCurrentUser());
    }

    public UserEntity addUser(RegisterRequest registerRequest) {
        Optional<UserEntity> userOpt = userRepository.findById(registerRequest.getUsername());
        if (userOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User %s is already created.", registerRequest.getUsername()));
        }
        UserEntity newUser = UserEntity.builder()
                .id(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(authService.hashPassword(registerRequest.getPassword()))
                .build();
        userRepository.insert(newUser);
        return newUser;
    }

    public UserEntity editUser(UpdateUserRequest updateUserRequest) {
        UserEntity userEntity = getUserOrThrow404(updateUserRequest.getUsername());
        boolean isPasswordOk = authService.authenticatePassword(updateUserRequest.getPassword(), userEntity.getPassword());
        if (!isPasswordOk) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        UserEntity user = UserEntity.builder()
                .id(updateUserRequest.getUsername())
                .email(updateUserRequest.getEmail())
                .password(updateUserRequest.getNewPassword())
                .userTokens(userEntity.getUserTokens())
                .sessionToken(userEntity.getSessionToken())
                .build();

        userRepository.save(user);

        return user;
    }

    public TokenResponse generateSessionTokenForUser(LoginRequest loginRequest) {
        UserEntity userEntity = getUserOrThrow404(loginRequest.getUsername());
        boolean isPasswordOk = authService.authenticatePassword(loginRequest.getPassword(), userEntity.getPassword());
        if (!isPasswordOk) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        SessionTokenEntity generatedToken = tokensService.generateSessionToken();
        UserEntity newUserEntity = userEntity.withSessionToken(generatedToken);
        userRepository.save(newUserEntity);

        return new TokenResponse(generatedToken.getData());
    }

    public TokenResponse generateApiTokenForUser(ApiTokenRequest apiTokenRequest) {
        UserEntity userEntity = getUserOrThrow404(apiTokenRequest.getUsername());
        TokenEntity generatedToken = tokensService.generateUserToken();
        List<TokenEntity> newTokensList = new ArrayList<>(Optional.ofNullable(userEntity.getUserTokens()).orElse(Collections.emptyList()));
        newTokensList.add(generatedToken);
        UserEntity newUser = userEntity.withUserTokens(newTokensList);
        userRepository.save(newUser);
        return new TokenResponse(generatedToken.getData());
    }

    public void removeToken(String apiToken, String username) {
        UserEntity userEntity = getUserOrThrow404(username);
        List<TokenEntity> newTokensList = new ArrayList<>(userEntity.getUserTokens());
        newTokensList.remove(new TokenEntity(apiToken));
        UserEntity newUser = userEntity.withUserTokens(newTokensList);
        userRepository.save(newUser);
    }

    public void removeAllTokens(String username) {
        UserEntity userEntity = getUserOrThrow404(username);
        UserEntity newUser = userEntity.withUserTokens(new ArrayList<>());
        userRepository.save(newUser);
    }


    private UserEntity getUserOrThrow404(String username) {
        return userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
