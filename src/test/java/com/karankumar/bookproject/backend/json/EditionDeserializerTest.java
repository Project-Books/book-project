package com.karankumar.bookproject.backend.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EditionDeserializer should")
class EditionDeserializerTest {

    private ObjectMapper mapper;
    private EditionDeserializer deserializer;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        deserializer = new EditionDeserializer();
    }


    @Test
    void deserializeEditionStringCorrectly() throws IOException {
        // given
        String edition = "{ \"edition\": \"2nd edition\" } ";
        JsonParser parser = mapper.getFactory().createParser(edition);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        // when
        Integer parsedValue = deserializer.deserialize(parser, ctxt);
        // then
        assertThat(parsedValue).isEqualTo(2);
    }
}