package com.openklaster.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SummaryResponse {
    private Measurement energy;
    private List<Measurement> power;
}
