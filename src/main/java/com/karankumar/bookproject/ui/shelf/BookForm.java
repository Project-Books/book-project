package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

/**
 * A Vaadin form for adding a new {@code Book}
 */
public class BookForm extends FormLayout {
    private TextField bookTitle = new TextField();
    private TextField authorFirstName = new TextField();
    private TextField authorLastName = new TextField();
//    private MultiselectComboBox<String> shelf = new MultiselectComboBox<>();
    private ComboBox<String> shelf = new ComboBox<>();
    private ComboBox<Genre> bookGenre = new ComboBox<>();
    private IntegerField pageCount = new IntegerField();
    private DatePicker dateStartedReading = new DatePicker();
    private DatePicker dateFinishedReading = new DatePicker();
//    private TextArea favouriteQuote = new TextArea();
    private NumberField rating = new NumberField();

    private static final String ENTER_DATE = "Enter a date";

    Binder<Book> binder = new BeanValidationBinder<>(Book.class);
    private Button addBook = new Button();
    private Button reset  = new Button();
    private Button delete = new Button();

    public BookForm(ShelfService shelfService) {
        configureBinder();

        configureTitle();
        configureAuthor();
        configureShelf(shelfService);
        configureGenre();
        configurePageCount();
        configureDateStarted();
        configureDateFinished();
//        configureQuote();
        configureRating();

        HorizontalLayout buttons = configureButtons();

        HasSize[] components = {
                bookTitle,
                authorFirstName,
                authorLastName,
                dateStartedReading,
                dateFinishedReading,
                bookGenre,
                shelf,
                pageCount,
                rating,
//                favouriteQuote,
        };
        setComponentMinWidth(components);

        setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        addFormItem(bookTitle, "Book title *");
        addFormItem(authorFirstName, "Author's first name *");
        addFormItem(authorLastName, "Author's last name *");
        addFormItem(shelf, "Book shelf *");
        addFormItem(dateStartedReading, "Date started");
        addFormItem(dateFinishedReading, "Date finished");
        addFormItem(bookGenre, "Book genre");
        addFormItem(pageCount, "Page count");
        addFormItem(rating, "Book rating");
//        addFormItem(favouriteQuote, "Favourite quote");
        add(buttons);
    }

    private void configureBinder() {
        binder.forField(bookTitle)
                .asRequired("Please provide a book title")
                .bind(Book::getTitle, Book::setTitle);
        binder.forField(authorFirstName)
                .withConverter(new StringToAuthorFirstNameConverter())
                .bind(Book::getAuthor, Book::setAuthor);
        binder
                .forField(authorLastName)
                .withConverter(new StringToAuthorLastNameConverter())
                .bind(Book::getAuthor, Book::setAuthor);

//        binder.forField(shelf)
//                .withConverter(new StringToShelfConverter())
//                .bind(Book::getShelves, Book::setShelves);
        binder.forField(dateStartedReading)
                .bind(Book::getDateStartedReading, Book::setDateStartedReading);
        binder.forField(dateFinishedReading)
                .bind(Book::getDateFinishedReading, Book::setDateFinishedReading);
        binder.forField(pageCount)
                .bind(Book::getNumberOfPages, Book::setNumberOfPages);
        binder.forField(bookGenre)
                .bind(Book::getGenre, Book::setGenre);
        binder.forField(rating)
                .withConverter(new DoubleToRatingScaleConverter())
                .bind(Book::getRating, Book::setRating);

//        binder.forField(favouriteQuote)
//                .bind(Book::getFavouriteQuote, Book::setFavouriteQuote);
    }

    private HorizontalLayout configureButtons() {
        addBook.setText("Add book");
        addBook.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addBook.addClickListener(click -> validateOnSave());

        reset.setText("Reset");
        reset.addClickListener(event -> clearForm());

        delete = new Button();
        delete.setText("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        binder.addStatusChangeListener(event -> addBook.setEnabled(binder.isValid()));

        return new HorizontalLayout(addBook, reset, delete);
    }

    private void validateOnSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setBook(Book book) {
        System.out.println("is the book null? " + (book == null));
        System.out.println("is the rating null? " + (book.getRating() == null));
        System.out.println("is the author's first name null? " + (book.getAuthor().getFirstName() == null));
        System.out.println("is the author's last name null? " + (book.getAuthor().getLastName() == null));
        System.out.println("is the date started null? " + (book.getDateStartedReading() == null));
        System.out.println("is the date finished null? " + (book.getDateFinishedReading() == null));
        System.out.println("is the genre null? " + (book.getGenre() == null));
        binder.setBean(book);
    }

    private void configureTitle() {
        bookTitle.setPlaceholder("Enter a book title");
        bookTitle.setClearButtonVisible(true);
        bookTitle.setRequired(true);
        bookTitle.setRequiredIndicatorVisible(true);
    }

    private void configureAuthor() {
        authorFirstName.setPlaceholder("Enter the author's first name");
        authorFirstName.setClearButtonVisible(true);
        authorFirstName.setRequired(true);
        authorFirstName.setRequiredIndicatorVisible(true);

        authorLastName.setPlaceholder("Enter the author's last name");
        authorLastName.setClearButtonVisible(true);
        authorLastName.setRequired(true);
        authorLastName.setRequiredIndicatorVisible(true);
    }

    private void configureGenre() {
        bookGenre.setItems(Genre.values());
        bookGenre.setPlaceholder("Choose a book genre");
    }

    private void configureShelf(ShelfService shelfService) {
        shelf.setRequired(true);
        shelf.setPlaceholder("Choose a shelf");
        shelf.setClearButtonVisible(true);

        List<Shelf> shelves = shelfService.findAll();
        shelf.setItems(shelves.stream().map(Shelf::getName));
    }

    private void configureRating() {
        rating.setHasControls(true);
        rating.setMin(0);
        rating.setMax(10);
        rating.setStep(0.5f);
        rating.setClearButtonVisible(true);
    }

    private void configureDateStarted() {
        dateStartedReading.setClearButtonVisible(true);
        dateStartedReading.setPlaceholder(ENTER_DATE);
    }

    private void configureDateFinished() {
        dateFinishedReading.setClearButtonVisible(true);
        dateFinishedReading.setPlaceholder(ENTER_DATE);
    }

    private void configurePageCount() {
        pageCount.setMin(1);
        pageCount.setHasControls(true);
        pageCount.setClearButtonVisible(true);
    }

    /*
    private void configureQuote() {
        favouriteQuote.setPlaceholder("Enter favourite quote");
        favouriteQuote.setClearButtonVisible(true);
    }
     */

    private void clearForm() {
        HasValue[] components = {
                bookTitle,
                authorFirstName,
                authorLastName,
                shelf,
                bookGenre,
                pageCount,
                dateStartedReading,
                dateFinishedReading,
                rating,
        };
        for (HasValue component : components) {
            if (component != null && !component.isEmpty()) {
                component.clear();
            }
        }
    }

    private void setComponentMinWidth(HasSize[] components) {
        for (HasSize h : components) {
            h.setMinWidth("23em");
        }
    }

    // Events
    public static abstract class BookFormEvent extends ComponentEvent<BookForm> {
        private Book book;

        protected BookFormEvent(BookForm source, Book book) {
            super(source, false);
            this.book = book;
        }

        public Book getBook() {
            return book;
        }
    }

    public static class SaveEvent extends BookFormEvent {
        SaveEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public static class DeleteEvent extends BookFormEvent {
        DeleteEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
