package com.devtiro.springdata_jpa.repositories;

import com.devtiro.springdata_jpa.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, String > {
}
