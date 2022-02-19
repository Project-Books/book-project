/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2022  Karan Kumar
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.bookexport;

import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.book.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookExportServiceTest {

  @Mock BookRepository bookRepository;

  @InjectMocks BookExportService bookExportService;

  @Test
  void getAllBooksData() {
    // given
    User user = mock(User.class);
    Book book = mock(Book.class);
    when(bookRepository.findAllBooksForUser(eq(user))).thenReturn(singletonList(book));

    // when
    ExportDto booksData = bookExportService.exportBooksData(user);

    // then
    assertSoftly(
        softly -> {
          softly.assertThat(booksData).isNotNull();
          softly.assertThat(booksData.getBooks().size()).isOne();
          softly.assertThat(booksData.getBooks().get(0)).isEqualTo(book);
        });
  }

  @Test
  void returnEmpty_whenNoBooksForUser() {
    // given
    User user = mock(User.class);
    when(bookRepository.findAllBooksForUser(eq(user))).thenReturn(emptyList());

    // when
    ExportDto booksData = bookExportService.exportBooksData(user);

    // then
    assertSoftly(
        softly -> {
          softly.assertThat(booksData).isNotNull();
          softly.assertThat(booksData.getBooks()).isEmpty();
        });
  }
}
