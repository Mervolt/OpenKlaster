package com.openklaster.api.handler.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.openklaster.api.handler.properties.SummaryProperties;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class DateSerializer extends JsonSerializer<Date> {
    public static final String TIME_FORMAT = "HH:mm:ss";

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(parseTimeFromTimeStamp(date));
    }

    private String parseTimeFromTimeStamp(Date timestamp) {
        return new SimpleDateFormat(TIME_FORMAT).format(timestamp);
    }
}