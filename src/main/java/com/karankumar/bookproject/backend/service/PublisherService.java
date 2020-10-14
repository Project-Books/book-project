package com.karankumar.bookproject.backend.service;

import java.util.List;
import java.util.logging.Level;

import org.springframework.stereotype.Service;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Publisher;
import com.karankumar.bookproject.backend.repository.PublisherRepository;

import lombok.NonNull;
import lombok.extern.java.Log;

@Service
@Log
public class PublisherService {
    
    private final PublisherRepository publisherRepository;
    
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }
    
    public Publisher findById(Long id) {
        return publisherRepository.getOne(id);
         
    }
    
    public void save(@NonNull Publisher publisher) {
        if (publisher.getId() != null) {
            Publisher existingPublisher = findById(publisher.getId());
            if (existingPublisher != null) {
                LOGGER.log(Level.INFO, "Matching PublisherIds: " + existingPublisher.getId());
                publisher = new Publisher(publisher.getName());
            }
        }

        publisherRepository.save(publisher);
    }

    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }
    
    public void delete(Publisher publisher) {
        publisherRepository.delete(publisher);
    }

    public void deleteAll() {
        publisherRepository.deleteAll();
    }

    public Long count() {
        return publisherRepository.count();
    }
    

}
