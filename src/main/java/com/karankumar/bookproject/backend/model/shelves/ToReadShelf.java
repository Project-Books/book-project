package com.karankumar.bookproject.backend.model.shelves;

import javax.persistence.Entity;

@Entity
public class ToReadShelf extends PredefinedShelf {
   public ToReadShelf() {
      super(ShelfName.TO_READ);
   }
}
