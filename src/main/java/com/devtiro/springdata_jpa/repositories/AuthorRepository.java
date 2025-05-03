package com.devtiro.springdata_jpa.repositories;

import com.devtiro.springdata_jpa.domain.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
