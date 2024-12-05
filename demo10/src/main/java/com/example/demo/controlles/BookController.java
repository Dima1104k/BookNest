package com.example.demo.controlles;

import com.example.demo.models.*;
import com.example.demo.rep.BookRepository;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
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
/*    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "book-details";
    }*/
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
                             @RequestParam Long genresrId,
                             @RequestParam Long publishersId) {
        Author author = authorService.findAuthorById(authorId);
        Genres genres = genresService.getGenresById(genresrId);
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

       /* User user =userService.getUserByPrincipal(principal);*/
      /*  model.addAttribute("user",user);*/
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


    private List<Book> cart = new ArrayList<>();
    private Map<Long, Integer> cartQuantities = new HashMap<>();

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Long bookId, HttpSession session) {
        // Отримуємо корзину з сесії
        List<Book> cart = (List<Book>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Перевіряємо, чи книга вже є в корзині
        boolean bookAlreadyInCart = cart.stream().anyMatch(book -> book.getId().equals(bookId));
        if (!bookAlreadyInCart) {
            // Якщо книги немає в корзині, додаємо її
            Book book = bookService.getBookById(bookId); // Отримання книги із сервісу
            cart.add(book);
        }

        // Зберігаємо оновлену корзину в сесії
        session.setAttribute("cart", cart);
        return "redirect:/";
    }


    @PostMapping("/removeFromCart")
    public String removeFromCart(@RequestParam Long bookId, HttpSession session) {
        List<Book> cart = (List<Book>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        cart.removeIf(book -> book.getId().equals(bookId));
        cartQuantities.remove(bookId);

        session.setAttribute("cart", cart);
        session.setAttribute("cartQuantities", cartQuantities);

        return "redirect:/cart";
    }

    @PostMapping("/updateQuantities")
    @ResponseBody
    public Map<String, Object> updateQuantities(@RequestParam List<Long> bookIds, @RequestParam List<Integer> quantities, HttpSession session) {
        for (int i = 0; i < bookIds.size(); i++) {
            Long bookId = bookIds.get(i);
            int quantity = quantities.get(i);
            cartQuantities.put(bookId, quantity);
        }

        // Обчислюємо нову загальну суму
        List<Book> cart = (List<Book>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        double totalPrice = cart.stream()
                .mapToDouble(book -> book.getPrice() * cartQuantities.getOrDefault(book.getId(), 1))
                .sum();

        // Повертаємо JSON з оновленими даними
        Map<String, Object> response = new HashMap<>();
        response.put("totalPrice", totalPrice);
        response.put("quantities", quantities); // Для оновлення кількостей, якщо потрібно
        return response;
    }



    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        List<Book> cart = (List<Book>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        double totalPrice = cart.stream()
                .mapToDouble(book -> book.getPrice() * cartQuantities.getOrDefault(book.getId(), 1))
                .sum();

        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartQuantities", cartQuantities);

        return "cart";
    }

    @GetMapping("/books-list")
    public String listBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "book-list-users";
    }
}
