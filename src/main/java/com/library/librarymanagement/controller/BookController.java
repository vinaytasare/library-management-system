package com.library.librarymanagement.controller;

import com.library.librarymanagement.dto.BookDTO;
import com.library.librarymanagement.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // ─── ADD BOOK ─────────────────────────────────────────
    // POST http://localhost:8080/api/books
    @PostMapping
    public ResponseEntity<BookDTO> addBook(
            @Valid @RequestBody BookDTO dto) {

        BookDTO saved = bookService.addBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ─── GET ALL BOOKS ────────────────────────────────────
    // GET http://localhost:8080/api/books
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // ─── GET BOOK BY ID ───────────────────────────────────
    // GET http://localhost:8080/api/books/1
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(
            @PathVariable Long id) {

        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // ─── UPDATE BOOK ──────────────────────────────────────
    // PUT http://localhost:8080/api/books/1
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO dto) {

        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    // ─── DELETE BOOK ──────────────────────────────────────
    // DELETE http://localhost:8080/api/books/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully!");
    }

    // ─── SEARCH BY TITLE ──────────────────────────────────
    // GET http://localhost:8080/api/books/search?title=clean
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(
            @RequestParam String title) {

        return ResponseEntity.ok(bookService.searchByTitle(title));
    }

    // ─── GET AVAILABLE BOOKS ──────────────────────────────
    // GET http://localhost:8080/api/books/available
    @GetMapping("/available")
    public ResponseEntity<List<BookDTO>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }
}