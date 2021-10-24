package com.karankumar.bookproject.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.karankumar.bookproject.backend.controller.BookImportController;
import com.karankumar.bookproject.backend.service.ImportServiceDelegate;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class BookImportControllerTest {

  private final ImportServiceDelegate importServiceDelegate = mock(ImportServiceDelegate.class);
  private final BookImportController bookImportController = new BookImportController(
      importServiceDelegate);

  @Test
  void shouldInvokeProperImportHandler() {
    // given
    final var testFile = getTestFile();

    // when
    bookImportController.importFromLibraryThing(testFile);

    // then
    verify(importServiceDelegate, times(1)).importFromLibraryThing(any());
    verify(importServiceDelegate, never()).importFromGoodreads();
  }

  private MockMultipartFile getTestFile() {
    try {
      final var testInputStream = getClass().getResourceAsStream("/libraryThingImportSample.json");
      return new MockMultipartFile("file.json", testInputStream);
    } catch (IOException exception) {
      throw new IllegalStateException();
    }
  }

}