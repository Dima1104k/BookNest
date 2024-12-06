package com.example.demo.rep;

import com.example.demo.models.Book;
import com.example.demo.models.Genres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenresRepository extends JpaRepository<Genres, Long> {

 boolean existsByName(String name);
}