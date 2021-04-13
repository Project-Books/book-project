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

package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.service.BookNotFoundException;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookControllerTest {
    private final BookController bookController;
    private final PredefinedShelfService mockedPredefinedShelfService;
    private final BookService mockedBookService;

    BookControllerTest() {
        mockedBookService = mock(BookService.class);
        mockedPredefinedShelfService = mock(PredefinedShelfService.class);
        ModelMapper mockedModelMapper = mock(ModelMapper.class);
        bookController = new BookController(
                mockedBookService,
                mockedPredefinedShelfService,
                mockedModelMapper
        );
    }

    @Test
    void findById_returnsBook_ifPresent() {
        Optional<Book> optionalBook = Optional.of(new Book());
        when(mockedBookService.findById(any(Long.class)))
                .thenReturn(optionalBook);

        assertThat(bookController.findById(0L)).isEqualTo(optionalBook);
    }

    @Test
    void findById_returnsNotFound_ifBookIsEmpty() {
        when(mockedBookService.findById(any(Long.class))).thenThrow(new BookNotFoundException(1L));

        assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> bookController.findById(0L));
    }
}
