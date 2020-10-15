package com.openklaster.api.model.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class EnvironmentalBenefits {
    private int co2Reduced;
    private int treesSaved;
}
