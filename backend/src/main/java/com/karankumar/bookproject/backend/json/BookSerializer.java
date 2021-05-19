package com.karankumar.bookproject.backend.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.PredefinedShelf;

import java.io.IOException;

public class BookSerializer extends StdSerializer<Book> {

    public BookSerializer(){
        this(null);
    }
    protected BookSerializer(Class<Book> t) {
        super(t);
    }

    @Override
    public void serialize(Book book, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject(); // Open Book JSON Object

        jsonGenerator.writeStringField("title", String.valueOf(book.getTitle()));
        jsonGenerator.writeStringField("edition", String.valueOf(book.getEdition()));
        jsonGenerator.writeStringField("recommendedBy", String.valueOf(book.getBookRecommendedBy()));
        jsonGenerator.writeStringField("isbn", String.valueOf(book.getIsbn()));
        jsonGenerator.writeStringField("review", String.valueOf(book.getBookReview()));
        jsonGenerator.writeStringField("dateStartedReading", String.valueOf(book.getDateStartedReading()));
        jsonGenerator.writeStringField("dateFinishedReading", String.valueOf(book.getDateFinishedReading()));
        jsonGenerator.writeObjectField("bookFormat", String.valueOf(book.getBookFormat()));
        jsonGenerator.writeObjectField("numberOfPages", String.valueOf(book.getNumberOfPages()));
        jsonGenerator.writeObjectField("rating", String.valueOf(book.getRating()));
        jsonGenerator.writeObjectField("seriesPosition", String.valueOf(book.getSeriesPosition()));
        jsonGenerator.writeObjectField("pagesRead", String.valueOf(book.getPagesRead()));
        jsonGenerator.writeObjectField("yearOfPublication", String.valueOf(book.getYearOfPublication()));

        // Array Objects
        jsonGenerator.writeObjectField("genre", book.getBookGenre()); // Writes Genre as Array object
        jsonGenerator.writeObjectField("publishers", book.getPublishers());
        jsonGenerator.writeObjectField("tags", book.getTags());

        jsonGenerator.writeEndObject(); //End Book JSON Object
    }
}
