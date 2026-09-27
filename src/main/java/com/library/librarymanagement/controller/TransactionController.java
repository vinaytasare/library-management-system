package com.library.librarymanagement.controller;

import com.library.librarymanagement.dto.TransactionDTO;
import com.library.librarymanagement.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // ─── ISSUE BOOK ───────────────────────────────────────
    // POST http://localhost:8080/api/transactions/issue/1/1
    @PostMapping("/issue/{bookId}/{memberId}")
    public ResponseEntity<TransactionDTO> issueBook(
            @PathVariable Long bookId,
            @PathVariable Long memberId) {

        TransactionDTO dto = transactionService.issueBook(bookId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // ─── RETURN BOOK ──────────────────────────────────────
    // PUT http://localhost:8080/api/transactions/return/1
    @PutMapping("/return/{transactionId}")
    public ResponseEntity<TransactionDTO> returnBook(
            @PathVariable Long transactionId) {

        return ResponseEntity.ok(
            transactionService.returnBook(transactionId));
    }

    // ─── GET ALL TRANSACTIONS ─────────────────────────────
    // GET http://localhost:8080/api/transactions
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(
            transactionService.getAllTransactions());
    }

    // ─── GET TRANSACTION BY ID ────────────────────────────
    // GET http://localhost:8080/api/transactions/1
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            transactionService.getTransactionById(id));
    }

    // ─── GET MEMBER HISTORY ───────────────────────────────
    // GET http://localhost:8080/api/transactions/member/1/history
    @GetMapping("/member/{memberId}/history")
    public ResponseEntity<List<TransactionDTO>> getMemberHistory(
            @PathVariable Long memberId) {

        return ResponseEntity.ok(
            transactionService.getMemberHistory(memberId));
    }

    // ─── GET OVERDUE BOOKS ────────────────────────────────
    // GET http://localhost:8080/api/transactions/overdue
    @GetMapping("/overdue")
    public ResponseEntity<List<TransactionDTO>> getOverdueTransactions() {
        return ResponseEntity.ok(
            transactionService.getOverdueTransactions());
    }
}