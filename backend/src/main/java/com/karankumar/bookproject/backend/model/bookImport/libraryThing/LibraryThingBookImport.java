package com.karankumar.bookproject.backend.model.bookImport.libraryThing;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LibraryThingBookImport {

  @JsonProperty("title")
  private String title;
  
  @JsonProperty("primaryauthor")
  private String author;
  
  @JsonProperty("date")
  private Integer publicationYear;

  @JsonProperty("originalisbn")
  private String isbn;

}
