package com.karankumar.bookproject.backend.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.UserCreatedShelf;

import java.io.IOException;

public class UserCreatedShelfSerializer extends StdSerializer<UserCreatedShelf> {

    public UserCreatedShelfSerializer() {
        this(null);
    }
    public UserCreatedShelfSerializer(Class<UserCreatedShelf> t) {
        super(t);
    }

    @Override
    public void serialize(UserCreatedShelf userCreatedShelf,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject(); // Open JSON Object
        jsonGenerator.writeStringField("shelfName", userCreatedShelf.getShelfName()); // Write ShelfName

        jsonGenerator.writeFieldName("books"); // Write Book JSON Object Label
        jsonGenerator.writeStartArray(); // Start JSON Array for Book Objects

        for(Book b : userCreatedShelf.getBooks()){
            jsonGenerator.writeObject(b);
        }

        jsonGenerator.writeEndArray(); // End JSON Array for Book Objects
        jsonGenerator.writeEndObject(); // End JSON Object

    }

}
