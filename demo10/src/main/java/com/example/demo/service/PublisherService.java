package com.example.demo.service;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genres;
import com.example.demo.models.Publisher;
import com.example.demo.rep.BookRepository;
import com.example.demo.rep.PublisherRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    @Transactional
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Transactional
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Видавництва не знайдено"));

        for (Book book : publisher.getBooks()) {
            book.setPublisher(null);
            bookRepository.save(book);
        }

        publisherRepository.deleteById(id);
    }

    @Transactional
    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean savePublisher(Publisher publisher) {

        if (publisherRepository.existsByName(publisher.getName())) {
            return false;
        }

        publisherRepository.save(publisher);
        return true;
    }

    @Transactional
    public void updatedPublisher(Long id, Publisher updatedGenres) {
        Publisher publisher = getPublisherById(id);
        publisher.setName(updatedGenres.getName());
        publisher.setWebsite(updatedGenres.getWebsite());
        publisher.setAddress(updatedGenres.getAddress());
        publisher.setPhone(updatedGenres.getPhone());
        publisherRepository.save(publisher);
    }
}
