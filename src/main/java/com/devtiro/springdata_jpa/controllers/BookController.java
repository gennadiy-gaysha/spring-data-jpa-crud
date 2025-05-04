package com.devtiro.springdata_jpa.controllers;

import com.devtiro.springdata_jpa.domains.Author;
import com.devtiro.springdata_jpa.domains.Book;
import com.devtiro.springdata_jpa.repositories.AuthorRepository;
import com.devtiro.springdata_jpa.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Long authorId = book.getAuthor().getId();
        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        book.setAuthor(existingAuthor); // attach the real Author

        Book savedBook = bookRepository.save(book);
        URI location = URI.create("/books/" + savedBook.getIsbn());
        return ResponseEntity.created(location).body(savedBook);
    }


    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBook(@PathVariable String isbn) {
        return bookRepository.findById(isbn)
                .map(ResponseEntity::ok) // 200 OK
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    @GetMapping
    public ResponseEntity<Iterable<Book>> getAllBooks() {
        Iterable<Book> books = bookRepository.findAll(); // Fetch all books from the repository
        return ResponseEntity.ok(books); // Wrap in a 200 OK response with the list of books
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Book> updateBook(@PathVariable String isbn, @RequestBody Book updatedBook) {
        return bookRepository.findById(isbn)
                .map(existingBook -> {
                    existingBook.setTitle(updatedBook.getTitle());

                    Long authorId = updatedBook.getAuthor().getId();
                    Author author = authorRepository.findById(authorId)
                            .orElseThrow(() -> new RuntimeException("Author not found"));

                    existingBook.setAuthor(author);

                    Book savedBook = bookRepository.save(existingBook);
                    return ResponseEntity.ok(savedBook);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        if (bookRepository.existsById(isbn)) {
            bookRepository.deleteById(isbn);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}

