package com.openklaster.app.services;

import com.openklaster.app.model.entities.user.Role;
import com.openklaster.app.model.entities.user.UserEntity;
import com.openklaster.app.model.requests.UpdateUserAdminRequest;
import com.openklaster.app.model.responses.UserDto;
import com.openklaster.app.persistence.mongo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    private UserDto toUserDto(UserEntity userEntity) {
        return new UserDto(userEntity.getUsername(), userEntity.getEmail(), userEntity.getRole().name(), userEntity.isActive());
    }

    public UserDto updateUser(UpdateUserAdminRequest updateUserRequest) {
        UserEntity userEntity = userRepository.findById(updateUserRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String password = Strings.isBlank(updateUserRequest.getPassword()) ? userEntity.getPassword() :
                authService.hashPassword(updateUserRequest.getPassword());

        UserEntity user = UserEntity.builder()
                .id(updateUserRequest.getUsername())
                .email(updateUserRequest.getEmail())
                .password(password)
                .userTokens(userEntity.getUserTokens())
                .sessionToken(userEntity.getSessionToken())
                .role(Role.valueOf(updateUserRequest.getRole()))
                .active(updateUserRequest.isActive())
                .build();

        userRepository.save(user);
        logger.debug(String.format("Edited user %s", user.getUsername()));
        return new UserDto(user.getUsername(), user.getEmail(), user.getRole().name(), user.isActive());
    }
}
