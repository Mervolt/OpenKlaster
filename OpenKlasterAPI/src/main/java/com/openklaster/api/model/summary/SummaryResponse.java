package com.openklaster.api.model.summary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.openklaster.api.handler.serializers.BigDecimalSerializer;
import com.openklaster.api.handler.serializers.DateSerializer;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Builder
@Data
public class SummaryResponse {
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalEnergy;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal currentPower;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal energyProducedToday;
    @JsonSerialize(keyUsing = DateSerializer.class, contentUsing = BigDecimalSerializer.class)
    private Map<Date, BigDecimal> power;
    private EnvironmentalBenefits environmentalBenefits;
}
