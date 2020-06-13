package com.karankumar.bookproject.backend.model.shelves;

import javax.persistence.Entity;

@Entity
public class ReadShelf extends PredefinedShelf {
   public ReadShelf() {
      super(ShelfName.READ);
   }
}
