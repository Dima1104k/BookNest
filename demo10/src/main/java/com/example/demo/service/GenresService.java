package com.example.demo.service;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genres;
import com.example.demo.rep.BookRepository;
import com.example.demo.rep.GenresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenresService {
    private final GenresRepository genresRepository;
    private final BookRepository bookRepository;
    @Transactional
    public List<Genres> getAllGenres() {
        return genresRepository.findAll();
    }

    @Transactional
    public boolean saveGenre(Genres genre) {
        if (genresRepository.existsByName(genre.getName())) {
            return false;
        }

        genresRepository.save(genre);
        return true;
    }
    @Transactional
    public void deleteBook(Long id) {
        Genres genre = genresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Жанр не знайдений"));

        for (Book book : genre.getBooks()) {
            book.setGenre(null);
            bookRepository.save(book);
        }
        genresRepository.deleteById(id);
    }
    @Transactional
    public void updateGenres(Long id, Genres updatedGenres) {
        Genres genres = getGenresById(id);
        genres.setName(updatedGenres.getName());
        genres.setDescription(updatedGenres.getDescription());
        genresRepository.save(genres);
    }
    @Transactional
    public Genres getGenresById(Long id) {
        return genresRepository.findById(id).orElseThrow(() -> new RuntimeException("Жанр не знайдено"));
    }
}
