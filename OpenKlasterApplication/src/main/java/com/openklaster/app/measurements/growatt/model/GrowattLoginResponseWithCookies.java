package com.openklaster.app.measurements.growatt.model;

import lombok.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.util.MultiValueMap;

@Value
public class GrowattLoginResponseWithCookies {
    GrowattLoginResponse loginResponse;
    MultiValueMap<String, ResponseCookie> cookies;
}
