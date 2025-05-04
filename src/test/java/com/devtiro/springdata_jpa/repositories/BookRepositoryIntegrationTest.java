package com.devtiro.springdata_jpa.repositories;

import com.devtiro.springdata_jpa.TestDataUtil;
import com.devtiro.springdata_jpa.domains.Author;
import com.devtiro.springdata_jpa.domains.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled(){
        Author author1 = authorRepository.save(TestDataUtil.createTestAuthorA());
        Book book1 = TestDataUtil.createTestBookA(author1);
        underTest.save(book1);

        Author author2 = authorRepository.save(TestDataUtil.createTestAuthorB());
        Book book2 = TestDataUtil.createTestBookB(author2);
        underTest.save(book2);

        Author author3 = authorRepository.save(TestDataUtil.createTestAuthorC());
        Book book3 = TestDataUtil.createTestBookC(author3);
        underTest.save(book3);

        Iterable<Book> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(book1, book2, book3);
    }

    @Test
    public void testThatBookCanBeUpdated(){
        Author author = authorRepository.save(TestDataUtil.createTestAuthorA());

        Book bookA =  TestDataUtil.createTestBookA(author);
        underTest.save(bookA);

        bookA.setTitle("Updated");
        underTest.save(bookA);
        // System.out.println(bookA.toString());

        Optional<Book> result = underTest.findById(bookA.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookA);
    }

    @Test
    public void testThatBookCanBeDeleted(){
        Author authorA = authorRepository.save(TestDataUtil.createTestAuthorA());

        Book bookA =  TestDataUtil.createTestBookA(authorA);
        underTest.save(bookA);

        // Verify that book exists before deletion (optional)
        Optional<Book> beforeDelete = underTest.findById(bookA.getIsbn());
        assertThat(beforeDelete).isPresent();
        assertThat(beforeDelete.get()).isEqualTo(bookA);

        System.out.println(bookA.toString());

        underTest.deleteById(bookA.getIsbn());

        Optional<Book> result = underTest.findById(bookA.getIsbn());
        assertThat(result).isEmpty();
    }
}
