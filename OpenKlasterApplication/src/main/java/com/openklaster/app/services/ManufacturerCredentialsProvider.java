package com.openklaster.app.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManufacturerCredentialsProvider {

    public Map<String, List<String>> getManufacturerCredentials() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("Growatt", Arrays.asList("Username", "Password"));
        map.put("Token-manufacturer", Collections.singletonList("Token"));
        return map;
    }
}
