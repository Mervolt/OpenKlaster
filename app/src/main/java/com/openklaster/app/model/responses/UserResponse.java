package com.openklaster.app.model.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse {
    private String username;
    private String email;
    private List<TokenResponse> tokens;

}
