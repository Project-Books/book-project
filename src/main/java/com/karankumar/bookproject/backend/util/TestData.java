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

package com.karankumar.bookproject.backend.util;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.entity.Tag;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestData {
    private static final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
    private static final List<String> recommenders =
            Arrays.asList("John", "Thomas", "Christina", "Luke", "Sally");

    private TestData() { }

    public static int generateRandomNumberInRange() {
        int min = 300;
        int max = 1000;
        return threadLocalRandom.nextInt(min, max + 1);
    }

    public static List<Author> generateAuthors() {
        return Stream.of(
                "J.K. Rowling",
                "Neil Gaiman",
                "J.R.R Tolkien",
                "Roald Dahl",
                "Robert Galbraith",
                "Dan Brown"
        ).map(name -> {
            String[] fullName = name.split(" ");
            return new Author(fullName[0], fullName[1]);
        }).collect(Collectors.toList());
    }

    public static List<Book> generateBooks(
            List<Author> authors,
            List<Tag> tags,
            List<PredefinedShelf> predefinedShelves) {
        return Stream.of(
                "Harry Potter and the Philosopher's stone",
                "Stardust",
                "Harry Potter and the Chamber of Secrets",
                "Harry Potter and the Prisoner of Azkaban",
                "Origin",
                "Harry Potter and the Goblet of Fire",
                "Harry Potter and the Order of Phoenix",
                "Matilda",
                "Harry Potter and the Half-Blood Prince",
                "The Hobbit",
                "Harry Potter and the Deathly Hallows"
        ).map(title -> createBook(authors, title, tags, predefinedShelves))
         .collect(Collectors.toList());
    }

    public static List<Tag> generateListOfTags() {
        return Stream.of("Adventure", "Interesting", "Tolkien", "Pokemon")
                     .map(Tag::new).collect(Collectors.toList());
    }

    public static Book createBook(List<Author> authors, String title, List<Tag> tags,
                                  List<PredefinedShelf> predefinedShelves) {
        Book book = new Book(
                title,
                generateRandomAuthor(authors),
                generateRandomPredefinedShelf(predefinedShelves)
        );

        book.setBookGenre(generateRandomGenre());
        book.setBookRecommendedBy(generateRandomRecommender());
        book.setTags(new HashSet<>(Collections.singletonList(generateRandomTag(tags))));
        book.setSeriesPosition(generateRandomSeriesPosition());
        book.setBookReview("Must Read Book. Really Enjoyed it");
        book.setNumberOfPages(generateRandomNumberInRange());
        book.setPagesRead(generateRandomNumberInRange());

        return book;
    }

    private static Author generateRandomAuthor(List<Author> authors) {
        return authors.get(threadLocalRandom.nextInt(authors.size()));
    }

    private static BookGenre generateRandomGenre() {
        return BookGenre.values()[threadLocalRandom.nextInt(BookGenre.values().length)];
    }

    private static String generateRandomRecommender() {
        return recommenders.get(threadLocalRandom.nextInt(recommenders.size()));
    }

    private static Tag generateRandomTag(List<Tag> tags) {
        return tags.get(threadLocalRandom.nextInt(tags.size()));
    }

    private static int generateRandomSeriesPosition() {
        return threadLocalRandom.nextInt(1, 10 + 1);
    }

    public static PredefinedShelf generateRandomPredefinedShelf(
            List<PredefinedShelf> predefinedShelves) {
        return predefinedShelves.get(threadLocalRandom.nextInt(predefinedShelves.size()));
    }

    public static List<Book> setPredefinedShelfForBooks(List<Book> books,
                                                        List<PredefinedShelf> predefinedShelves) {
        for (Book book : books) {
            PredefinedShelf shelf = generateRandomPredefinedShelf(predefinedShelves);
            PredefinedShelf.ShelfName predefinedShelfName = shelf.getPredefinedShelfName();
            book.setPredefinedShelf(shelf);
            switch (predefinedShelfName) {
                case TO_READ:
                    book.setDateStartedReading(null);
                    book.setDateFinishedReading(null);
                    book.setRating(RatingScale.NO_RATING);
                    break;
                case READING:
                case DID_NOT_FINISH:
                    book.setDateStartedReading(LocalDate.now().minusDays(2));
                    book.setDateFinishedReading(null);
                    book.setRating(RatingScale.NO_RATING);
                    break;
                case READ:
                    book.setRating(RatingScale.values()
                            [threadLocalRandom.nextInt(RatingScale.values().length)]);
                    book.setDateStartedReading(LocalDate.now().minusDays(2));
                    book.setDateFinishedReading(LocalDate.now());
                    break;
            }
        }
        return books;
    }
}
