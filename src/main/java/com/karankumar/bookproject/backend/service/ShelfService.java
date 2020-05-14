package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.RatingScale;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.ShelfRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ShelfService extends BaseService<Shelf, Long> {
    private static final Logger LOGGER = Logger.getLogger(ShelfService.class.getSimpleName());

    private BookRepository bookRepository;
    private ShelfRepository shelfRepository;

    public ShelfService(BookRepository bookRepository, ShelfRepository shelfRepository) {
        this.bookRepository = bookRepository;
        this.shelfRepository = shelfRepository;
    }

    @Override
    public Shelf findById(Long id) {
        return shelfRepository.getOne(id);
    }

    @Override
    public void save(Shelf shelf) {
        if (shelf != null) {
            shelfRepository.save(shelf);
        } else {
            LOGGER.log(Level.SEVERE, "Null Shelf");
        }
    }

    public List<Shelf> findAll() {
        return shelfRepository.findAll();
    }

    @Override
    public void delete(Shelf shelf) {
        shelfRepository.delete(shelf);
    }

    @PostConstruct
    public void populateTestData() {
        if (bookRepository.count() == 0) {
            bookRepository.saveAll(
                Stream.of("Harry Potter and the Philosopher's stone", "Harry Potter and the Order of Phoenix",
                        "Harry Potter and the Deathly Hallows")
                    .map(title -> {
                        Book book = new Book();
                        book.setTitle(title);
                        book.setGenre(Genre.FANTASY);
                        book.setNumberOfPages(300);
                        book.setDateStartedReading(LocalDate.now().minusDays(1));
                        book.setDateFinishedReading(LocalDate.now());
                        book.setRating(RatingScale.TEN);

                        return book;
                    }).collect(Collectors.toList()));

            System.out.println("Saved book");
        }

        System.out.println("Books: " + bookRepository.count());

        if (shelfRepository.count() == 0) {
            List<Book> books = bookRepository.findAll();
            shelfRepository.saveAll(
                    Stream.of("Want to read", "Currently reading", "Read")
                        .map(name -> {
                            Shelf shelf = new Shelf(name);
                            shelf.setBooks(books);
                            return shelf;
                    }).collect(Collectors.toList()));
        }
    }
}
