package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // Find member by email (for uniqueness check)
    Optional<Member> findByEmail(String email);

    // Find all active members
    List<Member> findByStatus(String status);

    // Search member by name (partial, case-insensitive)
    List<Member> findByNameContainingIgnoreCase(String name);

    // Check if email already exists (for validation)
    boolean existsByEmail(String email);
}