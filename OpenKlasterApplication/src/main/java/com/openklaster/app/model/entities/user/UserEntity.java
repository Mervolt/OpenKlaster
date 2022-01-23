package com.openklaster.app.model.entities.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("userz")
@Value
@Getter
@Builder
public class UserEntity {

    @Id
    String id;

    String password;
    String email;
    @With
    List<TokenEntity> userTokens;
    @With
    SessionTokenEntity sessionToken;
    boolean active;
    Role role;

    public String getUsername() {
        return getId();
    }
}
