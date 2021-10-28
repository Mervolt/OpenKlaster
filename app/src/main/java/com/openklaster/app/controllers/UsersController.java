package com.openklaster.app.controllers;

import com.openklaster.app.model.entities.user.TokenEntity;
import com.openklaster.app.model.entities.user.UserEntity;
import com.openklaster.app.model.requests.LoginRequest;
import com.openklaster.app.model.requests.RegisterRequest;
import com.openklaster.app.model.requests.UpdateUserRequest;
import com.openklaster.app.model.responses.TokenResponse;
import com.openklaster.app.model.responses.UserResponse;
import com.openklaster.app.services.UsersService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "user", description = "User management")
@RestController
@RequestMapping("user")
public class UsersController {

    @Autowired
    UsersService usersService;

    @GetMapping()
    public UserResponse userInfo(@RequestParam String username) {
        return usersService.getUser(username).map(UsersController::fromEntityToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public void registerUser(@RequestBody RegisterRequest registerRequest) {
        usersService.addUser(registerRequest);
    }

    @PostMapping(path = "login")
    public TokenResponse getSessionToken(@RequestBody LoginRequest loginRequest) {
        return usersService.generateSessionTokenForUser(loginRequest);
    }

    @PutMapping()
    public UserResponse updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        return fromEntityToResponse(usersService.editUser(updateUserRequest));
    }


    public static UserResponse fromEntityToResponse(UserEntity entity) {
        return UserResponse.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .tokens(toTokenResponse(entity.getUserTokens()))
                .build();
    }

    private static List<TokenResponse> toTokenResponse(List<TokenEntity> userTokens) {
        return Optional.ofNullable(userTokens)
                .map(tokenEntities -> tokenEntities.stream().map(tokenEntity -> new TokenResponse(tokenEntity.getData())).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
