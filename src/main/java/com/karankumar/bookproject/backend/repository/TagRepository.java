package com.karankumar.bookproject.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karankumar.bookproject.backend.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
