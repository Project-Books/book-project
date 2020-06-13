package com.karankumar.bookproject.backend.model.shelves;

import javax.persistence.Entity;

@Entity
public class ReadingShelf extends PredefinedShelf {
   public ReadingShelf() {
      super(ShelfName.READING);
   }
}
