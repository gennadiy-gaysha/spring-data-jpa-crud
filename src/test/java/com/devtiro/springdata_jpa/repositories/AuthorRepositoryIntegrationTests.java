package com.devtiro.springdata_jpa.repositories;

import com.devtiro.springdata_jpa.TestDataUtil;
import com.devtiro.springdata_jpa.domains.Author;
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
// Clean down the context of the previous test (refresh database before every test)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorRepositoryIntegrationTests {
    private final AuthorRepository underTest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatTheAuthorCanBeVCreatedAndRecalled(){
        Author author = TestDataUtil.createTestAuthorA();
        underTest.save(author);

        Optional<Author> result = underTest.findById(author.getId()); // retrieves from DB
        assertThat(result).isPresent(); // checks that something was returned
        assertThat(result.get()).isEqualTo(author); // checks it matches the input
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        Author author1 = TestDataUtil.createTestAuthorA();
        Author author2 = TestDataUtil.createTestAuthorB();
        Author author3 = TestDataUtil.createTestAuthorC();

        underTest.save(author1);
        underTest.save(author2);
        underTest.save(author3);

        Iterable<Author> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(author1, author2, author3);
    }

    @Test
    public void testThatAuthorCanBeUpdated(){
        // Create new author
        Author authorA = underTest.save(TestDataUtil.createTestAuthorA());
        underTest.save(authorA);
        // Update created author
        authorA.setName("UPDATED");

        // Resaving updated author
        underTest.save(authorA);
        // Find updated author by Id
        Optional<Author> result = underTest.findById(authorA.getId());

        assertThat(result).isPresent();
        // Assert that the result matches the author with the updated name
        assertThat(result.get()).isEqualTo(authorA);
    }

    @Test
    public void testThatAuthorCanBeDeleted(){
        Author authorA = underTest.save(TestDataUtil.createTestAuthorA());
        underTest.save(authorA);

        // Verify that author exists before deletion (optional)
        Optional<Author> beforeDelete = underTest.findById(authorA.getId());
        assertThat(beforeDelete).isPresent();
        assertThat(beforeDelete.get()).isEqualTo(authorA);

        System.out.println(authorA.toString());

        underTest.deleteById(authorA.getId());

        Optional<Author> result = underTest.findById(authorA.getId());
        assertThat(result).isEmpty();
    }
}
