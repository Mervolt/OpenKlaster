package com.openklaster.api.model.summary;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EnvironmentalConfig {
    private int co2Reduced;
    private int treesSaved;
}
