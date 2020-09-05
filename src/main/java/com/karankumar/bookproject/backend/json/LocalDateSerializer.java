package com.karankumar.bookproject.backend.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateSerializer extends StdSerializer<LocalDate> {
    protected LocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String dateString = String.format("%02d", localDate.getDayOfMonth()) + "-"
                            + String.format("%02d", localDate.getMonthValue())+ "-"
                            + localDate.getYear();
        jsonGenerator.writeString(dateString);
    }
}
