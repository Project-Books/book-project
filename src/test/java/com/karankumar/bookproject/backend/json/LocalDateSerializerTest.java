package com.karankumar.bookproject.backend.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

class LocalDateSerializerTest {
    @Test
    void shouldSerializeLocalDate() throws IOException {
        // given
        LocalDateSerializer serializer = new LocalDateSerializer();
        LocalDate date = LocalDate.of(2020, 9, 5);
        String expectedJsonString = "\"2020-09-05\"";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addSerializer(serializer));

        // when
        String json = mapper.writeValueAsString(date);

        // then
        Assertions.assertEquals(expectedJsonString, json);
    }
}