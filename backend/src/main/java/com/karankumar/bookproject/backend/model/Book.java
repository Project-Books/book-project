/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karankumar.bookproject.backend.json.LocalDateSerializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Data
//@DynamicUpdate
@JsonIgnoreProperties(value = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "tags","publishers", "predefinedShelf", "customShelf"})
public class Book {
    public static final int MAX_PAGES = 23_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @NotBlank
    private String title;

    @Max(value = MAX_PAGES)
    private Integer numberOfPages;

    @Max(value = MAX_PAGES)
    private Integer pagesRead;

    private BookGenre bookGenre;
    private BookFormat bookFormat;
    private Integer seriesPosition;
    private String edition;
    private String bookRecommendedBy;

    @ISBN
    private String isbn;

    private Integer yearOfPublication;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(
            name = "author_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "book_author_id_fk")
    )
    private Author author;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(name = "predefined_shelf_id", referencedColumnName = "id")
    private PredefinedShelf predefinedShelf;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(name = "custom_shelf_id", referencedColumnName = "id")
    private CustomShelf customShelf;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "book_tag",
            joinColumns = @JoinColumn(
                    name = "book_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "book_tag_book_id_fk")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "book_tag_tag_id_fk")
            )
    )
    @Setter(AccessLevel.NONE)
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "book_publisher",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    )
    private Set<Publisher> publishers;

    // For books that have been read
    private RatingScale rating;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateStartedReading;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateFinishedReading;
    private String bookReview;

    //need public parameterless constructor for mapstruct, delete this later
//    public Book() {
//    	this.title = "";
//    	this.author = null;
//    	this.predefinedShelf = null;
//    }

    public Book(String title, Author author, PredefinedShelf predefinedShelf) {
        this.title = title;
        this.author = author;
        this.predefinedShelf = predefinedShelf;
    }

    public Book(String title, Author author, PredefinedShelf predefinedShelf,
                Set<Publisher> publishers) {
        this.title = title;
        this.author = author;
        this.predefinedShelf = predefinedShelf;
        this.publishers = publishers;
    }

    public void setEdition(Integer edition) {
        if (edition == null) {
            return;
        }
        this.edition = convertToBookEdition(edition);
    }

    public static String convertToBookEdition(int edition) {
        String bookEdition;
        int lastDigit = edition % 10;
        int lastTwoDigits = edition % 100;

        if (lastDigit == 1 && lastTwoDigits != 11) {
            bookEdition = edition + "st edition";
        } else if (lastDigit == 2 && lastTwoDigits != 12) {
            bookEdition = edition + "nd edition";
        } else if (lastDigit == 3 && lastTwoDigits != 13) {
            bookEdition = edition + "rd edition";
        } else {
            bookEdition = edition + "th edition";
        }
        return bookEdition;
    }

    public void addTag(@NonNull Tag tag) {
        tags.add(tag);
        tag.getBooks().add(this);
    }

    public void removeTag(@NonNull Tag tag) {
        tags.remove(tag);
        tag.getBooks().remove(this);
    }

    public void setPublicationYear(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public static class BookBuilder {
        public BookBuilder edition(Integer edition) {
            this.edition = convertToBookEdition(edition);
            return this;
        }
    }

    @Override
    public String toString() {
        return Book.class.getSimpleName() + "{"
                + "title='" + title + '\''
                + '}';
    }
}
