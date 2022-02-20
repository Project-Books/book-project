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

package com.karankumar.bookproject.book.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import com.karankumar.bookproject.book.json.LocalDateSerializer;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.shelf.model.UserCreatedShelf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.ISBN;

@Entity
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(value = {"id"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@NamedEntityGraph(
    name = "Book.author",
    attributeNodes = {@NamedAttributeNode("author")})
@ExcludeFromJacocoGeneratedReport
public class Book {
  public static final int MAX_PAGES = 23_000;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @NotNull @NotBlank private String title;

  @Max(value = MAX_PAGES)
  private Integer numberOfPages;

  @Max(value = MAX_PAGES)
  private Integer pagesRead;

  @ElementCollection(targetClass = BookGenre.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "book_genre", joinColumns = @JoinColumn(name = "book_id"))
  @Column(name = "genre")
  private Set<BookGenre> bookGenre = new HashSet<>();

  private BookFormat bookFormat;

  private Integer seriesPosition;

  private String edition;

  private String bookRecommendedBy;

  @ISBN private String isbn;

  private Integer yearOfPublication;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(
      name = "author_id",
      nullable = false,
      referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "book_author_id_fk"))
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @ToString.Exclude
  private Author author;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "predefined_shelf_id", referencedColumnName = "id")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @ToString.Exclude
  private PredefinedShelf predefinedShelf;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinColumn(name = "user_created_shelf_id", referencedColumnName = "id")
  @ToString.Exclude
  private UserCreatedShelf userCreatedShelf;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(
      name = "book_tag",
      joinColumns =
          @JoinColumn(
              name = "book_id",
              referencedColumnName = "id",
              foreignKey = @ForeignKey(name = "book_tag_book_id_fk")),
      inverseJoinColumns =
          @JoinColumn(
              name = "tag_id",
              referencedColumnName = "id",
              foreignKey = @ForeignKey(name = "book_tag_tag_id_fk")))
  @Setter(AccessLevel.NONE)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @ToString.Exclude
  private Set<Tag> tags = new HashSet<>();

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(
      name = "book_publisher",
      joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "publisher_id", referencedColumnName = "id"))
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @ToString.Exclude
  private Set<Publisher> publishers = new HashSet<>();

  // For books that have been read
  private RatingScale rating;

  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate dateStartedReading;

  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate dateFinishedReading;

  private String bookReview;

  public Book(String title, Author author, PredefinedShelf predefinedShelf) {
    this.title = title;
    this.author = author;
    addPredefinedShelf(predefinedShelf);
  }

  public Book(
      String title, Author author, PredefinedShelf predefinedShelf, Set<Publisher> publishers) {
    this.title = title;
    this.author = author;
    addPredefinedShelf(predefinedShelf);
    this.publishers = publishers;
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

  public void setEdition(Integer edition) {
    if (edition == null) {
      return;
    }
    this.edition = convertToBookEdition(edition);
  }

  public void addTag(@NonNull Tag tag) {
    if (tags == null) {
      tags = new HashSet<>();
    }
    tags.add(tag);
    tag.getBooks().add(this);
  }

  public void removeTag(@NonNull Tag tag) {
    tags.remove(tag);
    tag.getBooks().remove(this);
  }

  public void addPredefinedShelf(@NonNull PredefinedShelf predefinedShelf) {
    this.predefinedShelf = predefinedShelf;
    predefinedShelf.getBooks().add(this);
  }

  public void removePredefinedShelf() {
    predefinedShelf.getBooks().remove(this);
    predefinedShelf = null;
  }

  public void removeAuthor() {
    author.getBooks().remove(this);
    author = null;
  }

  public void setPublicationYear(Integer yearOfPublication) {
    this.yearOfPublication = yearOfPublication;
  }

  public void addGenre(BookGenre genre) {
    bookGenre.add(genre);
  }

  public void addPublisher(@NonNull Publisher publisher) {
    publishers.add(publisher);
    publisher.getBooks().add(this);
  }

  public void removePublisher(@NonNull Publisher publisher) {
    for (Publisher bookPublisher : publishers) {
      if (bookPublisher.getId().equals(publisher.getId())) {
        bookPublisher.getBooks().remove(this);
        break;
      }
    }
    publishers.remove(publisher);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Book book = (Book) o;
    return id != null && Objects.equals(id, book.id);
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @ExcludeFromJacocoGeneratedReport
  public static class BookBuilder {
    public BookBuilder edition(Integer edition) {
      this.edition = convertToBookEdition(edition);
      return this;
    }
  }
}
