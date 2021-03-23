/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2021  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karankumar.bookproject.backend.model.Author;
import lombok.Data;
import lombok.Setter;
import javax.persistence.Id;
//import lombok.Builder;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Builder
@Data
public class BookDto {
	@Setter
	@Id
	@JsonProperty("id")
	private Long id;
	
    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private Author author;
    
    @JsonProperty("predefinedShelfString")
    private String predefinedShelfString;
    
    @JsonProperty("numberOfPages")
    //@Max(value = MAX_PAGES)
    private Integer numberOfPages;

    @JsonProperty("pagesRead")
    //@Max(value = MAX_PAGES)
    private Integer pagesRead;

    @JsonProperty("bookGenre")
    private String bookGenre;	//currently need to use the constant names in request body (use "HORROR" vs "Horror")
    
    @JsonProperty("bookFormat")
    private String bookFormat;	//currently need to use the constant names in request body (use "EBOOK" vs "eBook")
    
    @JsonProperty("seriesPosition")
    private Integer seriesPosition;
    
    @JsonProperty("edition")
    private String edition;
    
    @JsonProperty("bookRecommendedBy")
    private String bookRecommendedBy;

    //@ISBN
    @JsonProperty("isbn")
    private String isbn;

    @JsonProperty("yearofPublication")
    private Integer yearOfPublication;
}
