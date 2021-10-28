package com.openklaster.app.controllers;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = "miscellaneous")
@RestController()
public class MiscController {

    @GetMapping(path = "manufacturerCredentials")
    public Map<String, List<String>> getManufacturerCredentials() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("Growatt", Arrays.asList("Username", "Password"));
        map.put("Token-manufacturer", Collections.singletonList("Token"));
        return map;
    }
}
