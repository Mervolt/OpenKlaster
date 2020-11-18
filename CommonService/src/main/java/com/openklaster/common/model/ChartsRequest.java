package com.openklaster.common.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartsRequest {
    private String username;
    private String installationId;
    private String date;
}
