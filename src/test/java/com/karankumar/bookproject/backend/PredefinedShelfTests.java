package com.karankumar.bookproject.backend;

import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PredefinedShelfTests {

  private static PredefinedShelfService shelfService;

  @BeforeAll
  public static void setup(@Autowired PredefinedShelfService shelfService, @Autowired BookService bookService) {
    Assumptions.assumeTrue(shelfService != null && bookService != null);
    PredefinedShelfTests.shelfService = shelfService;

    bookService.deleteAll(); // reset
  }

  /**
   * A {@link com.karankumar.bookproject.backend.model.PredefinedShelf} without any books should still exist
   */
  @Test
  public void orphanShelfExists() {
    Assumptions.assumeTrue(shelfService != null);
    List<PredefinedShelf> shelves = PredefinedShelfTests.shelfService.findAll();

    Assertions.assertEquals(4, shelves.size());
    Assertions.assertEquals(shelves.get(0).getShelfName(), PredefinedShelf.ShelfName.TO_READ);
    Assertions.assertEquals(shelves.get(1).getShelfName(), PredefinedShelf.ShelfName.READING);
    Assertions.assertEquals(shelves.get(2).getShelfName(), PredefinedShelf.ShelfName.READ);
    Assertions.assertEquals(shelves.get(3).getShelfName(), PredefinedShelf.ShelfName.DID_NOT_FINISH);
  }
}
