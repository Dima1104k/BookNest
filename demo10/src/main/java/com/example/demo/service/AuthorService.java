package com.example.demo.service;

import com.example.demo.models.Author;
import com.example.demo.rep.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
  /*  public Author findByName(String authorName) {
        Author existingAuthor = authorRepository.findByName(authorName);
        if (existingAuthor!=null) {
            // Якщо автор знайдений, повертаємо його
            return existingAuthor;
        } else {
            // Якщо автора немає, створюємо нового
            Author newAuthor = new Author();
            newAuthor.setName(authorName);
            return authorRepository.save(newAuthor);
        }*/
  public void saveAuthor(Author author) {
      authorRepository.save(author);
  }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    public Author findAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }
}
