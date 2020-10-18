package com.karankumar.bookproject.backend.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LocalDateDeserializer should")
public class LocalDateDeserializerTest {
    private ObjectMapper mapper;
    private LocalDateDeserializer deserializer;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        deserializer = new LocalDateDeserializer();
    }
    @Test
    void deserializeDateStringCorrectly() throws IOException {
        String testJSON = "{ " +
                "\"date\": \"2020-01-01\""+
                "}";
        JsonParser parser = mapper.getFactory().createParser(testJSON);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        // when
        LocalDate parsedDate = deserializer.deserialize(parser, ctxt);

        // then
        assertThat(parsedDate).isEqualTo(LocalDate.of(2020,1,1));

    }
}
