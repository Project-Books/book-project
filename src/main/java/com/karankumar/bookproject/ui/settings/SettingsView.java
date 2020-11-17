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

package com.karankumar.bookproject.ui.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karankumar.bookproject.backend.dto.GoodreadsBookImport;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.CsvUtils;
import com.karankumar.bookproject.backend.util.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.components.dialog.ResetShelvesDialog;
import com.karankumar.bookproject.ui.components.toggle.SwitchToggle;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings | Book Project")
@Log
public class SettingsView extends HorizontalLayout {

    private static final String ENABLE_DARK_MODE = "Enable dark mode";
    private static final String APPEARANCE = "Appearance:";
    private static final String MY_BOOKS = "My books:";
    private static final String DISABLE_DARK_MODE = "Disable dark mode";

    private static SwitchToggle darkModeToggle;
    private static boolean darkModeOn = false;
    private static final Label darkModeLabel = new Label(ENABLE_DARK_MODE);

    private static final H3 appearanceHeading = new H3(APPEARANCE);
    private static final H3 myBooksHeading = new H3(MY_BOOKS);
    private static final HtmlComponent lineBreak = new HtmlComponent("br");

    // Clear Shelves
    private ResetShelvesDialog resetShelvesDialog;
    private static final String CLEAR_SHELVES = "Reset shelves";

    private static final String EXPORT_BOOKS = "Export";
    private static final Anchor exportBooksAnchor = new Anchor();

    private static final String IMPORT_FROM_GOODREADS = "Import from Goodreads";
    private static final String UPLOAD_DROP_LABEL = "Upload a file in .csv format";
    private static final MemoryBuffer importGoodreadsMemoryBuffer = new MemoryBuffer();
    private static final Upload importGoodreadsUpload = new Upload(importGoodreadsMemoryBuffer);

    private static BookService bookService;
    private final transient PredefinedShelfService predefinedShelfService;
    private final transient CustomShelfService customShelfService;

    static {
        configureDarkModeToggle();
        createExportBooksAnchor();
        createImportGoodreadsAnchor();
    }

    private static void configureDarkModeToggle() {
        darkModeToggle = new SwitchToggle();
        darkModeToggle.addClickListener(e -> {

            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                darkModeOn = false;
            } else {
                themeList.add(Lumo.DARK);
                darkModeOn = true;
            }
            updateDarkModeLabel();
        });
    }

    private static void createExportBooksAnchor() {
        exportBooksAnchor.getElement().setAttribute("download", true);
        exportBooksAnchor.add(new Button(EXPORT_BOOKS, new Icon(VaadinIcon.DOWNLOAD_ALT)));
    }

    private static void createImportGoodreadsAnchor() {
        importGoodreadsUpload.setMaxFiles(1);
        importGoodreadsUpload.setAcceptedFileTypes(CsvUtils.TEXT_CSV);
        importGoodreadsUpload.getElement().setAttribute("import-goodreads", true);
        importGoodreadsUpload.setDropLabel(new Label(UPLOAD_DROP_LABEL));
        importGoodreadsUpload.setUploadButton(new Button(IMPORT_FROM_GOODREADS));
    }

    SettingsView(BookService bookService, PredefinedShelfService predefinedShelfService,
                 CustomShelfService customShelfService) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
        this.customShelfService = customShelfService;

        setDarkModeState();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(darkModeLabel, darkModeToggle);

        configureExportBooksAnchor();
        configureImportGoodreads();

        VerticalLayout verticalLayout = new VerticalLayout(
                appearanceHeading,
                horizontalLayout,
                lineBreak,
                myBooksHeading,
                createClearShelvesButton(),
                exportBooksAnchor,
                importGoodreadsUpload
        );

        verticalLayout.setAlignItems(Alignment.CENTER);

        add(verticalLayout);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private Button createClearShelvesButton() {
        return new Button(CLEAR_SHELVES, click -> {
            resetShelvesDialog = new ResetShelvesDialog(bookService);
            resetShelvesDialog.openDialog();
        });
    }

    private void setDarkModeState() {
        if (darkModeOn) {
            updateDarkModeLabel();
            darkModeToggle.setChecked(true);
        } else {
            darkModeToggle.setChecked(false);
        }
    }

    private static void updateDarkModeLabel() {
        darkModeLabel.setText(darkModeOn ? DISABLE_DARK_MODE : ENABLE_DARK_MODE);
    }

    private void configureExportBooksAnchor() {
        String jsonExportURI = generateJsonResource();
        exportBooksAnchor.setHref(jsonExportURI);
    }

    private void configureImportGoodreads() {
        importGoodreadsUpload.addSucceededListener(succeededEvent -> {
            LOGGER.info("Import success: " + succeededEvent.toString());
            try {
                List<GoodreadsBookImport> goodreadsBookImports =
                        CsvUtils.read(importGoodreadsMemoryBuffer.getInputStream(),
                                GoodreadsBookImport.class);
                goodreadsBookImports.forEach(i -> LOGGER.info(i.toString()));

                saveAll(goodreadsBookImports);
            } catch (IOException e) {
                LOGGER.severe("Error in reading input file, error: + " + e);
            }
        });
    }

    private void saveAll(List<GoodreadsBookImport> goodreadsBookImports) {
        List<Book> books = toBooks(goodreadsBookImports);
        List<Book> savedBooks = books.stream()
                                     .map(bookService::save)
                                     .filter(Optional::isPresent)
                                     .map(Optional::get)
                                     .collect(Collectors.toList());
        savedBooks.forEach(b -> LOGGER.info("Book: " + b + " saved successfully"));
    }

    private List<Book> toBooks(Collection<? extends GoodreadsBookImport> goodreadsBookImports) {
        return goodreadsBookImports.stream()
                                   .map(this::toBook)
                                   .filter(Optional::isPresent)
                                   .map(Optional::get)
                                   .collect(Collectors.toList());
    }

    private Optional<Book> toBook(GoodreadsBookImport goodreadsBookImport) {
        Optional<Author> author = toAuthor(goodreadsBookImport.getAuthor());
        if (author.isEmpty()) {
            LOGGER.severe("Author is null");
            return Optional.empty();
        }

        Optional<PredefinedShelf> predefinedShelf =
                toPredefinedShelf(goodreadsBookImport.getBookshelves(),
                        goodreadsBookImport.getDateRead());
        if (predefinedShelf.isEmpty()) {
            LOGGER.severe("Predefined shelf is null");
            return Optional.empty();
        }

        Book book = new Book(goodreadsBookImport.getTitle(), author.get(), predefinedShelf.get());

        Optional<CustomShelf> customShelf = toCustomShelf(goodreadsBookImport.getBookshelves());
        customShelf.ifPresent(book::setCustomShelf);

        if (Objects.nonNull(goodreadsBookImport.getRating())) {
            Optional<RatingScale> ratingScale =
                    RatingScale.of(goodreadsBookImport.getRating() * 2);
            ratingScale.ifPresent(book::setRating);
        }

        return Optional.of(book);
    }

    private Optional<Author> toAuthor(String name) {
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }
        String[] authorNames = name.split(" ");
        return Optional
                .of(new Author(authorNames[0], authorNames.length > 1 ? authorNames[1] : null));
    }

    private Optional<PredefinedShelf> toPredefinedShelf(String shelves, LocalDate dateRead) {
        if (Objects.nonNull(dateRead)) {
            return Optional.of(predefinedShelfService.findToReadShelf());
        }
        if (StringUtils.isBlank(shelves)) {
            return Optional.empty();
        }
        String[] shelvesArray = shelves.split(",");
        return Arrays.stream(shelvesArray)
                     .filter(PredefinedShelfUtils::isPredefinedShelf)
                     .findFirst()
                     .map(predefinedShelfService::getPredefinedShelfByNameAsString);
    }

    private Optional<CustomShelf> toCustomShelf(String shelves) {
        if (StringUtils.isBlank(shelves)) {
            return Optional.empty();
        }
        String[] shelvesArray = shelves.split(",");
        return Arrays.stream(shelvesArray)
                     .filter(Predicate.not(PredefinedShelfUtils::isPredefinedShelf))
                     .findFirst()
                     .map(customShelfService::findOrCreate);
    }

    private String generateJsonResource() {
        try {
            byte[] jsonRepresentationForBooks = bookService.getJsonRepresentationForBooksAsString()
                                                           .getBytes();

            final StreamResource resource = new StreamResource("bookExport.json",
                    () -> new ByteArrayInputStream(jsonRepresentationForBooks));
            final StreamRegistration registration = UI.getCurrent()
                                                    .getSession()
                                                    .getResourceRegistry()
                                                    .registerResource(resource);

            return registration.getResourceUri().toString();
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Cannot create json resource." + e);
            return "";
        }
    }
}
