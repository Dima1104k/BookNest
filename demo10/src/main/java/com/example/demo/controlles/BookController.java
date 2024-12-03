package com.example.demo.controlles;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.service.AuthorService;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;

    @GetMapping("/book")
    public String getAllBooks(Model model, Principal principal) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("user",bookService.getUserByPrincipal(principal) );
        return "book-list";
    }
    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "book-details";
    }
    @GetMapping("/book/new")
    public String createBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "book-create";
    }
    @PostMapping("/book/new")
    public String createBook(@RequestParam String title,
                             @RequestParam int price,
                             @RequestParam String description,
                             @RequestParam Long authorId) {
        Author author = authorService.findAuthorById(authorId);
        Book book = new Book();
        book.setTitle(title);
        book.setPrice(price);
        book.setDescription(description);
        book.setAuthor(author);
        bookService.saveBook(book);
        return "redirect:/book";
    }
    @GetMapping("/")
    public String index() {
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
        model.addAttribute("book", book);
        return "book-edit";
    }

    @PostMapping("/book/edit/{id}")
    public String updateBook(@PathVariable Long id, @RequestParam String title, @RequestParam int price, @RequestParam String description,  @RequestParam String authorName) {
        Book updatedBook = bookService.getBookById(id);
        updatedBook.setTitle(title);
        updatedBook.setPrice(price);
        updatedBook.setDescription(description);
        updatedBook.getAuthor().setName(authorName);
        bookService.updateBook(id,updatedBook);
        return "redirect:/book";
    }
}
