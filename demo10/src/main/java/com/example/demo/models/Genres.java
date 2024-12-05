package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="genres")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Genres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(name = "description", columnDefinition = "text",length = 255)
    private String description;
    @OneToMany(mappedBy = "genre")
    private List<Book> books = new ArrayList<>();
}