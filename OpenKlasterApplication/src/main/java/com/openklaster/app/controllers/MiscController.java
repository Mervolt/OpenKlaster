package com.openklaster.app.controllers;

import com.openklaster.app.services.ManufacturerCredentialsProvider;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = "miscellaneous")
@RestController
@AllArgsConstructor
public class MiscController {
    private final ManufacturerCredentialsProvider credentialsProvider;

    @GetMapping(path = "manufacturerCredentials")
    public Map<String, List<String>> getManufacturerCredentials() {
        return credentialsProvider.getManufacturerCredentials();
    }
}
