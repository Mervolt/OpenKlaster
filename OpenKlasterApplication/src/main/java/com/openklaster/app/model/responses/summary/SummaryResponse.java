package com.openklaster.app.model.responses.summary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.openklaster.app.model.responses.serializers.BigDecimalSerializer;
import com.openklaster.app.model.responses.serializers.DateSerializer;
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
