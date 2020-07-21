package com.karankumar.bookproject.backend.service;

import java.util.List;
import java.util.logging.Level;

import org.springframework.stereotype.Service;

import com.karankumar.bookproject.backend.entity.Tag;
import com.karankumar.bookproject.backend.repository.TagRepository;

import lombok.extern.java.Log;

@Service
@Log
public class TagService extends BaseService<Tag, Long> {

    private TagRepository tagRepository;
    
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag findById(Long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public void save(Tag tag) {
        if (tag != null) {
            tagRepository.save(tag);
        }else {
            LOGGER.log(Level.SEVERE, "Null tag");
        }
    }

    public Long count() {
        return tagRepository.count();
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }

    @Override
    public void deleteAll() {
        tagRepository.deleteAll();
    }
}
