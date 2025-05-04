Here's a professionally written `README.md` file for your Spring Boot JPA project:

---

# Spring Boot JPA: Author-Book Management System

- This is a simple Spring Boot application that demonstrates a one-to-many relationship between `Author` and `Book` entities using Spring Data JPA. It provides full CRUD operations via RESTful endpoints, tested using **Postman** and **DBeaver**.
- All CRUD functionalities provided by Spring Data JPA are covered through comprehensive integration tests.

## Features

* REST API for managing authors and books
* One-to-many relationship: One author can have multiple books
* Cascading deletes: Deleting an author removes all related books
* Validation of JSON payloads
* Circular reference prevention using Jackson annotations
* Entity persistence verified with Postman and DBeaver
* Controller functionality verified via unit tests and manual API testing

---

## Technologies Used

* Java 17+
* Spring Boot 3+
* Spring Data JPA
* H2 / PostgreSQL (pluggable DB layer)
* Jackson for JSON serialization
* Lombok for boilerplate reduction
* Postman for API testing
* DBeaver for database verification

---

## Entity Model

### `Author`

```java
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    private Long id;

    private String name;
    private Integer age;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Book> books = new ArrayList<>();
}
```

### `Book`

```java
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
```

---

## Testing

### Manual Testing

All endpoints were manually tested using:

* **Postman**: For sending HTTP requests (POST, PUT, GET, DELETE)
* **DBeaver**: For inspecting the database state after each operation

### Sample Endpoints Tested

#### Create Author

```http
POST /authors
```

```json
{
  "name": "Abigail Rose",
  "age": 80
}
```

#### Create Book

```http
POST /books
```

```json
{
  "isbn": "978-1-2345-6789-0",
  "title": "The Shadow in the Attic",
  "author": { "id": 1 }
}
```

#### Get All Authors

```http
GET /authors
```

#### Delete Author (with cascading book delete)

```http
DELETE /authors/1
```

---

### Integration Testing

Full integration tests were written for both `AuthorRepository` and `BookRepository` using Spring Boot's testing framework (`@SpringBootTest`) and JUnit 5.

#### Test Structure

* `AuthorRepositoryIntegrationTests`: Verifies CRUD operations for authors.
* `BookRepositoryIntegrationTest`: Verifies CRUD operations for books and author-book relationships.
* Each test uses a fresh in-memory database context (`@DirtiesContext`) to ensure isolation and repeatability.

### AuthorRepository Tests

* `testThatTheAuthorCanBeCreatedAndRecalled`: Ensures authors are saved and retrieved correctly.
* `testThatMultipleAuthorsCanBeCreatedAndRecalled`: Confirms multiple authors are persisted.
* `testThatAuthorCanBeUpdated`: Verifies updating author attributes is persisted.
* `testThatAuthorCanBeDeleted`: Ensures an author can be deleted and no longer retrievable.

### BookRepository Tests

* `testThatBookCanBeCreatedAndRecalled`: Verifies a book with an author is saved and retrieved.
* `testThatMultipleBooksCanBeCreatedAndRecalled`: Ensures multiple books across different authors are handled.
* `testThatBookCanBeUpdated`: Checks that changes to a book (e.g., title) are persisted.
* `testThatBookCanBeDeleted`: Confirms books are deleted and removed from the database.

### Test Data

A helper utility class (`TestDataUtil`) was used to generate consistent test data across all tests, ensuring clean, readable, and reusable setup logic.

---

## Running the Application

Before running the application, ensure that Docker Compose is running to provide the required PostgreSQL database:
```bash
docker-compose up -build
```
then run the app:
```bash
./mvnw spring-boot:run
```

The app will start at: `http://localhost:8080`


