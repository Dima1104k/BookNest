package com.example.demo.controlles;

import com.example.demo.models.*;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenresService genresService;
    private final PublisherService publisherService;

    @GetMapping("/book")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book-list";
    }

    @GetMapping("/book/new")
    public String createBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("genres", genresService.getAllGenres());
        model.addAttribute("publishers", publisherService.getAllPublishers());
        return "book-create";
    }
    @PostMapping("/book/new")
    public String createBook(@RequestParam String title,
                             @RequestParam int price,
                             @RequestParam String description,
                             @RequestParam String photoUrl,
                             @RequestParam Long authorId,
                             @RequestParam Long genresId,
                             @RequestParam Long publishersId) {
        Author author = authorService.findAuthorById(authorId);
        Genres genres = genresService.getGenresById(genresId);
        Publisher publisher = publisherService.getPublisherById(publishersId);
        Book book = new Book();
        book.setTitle(title);
        book.setPrice(price);
        book.setDescription(description);
        book.setAuthor(author);
        book.setGenre(genres);
        book.setPhotoUrl(photoUrl);
        book.setPublisher(publisher);
        bookService.saveBook(book);
        return "redirect:/book";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Book> books = bookService.getAllBooks();
        Collections.shuffle(books);
        model.addAttribute("books", books);
        return "index";
    }

    @PostMapping("/book/delete/{id}")
    public String delete(@PathVariable Long id){
        bookService.deleteBook(id);
        return "redirect:/book";
    }

    @GetMapping("/book/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        List<Author> authors = authorService.getAllAuthors();
        List<Genres> genres = genresService.getAllGenres();
        List<Publisher> publishers = publisherService.getAllPublishers();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("publishers", publishers);
        return "book-edit";
    }

    @PostMapping("/book/edit/{id}")
    public String updateBook(@PathVariable Long id, @RequestParam String title, @RequestParam int price,
                             @RequestParam String description,
                             @RequestParam String photoUrl,
                             @RequestParam Long authorId,
                             @RequestParam Long genreId,
                             @RequestParam Long publishersId) {
        Book updatedBook = bookService.getBookById(id);
        Author author = authorService.findAuthorById(authorId);
        Genres genre = genresService.getGenresById(genreId);
        Publisher publisher = publisherService.getPublisherById(publishersId);
        updatedBook.setTitle(title);
        updatedBook.setPrice(price);
        updatedBook.setDescription(description);
        updatedBook.setPhotoUrl(photoUrl);
        updatedBook.setGenre(genre);
        updatedBook.setAuthor(author);
        updatedBook.setPublisher(publisher);
        bookService.updateBook(id,updatedBook);
        return "redirect:/book";
    }

    @GetMapping("/books-list")
    public String listBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "book-list-users";
    }
}