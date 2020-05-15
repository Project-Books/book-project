package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.List;

public class BookForm extends VerticalLayout {
    private TextField bookTitle = new TextField();
    private TextField bookAuthor = new TextField();
    private MultiselectComboBox<String> shelf = new MultiselectComboBox<>();
    private ComboBox<Genre> bookGenre = new ComboBox<>();
    private IntegerField pageCount = new IntegerField();
    private DatePicker dateStartedReading = new DatePicker();
    private DatePicker dateFinishedReading = new DatePicker();
    private TextArea favouriteQuote = new TextArea();
    private NumberField rating = new NumberField();

    private static final String ENTER_DATE = "Enter a date";

    Binder<Book> binder = new BeanValidationBinder<>(Book.class);
    private Button addBook = new Button();
    private Button reset  = new Button();
    private Button delete = new Button();

    public BookForm(BookService bookService, ShelfService shelfService) {
        configureBinder();
//        binder.bindInstanceFields(this);

        configureTitle();
        configureAuthor();
        configureShelf(shelfService);
        configureGenre();
        configurePageCount();
        configureDateStarted();
        configureDateFinished();
        configureQuote();
        configureRating();

        HorizontalLayout buttons = configureButtons();

        HasSize[] components = {
                bookTitle,
                bookAuthor,
                dateStartedReading,
                dateFinishedReading,
                bookGenre,
                shelf,
                pageCount,
                rating,
                favouriteQuote,
        };
        setComponentMinWidth(components);

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        form.addFormItem(bookTitle, "Book title *");
        form.addFormItem(bookAuthor, "Book author *");
        form.addFormItem(shelf, "Book shelf *");
        form.addFormItem(dateStartedReading, "Date started");
        form.addFormItem(dateFinishedReading, "Date finished");
        form.addFormItem(bookGenre, "Book genre");
        form.addFormItem(pageCount, "Page count");
        form.addFormItem(rating, "Book rating");
        form.addFormItem(favouriteQuote, "Favourite quote");
        form.add(buttons);

        VerticalLayout booksInShelf = new BooksInShelf(bookService, shelfService);

        add(booksInShelf, form);
    }

    private void configureBinder() {
        binder.forField(bookTitle)
                .bind(Book::getTitle, Book::setTitle);
        binder.forField(rating)
                .withConverter(new DoubleToRatingScaleConverter())
                .bind(Book::getRating, Book::setRating);
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
        binder.setBean(book);
    }

    private void configureTitle() {
        bookTitle.setPlaceholder("Enter a book title");
        bookTitle.setClearButtonVisible(true);
        bookTitle.setRequired(true);
        bookTitle.setRequiredIndicatorVisible(true);
    }

    private void configureAuthor() {
        bookAuthor.setPlaceholder("Enter a book author");
        bookAuthor.setClearButtonVisible(true);
        bookAuthor.setRequired(true);
        bookAuthor.setRequiredIndicatorVisible(true);
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

    private void configureQuote() {
        favouriteQuote.setPlaceholder("Enter favourite quote");
        favouriteQuote.setClearButtonVisible(true);
    }

    private void clearForm() {
        bookTitle.clear();
        bookAuthor.clear();
        shelf.clear();
        bookGenre.clear();
        pageCount.clear();
        dateStartedReading.clear();
        dateFinishedReading.clear();
        favouriteQuote.clear();
        rating.clear();
    }

    private void setComponentMinWidth(HasSize[] components) {
        for (HasSize h : components) {
            h.setMinWidth("15em");
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
