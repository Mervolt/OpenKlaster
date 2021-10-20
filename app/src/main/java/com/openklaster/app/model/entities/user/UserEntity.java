package com.openklaster.app.model.entities.user;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("userz")
@Value
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

    public String getUsername() {
        return getId();
    }
}
