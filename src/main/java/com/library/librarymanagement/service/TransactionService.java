package com.library.librarymanagement.service;

import com.library.librarymanagement.dto.TransactionDTO;
import com.library.librarymanagement.entity.*;
import com.library.librarymanagement.exception.*;
import com.library.librarymanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BookRepository        bookRepository;
    private final MemberRepository      memberRepository;

    // ─── ISSUE BOOK ───────────────────────────────────────
    @Transactional
    public TransactionDTO issueBook(Long bookId, Long memberId) {

        // Step 1: Check book exists
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with ID: " + bookId));

        // Step 2: Check member exists
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with ID: " + memberId));

        // Step 3: Business Rule — Member must be ACTIVE
        if (!member.getStatus().equals("ACTIVE")) {
            throw new LibraryException(
                "Member is INACTIVE and cannot borrow books!");
        }

        // Step 4: Business Rule — Book must have available copies
        if (book.getAvailableCopies() <= 0) {
            throw new LibraryException(
                "No copies available for book: " + book.getTitle());
        }

        // Step 5: Business Rule — Member shouldn't already have this book
        transactionRepository
            .findByBook_BookIdAndMember_MemberIdAndStatus(
                bookId, memberId, "ISSUED")
            .ifPresent(t -> {
                throw new LibraryException(
                    "Member already has this book issued!");
            });

        // Step 6: Decrease available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Step 7: Create transaction record
        Transaction transaction = Transaction.builder()
                .book(book)
                .member(member)
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status("ISSUED")
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return mapToDTO(saved);
    }

    // ─── RETURN BOOK ──────────────────────────────────────
    @Transactional
    public TransactionDTO returnBook(Long transactionId) {

        // Step 1: Find the transaction
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with ID: " + transactionId));

        // Step 2: Business Rule — Book must be currently ISSUED
        if (transaction.getStatus().equals("RETURNED")) {
            throw new LibraryException(
                "This book has already been returned!");
        }

        // Step 3: Update transaction
        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus("RETURNED");
        transactionRepository.save(transaction);

        // Step 4: Increase available copies back
        Book book = transaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return mapToDTO(transaction);
    }

    // ─── GET ALL TRANSACTIONS ─────────────────────────────
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── GET TRANSACTION BY ID ────────────────────────────
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with ID: " + id));
        return mapToDTO(transaction);
    }

    // ─── GET MEMBER HISTORY ───────────────────────────────
    public List<TransactionDTO> getMemberHistory(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException(
                "Member not found with ID: " + memberId);
        }
        return transactionRepository.findMemberHistory(memberId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── GET OVERDUE BOOKS ────────────────────────────────
    public List<TransactionDTO> getOverdueTransactions() {
        return transactionRepository.findOverdueTransactions()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── MAPPER ───────────────────────────────────────────
    private TransactionDTO mapToDTO(Transaction t) {
        return TransactionDTO.builder()
                .transactionId(t.getTransactionId())
                .bookId(t.getBook().getBookId())
                .bookTitle(t.getBook().getTitle())
                .memberId(t.getMember().getMemberId())
                .memberName(t.getMember().getName())
                .issueDate(t.getIssueDate())
                .dueDate(t.getDueDate())
                .returnDate(t.getReturnDate())
                .status(t.getStatus())
                .build();
    }
}
