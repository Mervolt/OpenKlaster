package com.openklaster.app.model.responses.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(parseTimeFromTimeStamp(date));
    }

    private String parseTimeFromTimeStamp(Date timestamp) {
        return new SimpleDateFormat(TIME_FORMAT).format(timestamp);
    }
}
