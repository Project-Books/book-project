package com.karankumar.bookproject.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;


@Log
@Service
public class JsonImportService implements ComponentEventListener<SucceededEvent> {

    private final BookService bookService;
    private final CustomShelfService customShelfService;
    private final PredefinedShelfService predefinedShelfService;
    private final TagService tagService;
    private final UserService userService;

    public JsonImportService(BookService bookService,
                             CustomShelfService customShelfService,
                             PredefinedShelfService predefinedShelfService,
                             TagService tagService,
                             UserService userService) {
        this.bookService = bookService;
        this.customShelfService = customShelfService;
        this.predefinedShelfService = predefinedShelfService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @Transactional
    public void importJson(String json) {
        try {
            List<Book> books = bookService.readJsonRepresentationFromString(json);
            importCustomShelves(books);
            importTags(books);
            resetPredefinedShelves(books);

            books.forEach(bookService::save);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Couldn't parse JSON for import", e);
        }
    }

    private void importTags(List<Book> books) {
        for (Book book : books) {
            if (book.getTags() != null) {
                book.getTags().forEach(tagService::save);
            }
        }
    }

    private void importCustomShelves(List<Book> books) {
        for (Book book : books) {
            if (book.getCustomShelf() != null) {
                importCustomShelf(book);
            }
        }
    }

    private void resetPredefinedShelves(List<Book> books) {
        for (Book book : books) {
            PredefinedShelf.ShelfName predefinedShelfName = book.getPredefinedShelf().getPredefinedShelfName();
            PredefinedShelf shelf = predefinedShelfService.findByPredefinedShelfNameAndLoggedInUser(predefinedShelfName);
            book.setPredefinedShelf(shelf);
        }
    }

    private void importCustomShelf(Book book) {
        CustomShelf customShelf = book.getCustomShelf();
        List<CustomShelf> dbShelves = customShelfService.findAllForLoggedInUser();

        if (dbShelves.isEmpty()) {
            customShelf = new CustomShelf(customShelf.getShelfName(), userService.getCurrentUser());
            customShelfService.save(customShelf);
        } else {
            customShelf = dbShelves.get(0);
        }
        book.setCustomShelf(customShelf);
    }


    @Override
    public void onComponentEvent(SucceededEvent succeededEvent) {

        try {
            MemoryBuffer memoryBuffer = (MemoryBuffer)succeededEvent.getUpload().getReceiver();
            String content = new String(memoryBuffer.getInputStream().readAllBytes());
            this.importJson(content);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Couldn't read input file", e);
        }
    }
}

