package com.karankumar.bookproject.backend.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.PredefinedShelf;

import java.io.IOException;

public class PredefinedShelfSerializer extends StdSerializer<PredefinedShelf> {

    public PredefinedShelfSerializer() {
        this(null);
    }
    public PredefinedShelfSerializer(Class<PredefinedShelf> t) {
        super(t);
    }

    /*
     * Serialize PredefinedShelf as follows
     * {
     *   "shelfName": "nameOfShelf",
     *   "Books": [
     *              {
     *                 title: "title",
     *                 edition: "edition",
     *                 recommendedBy: "name",
     *                 isbn: "isbn",
     *                 genre: ["genre"],
     *
     *               }
     *            ]
     *
     *
     */
    @Override
    public void serialize(PredefinedShelf predefinedShelf,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject(); // Open JSON Object
        jsonGenerator.writeStringField("shelfName", predefinedShelf.getShelfName()); // Write ShelfName

        jsonGenerator.writeFieldName("books"); // Write Book JSON Object Label
        jsonGenerator.writeStartArray(); // Start JSON Array for Book Objects

        for(Book b : predefinedShelf.getBooks()){
            jsonGenerator.writeObject(b);
        }
        jsonGenerator.writeEndArray(); // End JSON Array for Book Objects

        jsonGenerator.writeEndObject(); // End JSON Object

    }
}
