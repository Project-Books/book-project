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

package com.karankumar.bookproject.book.controller;

import com.karankumar.bookproject.book.dto.BookPatchDto;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookControllerTest {
  private final BookController bookController;
  private final BookService mockedBookService;

  BookControllerTest() {
    mockedBookService = mock(BookService.class);
    PredefinedShelfService mockedPredefinedShelfService = mock(PredefinedShelfService.class);
    ModelMapper mockedModelMapper = mock(ModelMapper.class);
    bookController =
        new BookController(mockedBookService, mockedPredefinedShelfService, mockedModelMapper);
  }

  @Test
  void all_pageNumberIsOptional() {
    bookController.all(null);
    verify(mockedBookService, times(1)).findAll((Integer) null);
  }

  @Test
  void all_returnsEmptyList_whenNoBooksExist() {
    int page = 0;
    when(mockedBookService.findAll(page)).thenReturn(new ArrayList<>());

    assertThat(bookController.all(page).size()).isZero();
  }

  @Test
  void all_returnsBadRequest_whenNegativePage() {
    Integer page = -1;
    String expectedMessage =
        String.format(
            "%s \"%s\"",
            HttpStatus.BAD_REQUEST,
            String.format(BookController.NEGATIVE_PAGE_ERROR_MESSAGE, page));

    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> bookController.all(page))
        .withMessage(expectedMessage);
  }

  @Test
  void all_returnsNonEmptyList_whenBooksExist() {
    // given
    int page = 0;
    List<Book> books = new ArrayList<>();
    books.add(new Book());
    books.add(new Book());

    // when
    when(mockedBookService.findAll(page)).thenReturn(books);

    // then
    assertThat(bookController.all(page).size()).isEqualTo(books.size());
  }

  @Test
  void findById_returnsBook_ifPresent() {
    Book book = new Book();
    when(mockedBookService.findById(any(Long.class))).thenReturn(Optional.of(book));

    assertThat(bookController.findById(0L)).isEqualTo(book);
  }

  @Test
  void findById_returnsNotFound_ifBookIsEmpty() {
    when(mockedBookService.findById(any(Long.class))).thenReturn(Optional.empty());

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
  void update_callsService_ifBookPresent() {
    // given
    Book book = new Book();
    BookPatchDto bookPatchDto = new BookPatchDto();
    when(mockedBookService.findById(anyLong())).thenReturn(Optional.of(book));

    // when
    bookController.update(1L, bookPatchDto);

    // then
    ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
    ArgumentCaptor<BookPatchDto> bookPatchDtoArgumentCaptor =
        ArgumentCaptor.forClass(BookPatchDto.class);
    verify(mockedBookService)
        .updateBook(bookArgumentCaptor.capture(), bookPatchDtoArgumentCaptor.capture());
    assertThat(bookArgumentCaptor.getValue()).isEqualTo(book);
    assertThat(bookPatchDtoArgumentCaptor.getValue()).isEqualTo(bookPatchDto);
  }

  @Test
  void update_throwsNotFoundException_ifBookNotPresent() {
    when(mockedBookService.findById(anyLong())).thenReturn(Optional.empty());
    long id = 1;
    String expectedMessage = String.format("404 NOT_FOUND \"Could not find book with ID %d\"", id);
    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> bookController.update(id, new BookPatchDto()))
        .withMessage(expectedMessage);
  }

  @Test
  void getGenres_alwaysReturnsNonEmptyArray() {
    assertThat(bookController.getGenres()).isNotEmpty();
  }

  @Test
  void delete_returnsNotFound_ifBookDoesNotExist() {
    // given
    when(mockedBookService.findById(any(Long.class))).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> response = bookController.delete(1L);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void delete_returnsOk_ifBookExists() {
    // given
    when(mockedBookService.findById(any(Long.class))).thenReturn(Optional.of(new Book()));

    // when
    ResponseEntity<String> response = bookController.delete(1L);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
