package com.example.demo.service;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.User;
import com.example.demo.rep.AuthorRepository;
import com.example.demo.rep.BookRepository;
import com.example.demo.rep.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Книгу не знайдено"));
    }
    @Transactional
    public void saveBook(Book book) {

        bookRepository.save(book);
    }
    @Transactional
    public void updateBook(Long id, Book updatedBook) {
        Book book = getBookById(id);
        book.setTitle(updatedBook.getTitle());
        book.setPrice(updatedBook.getPrice());
        book.setDescription(updatedBook.getDescription());
        book.getAuthor().setName(updatedBook.getAuthor().getName());
        bookRepository.save(book);
    }
public User getUserByPrincipal(Principal principal){
        if(principal==null) return new User();
        return userRepository.findByEmail(principal.getName());
}
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
