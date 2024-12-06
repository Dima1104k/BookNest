package com.example.demo.rep;

import com.example.demo.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository  extends JpaRepository<Publisher, Long> {
    boolean existsByName(String name);
}
