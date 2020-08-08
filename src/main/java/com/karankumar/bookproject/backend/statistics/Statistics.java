package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;

import java.util.Set;

public class Statistics {
    protected final Set<Book> readShelfBooks;

    public Statistics(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();
    }
}
