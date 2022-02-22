package com.openklaster.app.controllers;

import com.openklaster.app.model.requests.UpdateUserAdminRequest;
import com.openklaster.app.model.responses.UserDto;
import com.openklaster.app.services.AdminService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "admin", description = "Admin tools")
@Hidden
@Validated
@AllArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("user")
    public List<UserDto> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("user")
    public UserDto updateUser(@RequestBody UpdateUserAdminRequest updateUserRequest) {
        return adminService.updateUser(updateUserRequest);
    }
}
