package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.repository.ShelfRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author karan on 08/05/2020
 */
@Service
public class ShelfService extends BaseService<Shelf, Long> {

    private ShelfRepository shelfRepository;

    public ShelfService(ShelfRepository shelfRepository) {
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
        if (shelfRepository.count() == 0) {
            shelfRepository.saveAll(
                    Stream.of("Want to read", "Currently reading", "Read")
                        .map(Shelf::new).collect(Collectors.toList()));
        }
    }
}
