package com.openklaster.app.controllers;

import com.openklaster.app.model.requests.UpdateUserAdminRequest;
import com.openklaster.app.model.requests.UpdateUserRequest;
import com.openklaster.app.model.responses.UserDto;
import com.openklaster.app.model.responses.UserResponse;
import com.openklaster.app.services.AdminService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "admin", description = "Admin tools")
@Validated
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("qweqwe")
    public List<UserDto> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("ewqeqw")
    public UserDto updateUser(@RequestBody UpdateUserAdminRequest updateUserRequest) {
        return adminService.updateUser(updateUserRequest);
    }
}
