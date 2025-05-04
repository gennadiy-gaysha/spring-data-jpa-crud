package com.devtiro.springdata_jpa.controllers;


import com.devtiro.springdata_jpa.domains.Author;
import com.devtiro.springdata_jpa.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // CREATE (POST)
    @PostMapping
    // ResponseEntity<T> is a wrapper around the response that allows to:
    // - Return data (T),
    // - Control the HTTP status code (e.g., 200 OK, 404 Not Found, 201 Created),
    // - Optionally include headers
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);
        URI location = URI.create("/authors/" + savedAuthor.getId());
        return ResponseEntity.created(location).body(savedAuthor); // 201 Created + Location header
    }

    // READ (GET by ID)
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable Long id) {
        return authorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author updatedAuthor) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(updatedAuthor.getName());
                    author.setAge(updatedAuthor.getAge());
                    Author saved = authorRepository.save(author);
                    return new ResponseEntity<>(saved, HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }

    // LIST ALL
    @GetMapping
    public ResponseEntity<Iterable<Author>> getAllAuthors() {
        Iterable<Author> authors = authorRepository.findAll();
        return ResponseEntity.ok(authors);
    }
}
