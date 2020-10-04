package com.openklaster.api.model.summary;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EnvironmentalBenefits {
    private int co2Reduced;
    private int treesSaved;
}
