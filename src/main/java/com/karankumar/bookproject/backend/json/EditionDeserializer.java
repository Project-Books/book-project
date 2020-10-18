package com.karankumar.bookproject.backend.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class EditionDeserializer extends StdDeserializer<Integer> {
    protected EditionDeserializer() {
        super(Integer.class);
    }


    @Override
    public Integer deserialize(JsonParser jsonParser,
                              DeserializationContext deserializationContext) throws IOException {
        String content = jsonParser.getText();
        if (content.contains("st edition")) {
            return Integer.parseInt(content.replace("st edition", ""));
        }
        if (content.contains("nd edition")) {
            return Integer.parseInt(content.replace("nd edition", ""));
        }
        if (content.contains("rd edition")) {
            return Integer.parseInt(content.replace("rd edition", ""));
        }
        if (content.contains("th edition")) {
            return Integer.parseInt(content.replace("th edition", ""));
        }

        return Integer.parseInt(content);
    }
}
