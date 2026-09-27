package com.library.librarymanagement.service;

import com.library.librarymanagement.dto.BookDTO;
import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.exception.*;
import com.library.librarymanagement.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor        // Lombok: auto-generates constructor for final fields
public class BookService {

    private final BookRepository bookRepository;

    // ─── CREATE ───────────────────────────────────────────
    public BookDTO addBook(BookDTO dto) {

        // Business Rule: ISBN must be unique
        if (bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new LibraryException(
                "Book with ISBN " + dto.getIsbn() + " already exists!");
        }

        Book book = mapToEntity(dto);
        book.setAvailableCopies(dto.getTotalCopies()); // Initially all copies available
        Book saved = bookRepository.save(book);
        return mapToDTO(saved);
    }

    // ─── READ ALL ──────────────────────────────────────────
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── READ ONE ──────────────────────────────────────────
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with ID: " + id));
        return mapToDTO(book);
    }

    // ─── UPDATE ───────────────────────────────────────────
    public BookDTO updateBook(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with ID: " + id));

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setCategory(dto.getCategory());
        book.setTotalCopies(dto.getTotalCopies());

        Book updated = bookRepository.save(book);
        return mapToDTO(updated);
    }

    // ─── DELETE ───────────────────────────────────────────
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
    }

    // ─── SEARCH ───────────────────────────────────────────
    public List<BookDTO> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── MAPPER METHODS ───────────────────────────────────
    private BookDTO mapToDTO(Book book) {
        return BookDTO.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .build();
    }

    private Book mapToEntity(BookDTO dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .category(dto.getCategory())
                .totalCopies(dto.getTotalCopies())
                .build();
    }
}