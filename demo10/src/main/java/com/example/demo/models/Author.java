package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="author")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "books")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "biography", columnDefinition = "text")
    private String biography;
    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();
}
