package com.example.demo.controlles;
import com.example.demo.models.Book;
import com.example.demo.models.Genres;
import com.example.demo.service.GenresService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class GenresController {
    private final GenresService genresService;

    @GetMapping("/genres/new")
    public String createGenres(Model model) {
         model.addAttribute("genres", new Genres());
        return "genres-form";
    }

    @PostMapping("/genres/new")
    public String createGenre(@RequestParam String name, @RequestParam String description, Model model) {
       Genres genres = new Genres();
       genres.setName(name);
       genres.setDescription(description);
        if(!genresService.saveGenre(genres)) {
            model.addAttribute("error", "Жанр: " + genres.getName() + " уже існує");
            return "genres-form";
        }
            genresService.saveGenre(genres);
       return "redirect:/genres/list";
    }
    @GetMapping("/genres/list")
    public String listGenres(Model model) {
        model.addAttribute("genres", genresService.getAllGenres());
        return "genres-list";
    }
    @PostMapping("/genre/delete/{id}")
    public String delete(@PathVariable Long id){
       genresService.deleteBook(id);
        return "redirect:/genres/list";
    }
    @GetMapping("/genre/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Genres genres = genresService.getGenresById(id);
        model.addAttribute("genres", genres);
        return "genre-edit";
    }

    @PostMapping("/genre/edit/{id}")
    public String updateGenre(@PathVariable Long id, @RequestParam String name, @RequestParam String description) {
        Genres updatedGenre= genresService.getGenresById(id);
        updatedGenre.setName(name);
        updatedGenre.setDescription(description);
        genresService.updateGenres(id,updatedGenre);
        return "redirect:/genres/list";
    }

}
