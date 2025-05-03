package com.devtiro.springdata_jpa.repositories;

import com.devtiro.springdata_jpa.TestDataUtil;
import com.devtiro.springdata_jpa.domain.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AuthorRepositoryIntegrationTests {
    private AuthorRepository underTest;

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
}
