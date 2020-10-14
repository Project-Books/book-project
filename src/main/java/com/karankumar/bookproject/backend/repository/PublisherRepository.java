package com.karankumar.bookproject.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karankumar.bookproject.backend.entity.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long>{

}
