package com.karankumar.bookproject.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodreadsBookImport {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Author")
    private String author;

    @JsonProperty("My Rating")
    private String rating;

    @JsonProperty("Date Read")
    private LocalDate dateRead;

    @JsonProperty("Bookshelves")
    private String bookshelves;
}
