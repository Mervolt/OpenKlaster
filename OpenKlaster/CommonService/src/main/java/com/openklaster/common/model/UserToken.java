package com.openklaster.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserToken {

    private String data;
    private LocalDate expirationDate;


}
