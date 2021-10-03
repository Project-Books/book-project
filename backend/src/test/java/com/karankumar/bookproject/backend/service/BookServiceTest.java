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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        AuthorService authorService = mock(AuthorService.class);
        PublisherService publisherService = mock(PublisherService.class);
        PredefinedShelfService predefinedShelfService = mock(PredefinedShelfService.class);
        bookService = new BookService(bookRepository, authorService, publisherService, predefinedShelfService);
    }

    @Test
    void findById_throwsException_ifIdIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> bookService.findById(null));
        verify(bookRepository, never()).findById(anyLong());
    }

    @Test
    void canFindByNonNullId() {
        bookService.findById(1L);
        verify(bookRepository).findBookById(anyLong());
    }

    @Test
    void save_throwsException_ifBookIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> bookService.save(null));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void save_returnsEmpty_ifBookDoesNotHaveAuthor() {
        // given
        User user = User.builder().build();
        PredefinedShelf predefinedShelf = new PredefinedShelf(PredefinedShelf.ShelfName.READ, user);
        Book book = new Book(
                "title",
                null,
                predefinedShelf
        );

        // when
        Optional<Book> actual = bookService.save(book);

        // then
        assertThat(actual).isEmpty();
        verify(bookRepository, never()).save(book);
    }

    @Test
    // TODO: fix test
    @Disabled
    void canSaveIfBookHasAuthorAndPredefinedShelf() {
        // given
        User user = User.builder().build();
        PredefinedShelf predefinedShelf = new PredefinedShelf(PredefinedShelf.ShelfName.READ, user);
        Book book = new Book(
                "title",
                new Author("test"),
                predefinedShelf
        );

        // when
        Optional<Book> actual = bookService.save(book);

        // then
        assertThat(actual).isNotEmpty();

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.capture();
        assertThat(capturedBook).isEqualTo(book);
    }

    @Test
    void canCount() {
        bookService.count();
        verify(bookRepository).count();
    }

    @Test
    void canFindAll() {
        bookService.findAll();
        verify(bookRepository).findAllBooks();
    }

    @Test
    void findAll_searchesWithoutFilter_ifFilterIsNull() {
        bookService.findAll(null);
        verify(bookRepository).findAllBooks();
    }

    @Test
    void findAll_searchesWithoutFilter_ifFilterIsEmpty() {
        bookService.findAll("");
        verify(bookRepository).findAllBooks();
    }

    @Test
    void findAll_filtersByTitle_ifFilterIsNotNullOrEmpty() {
        // given
        String filter = "test";

        // when
        bookService.findAll(filter);

        // then
        verify(bookRepository).findByTitleContainingIgnoreCase(filter);
    }

    @Test
    void delete_throwsException_ifBookIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> bookService.delete(null));
    }

    @Test
    void canDeleteAll() {
        bookService.deleteAll();
        verify(bookRepository).deleteAll();
    }

//    @Test
//    void canFindByTitleOrAuthor() {
//        bookService.findByTitleOrAuthor("test", "author");
//        verify(bookRepository).findByTitleOrAuthor(anyString(), anyString());
//    }
}
