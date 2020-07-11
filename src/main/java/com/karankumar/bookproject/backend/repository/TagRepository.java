package com.karankumar.bookproject.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karankumar.bookproject.backend.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
