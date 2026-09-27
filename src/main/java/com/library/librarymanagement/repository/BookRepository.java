package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find book by ISBN
    Optional<Book> findByIsbn(String isbn);

    // Find all books by category
    List<Book> findByCategory(String category);

    // Find all books by author
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Find all available books (availableCopies > 0)
    List<Book> findByAvailableCopiesGreaterThan(int copies);

    // Search books by title (case-insensitive, partial match)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Custom JPQL query - find books by category AND availability
    @Query("SELECT b FROM Book b WHERE b.category = :category " +
           "AND b.availableCopies > 0")
    List<Book> findAvailableBooksByCategory(String category);
}