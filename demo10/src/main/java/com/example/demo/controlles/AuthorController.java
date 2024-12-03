package com.example.demo.controlles;

import com.example.demo.models.Author;
import com.example.demo.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/author/new")
    public String createAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "author-form";
    }

    @PostMapping("/author/new")
    public String saveAuthor(@RequestParam String name,@RequestParam String biography) {
        Author author = new Author();
        author.setName(name);
        author.setBiography(biography);
        authorService.saveAuthor(author);
        return "redirect:/author/list";
    }
    @GetMapping("/author/list")
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "author-list";
    }

    @GetMapping("/author/edit/{id}")
    public String editAuthorForm(@PathVariable Long id, Model model) {
        Author author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        return "author-edit";
    }

    @PostMapping("/author/edit/{id}")
    public String updateAuthor(@PathVariable Long id, @RequestParam String name,@RequestParam String biography ) {
        Author author = authorService.findAuthorById(id);
        author.setName(name);
        author.setBiography(biography);
        authorService.saveAuthor(author);
        return "redirect:/author/list";
    }


}
