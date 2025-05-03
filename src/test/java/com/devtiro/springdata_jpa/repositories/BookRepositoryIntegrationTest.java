package com.devtiro.springdata_jpa.repositories;

import com.devtiro.springdata_jpa.TestDataUtil;
import com.devtiro.springdata_jpa.domain.Author;
import com.devtiro.springdata_jpa.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BookRepositoryIntegrationTest {
    AuthorRepository authorRepository;
    BookRepository underTest;

    @Autowired
    public BookRepositoryIntegrationTest(AuthorRepository authorRepository, BookRepository underTest) {
        this.authorRepository = authorRepository;
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled(){
        Author author = TestDataUtil.createTestAuthorA();
        Author savedAuthor = authorRepository.save(author);
        Book book = TestDataUtil.createTestBookA(savedAuthor);

        underTest.save(book);
        Optional<Book> result = underTest.findById(book.getIsbn()); // retrieves from DB
        assertThat(result).isPresent(); // checks that something was returned
        assertThat(result.get()).isEqualTo(book); // checks it matches the input

    }
}
