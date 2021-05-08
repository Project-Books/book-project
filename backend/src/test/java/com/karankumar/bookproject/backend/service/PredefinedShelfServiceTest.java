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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.PredefinedShelfRepository;
import com.karankumar.bookproject.backend.repository.PublisherRepository;
import com.karankumar.bookproject.backend.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PredefinedShelfServiceTest {
    private PredefinedShelfService underTest;
    private PredefinedShelfRepository predefinedShelfRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        BookRepository bookRepository = mock(BookRepository.class);
        AuthorRepository authorRepository = mock(AuthorRepository.class);
        predefinedShelfRepository = mock(PredefinedShelfRepository.class);
        TagRepository tagRepository = mock(TagRepository.class);
        userService = mock(UserService.class);
        PublisherRepository publisherRepository = mock(PublisherRepository.class);
        underTest = new PredefinedShelfService(
                bookRepository,
                authorRepository,
                predefinedShelfRepository,
                tagRepository,
                userService,
                publisherRepository
        );
    }

    @Test
    void findById_throwsNullPointerException_ifIdIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.findById(null));
        verify(predefinedShelfRepository, never()).findById(anyLong());
    }

    @Test
    void canFindByNonNullId() {
        underTest.findById(1L);
        verify(predefinedShelfRepository).findById(anyLong());
    }

    @Test
    void save_throwsNullPointerException_ifNullPredefinedShelf() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.save(null));
        verify(predefinedShelfRepository, never()).save(any(PredefinedShelf.class));
    }

    @Test
    void saveNonNullPredefinedShelf() {
        // given
        User user = User.builder().build();
        PredefinedShelf predefinedShelf =
                new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ, user);

        // when
        underTest.save(predefinedShelf);

        // then
        verify(predefinedShelfRepository).save(any(PredefinedShelf.class));
    }

    @Test
    void canFindAllForLoggedInUser() {
        // given
        User user = User.builder().build();
        given(userService.getCurrentUser()).willReturn(user);

        // when
        underTest.findAllForLoggedInUser();

        // then
        verify(predefinedShelfRepository).findAllByUser(any(User.class));
    }

    @Test
    void canFindToReadShelf() {
        // given
        User user = User.builder().build();
        given(userService.getCurrentUser()).willReturn(user);

        // when
        underTest.findToReadShelf();

        // then
        verify(predefinedShelfRepository).findByPredefinedShelfNameAndUser(
                eq(PredefinedShelf.ShelfName.TO_READ), any(User.class)
        );
    }

    @Test
    void canFindReadingShelf() {
        // given
        User user = User.builder().build();
        given(userService.getCurrentUser()).willReturn(user);

        // when
        underTest.findReadingShelf();

        // then
        verify(predefinedShelfRepository).findByPredefinedShelfNameAndUser(
                eq(PredefinedShelf.ShelfName.READING), any(User.class)
        );
    }

    @Test
    void canFindReadShelf() {
        // given
        User user = User.builder().build();
        given(userService.getCurrentUser()).willReturn(user);

        // when
        underTest.findReadShelf();

        // then
        verify(predefinedShelfRepository).findByPredefinedShelfNameAndUser(
                eq(PredefinedShelf.ShelfName.READ), any(User.class)
        );
    }

    @Test
    void canFindDidNotFinishShelf() {
        // given
        User user = User.builder().build();
        given(userService.getCurrentUser()).willReturn(user);

        // when
        underTest.findDidNotFinishShelf();

        // then
        verify(predefinedShelfRepository).findByPredefinedShelfNameAndUser(
                eq(PredefinedShelf.ShelfName.DID_NOT_FINISH), any(User.class)
        );
    }

    @Test
    void canCount() {
        underTest.count();
        verify(predefinedShelfRepository).count();
    }

//
//    @Test
//    void findPredefinedShelfForPredefinedShelfNameAndLoggedInUser() {
//        Optional<PredefinedShelf> shelf =
//                predefinedShelfService.findByPredefinedShelfNameAndLoggedInUser(
//                        PredefinedShelf.ShelfName.TO_READ
//                );
//        assertSoftly(softly -> {
//            assertThat(shelf).isNotEmpty();
//            softly.assertThat(shelf.get().getPredefinedShelfName())
//                  .isEqualTo(PredefinedShelf.ShelfName.TO_READ);
//            softly.assertThat(shelf.get().getUser().getEmail()).isEqualTo(TEST_USER_EMAIL);
//        });
//    }
//

//
//    @Test
//    void getPredefinedShelfNamesCorrectlyAsStrings() {
//        List<String> actualShelfNames = predefinedShelfService.getPredefinedShelfNamesAsStrings();
//        List<String> expectedShelfNames =
//                Stream.of(PredefinedShelf.ShelfName.values()).map(Enum::toString).collect(
//                        Collectors.toList());
//
//        assertSoftly(softly -> {
//            softly.assertThat(actualShelfNames).hasSize(expectedShelfNames.size());
//            softly.assertThat(expectedShelfNames).containsAll(actualShelfNames);
//        });
//    }
//
//    @Test
//    @DisplayName("return an empty set of books for a non-existent predefined shelf")
//    void returnEmptySetForNonExistentShelf() {
//        // given
//        String nonExistentShelf = "not a predefined shelf";
//
//        // when
//        Set<Book> actual = predefinedShelfService.getBooksInChosenPredefinedShelf(nonExistentShelf);
//
//        // then
//        assertThat(actual).isEmpty();
//    }
}
