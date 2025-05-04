package com.devtiro.springdata_jpa.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book {
    @Id
    private String isbn;
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private Author author;
}
