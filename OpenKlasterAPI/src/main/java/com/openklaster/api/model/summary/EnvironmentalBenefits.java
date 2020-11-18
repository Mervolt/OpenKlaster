package com.openklaster.api.model.summary;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EnvironmentalBenefits {
    private Integer co2Reduced;
    private Integer treesSaved;

    public EnvironmentalBenefits(Integer co2Reduced, Integer treesSaved) {
        this.co2Reduced = co2Reduced;
        this.treesSaved = treesSaved;
    }
}
