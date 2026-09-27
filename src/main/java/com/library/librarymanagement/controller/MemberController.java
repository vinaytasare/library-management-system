package com.library.librarymanagement.controller;

import com.library.librarymanagement.dto.MemberDTO;
import com.library.librarymanagement.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // ─── REGISTER MEMBER ──────────────────────────────────
    // POST http://localhost:8080/api/members
    @PostMapping
    public ResponseEntity<MemberDTO> registerMember(
            @Valid @RequestBody MemberDTO dto) {

        MemberDTO saved = memberService.registerMember(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ─── GET ALL MEMBERS ──────────────────────────────────
    // GET http://localhost:8080/api/members
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // ─── GET MEMBER BY ID ─────────────────────────────────
    // GET http://localhost:8080/api/members/1
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(
            @PathVariable Long id) {

        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    // ─── UPDATE MEMBER ────────────────────────────────────
    // PUT http://localhost:8080/api/members/1
    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberDTO dto) {

        return ResponseEntity.ok(memberService.updateMember(id, dto));
    }

    // ─── UPDATE MEMBER STATUS ─────────────────────────────
    // PATCH http://localhost:8080/api/members/1/status?status=INACTIVE
    @PatchMapping("/{id}/status")
    public ResponseEntity<MemberDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
            memberService.updateMemberStatus(id, status));
    }

    // ─── DELETE MEMBER ────────────────────────────────────
    // DELETE http://localhost:8080/api/members/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(
            @PathVariable Long id) {

        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully!");
    }

    // ─── SEARCH BY NAME ───────────────────────────────────
    // GET http://localhost:8080/api/members/search?name=rahul
    @GetMapping("/search")
    public ResponseEntity<List<MemberDTO>> searchMembers(
            @RequestParam String name) {

        return ResponseEntity.ok(memberService.searchByName(name));
    }

    // ─── GET ACTIVE MEMBERS ───────────────────────────────
    // GET http://localhost:8080/api/members/active
    @GetMapping("/active")
    public ResponseEntity<List<MemberDTO>> getActiveMembers() {
        return ResponseEntity.ok(memberService.getActiveMembers());
    }
}