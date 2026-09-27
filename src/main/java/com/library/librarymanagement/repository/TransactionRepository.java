package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find all transactions for a specific member
    List<Transaction> findByMember_MemberId(Long memberId);

    // Find all transactions for a specific book
    List<Transaction> findByBook_BookId(Long bookId);

    // Find all currently issued (not returned) transactions
    List<Transaction> findByStatus(String status);

    // Find active transaction for a specific book+member combo
    Optional<Transaction> findByBook_BookIdAndMember_MemberIdAndStatus(
            Long bookId, Long memberId, String status);

    // Find all overdue books (due date passed, not returned)
    @Query("SELECT t FROM Transaction t WHERE t.status = 'ISSUED' " +
           "AND t.dueDate < CURRENT_DATE")
    List<Transaction> findOverdueTransactions();

    // Find transaction history for a member
    @Query("SELECT t FROM Transaction t WHERE t.member.memberId = :memberId " +
           "ORDER BY t.issueDate DESC")
    List<Transaction> findMemberHistory(@Param("memberId") Long memberId);
}