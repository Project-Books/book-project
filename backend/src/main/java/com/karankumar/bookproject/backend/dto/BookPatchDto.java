package com.karankumar.bookproject.backend.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karankumar.bookproject.backend.model.Author;
import lombok.Data;

@Data
public class BookPatchDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("numberOfPages")
    private Integer numberOfPages;

    @JsonProperty("pagesRead")
    private Integer pagesRead;

    @JsonProperty("bookGenres")
    private Set<String> bookGenres;

    @JsonProperty("bookFormat")
    private String bookFormat;

    @JsonProperty("seriesPosition")
    private Integer seriesPosition;

    @JsonProperty("edition")
    private Integer edition;

    @JsonProperty("bookRecommendedBy")
    private String bookRecommendedBy;

    @JsonProperty("isbn")
    private String isbn;

    @JsonProperty("yearofPublication")
    private Integer yearOfPublication;

    @JsonProperty("author")
    private Author author;

    @JsonProperty("predefinedShelf")
    private String predefinedShelf;

    @JsonProperty("bookReview")
    private String bookReview;
}
