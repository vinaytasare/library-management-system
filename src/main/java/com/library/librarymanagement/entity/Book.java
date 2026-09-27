package com.library.librarymanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data                   // Lombok: generates getters, setters, toString
@NoArgsConstructor      // Lombok: generates empty constructor
@AllArgsConstructor     // Lombok: generates constructor with all fields
@Builder                // Lombok: enables Builder pattern
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(name = "author", nullable = false)
    private String author;

    @NotBlank(message = "ISBN is required")
    @Column(name = "isbn", unique = true, nullable = false)
    private String isbn;

    @Column(name = "category")
    private String category;

    @Min(value = 1, message = "Total copies must be at least 1")
    @Column(name = "total_copies", nullable = false)
    private Integer totalCopies;

    @Column(name = "available_copies", nullable = false)
    private Integer availableCopies;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}