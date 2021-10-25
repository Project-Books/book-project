/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2021  Karan Kumar
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.controller;

import com.karankumar.bookproject.model.Book;
import com.karankumar.bookproject.service.BookService;
import com.karankumar.bookproject.service.PredefinedShelfService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookControllerTest {
    private final BookController bookController;
    private final BookService mockedBookService;

    BookControllerTest() {
        mockedBookService = mock(BookService.class);
        PredefinedShelfService mockedPredefinedShelfService = mock(PredefinedShelfService.class);
        ModelMapper mockedModelMapper = mock(ModelMapper.class);
        bookController = new BookController(
                mockedBookService,
                mockedPredefinedShelfService,
                mockedModelMapper
        );
    }

    @Test
    void all_returnsEmptyList_whenNoBooksExist() {
        when(mockedBookService.findAll()).thenReturn(new ArrayList<>());

        assertThat(bookController.all().size()).isZero();
    }

    @Test
    void all_returnsNonEmptyList_whenBooksExist() {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());

        // when
        when(mockedBookService.findAll()).thenReturn(books);

        // then
        assertThat(bookController.all().size()).isEqualTo(books.size());
    }

    @Test
    void findById_returnsBook_ifPresent() {
        Book book = new Book();
        when(mockedBookService.findById(any(Long.class)))
                .thenReturn(Optional.of(book));

        assertThat(bookController.findById(0L)).isEqualTo(book);
    }

    @Test
    void findById_returnsNotFound_ifBookIsEmpty() {
        when(mockedBookService.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> bookController.findById(0L));
    }

    @Test
    // TODO: finish writing this test
    void findByShelf_returnsNotFound_ifBookDoesNotExist() {
//        when(mockedBookService.findByShelfAndTitleOrAuthor(
//                any(Shelf.class),
//                any(String.class),
//                any(String.class))
//        ).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

//        assertThatExceptionOfType(BookNotFoundException.class)
//                .isThrownBy(bookController.findByShelf(new CustomShelf(), "title", "author"));
    }

    @Test
    // TODO: finish writing this test
    void delete_returnsNotFound_ifBookDoesNotExist() {
        when(mockedBookService.findById(any(Long.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

//        assertThatExceptionOfType(BookNotFoundException.class)
//                .isThrownBy(bookController.delete(1L));
    }
}
