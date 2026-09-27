package com.library.librarymanagement.service;

import com.library.librarymanagement.dto.MemberDTO;
import com.library.librarymanagement.entity.Member;
import com.library.librarymanagement.exception.*;
import com.library.librarymanagement.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // ─── CREATE ───────────────────────────────────────────
    public MemberDTO registerMember(MemberDTO dto) {

        // Business Rule: Email must be unique
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new LibraryException(
                "Member with email " + dto.getEmail() + " already exists!");
        }

        Member member = mapToEntity(dto);
        Member saved = memberRepository.save(member);
        return mapToDTO(saved);
    }

    // ─── READ ALL ──────────────────────────────────────────
    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── READ ONE ──────────────────────────────────────────
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with ID: " + id));
        return mapToDTO(member);
    }

    // ─── UPDATE ───────────────────────────────────────────
    public MemberDTO updateMember(Long id, MemberDTO dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with ID: " + id));

        member.setName(dto.getName());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());

        Member updated = memberRepository.save(member);
        return mapToDTO(updated);
    }

    // ─── ACTIVATE / DEACTIVATE ────────────────────────────
    public MemberDTO updateMemberStatus(Long id, String status) {

        // Business Rule: Only valid statuses allowed
        if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
            throw new LibraryException(
                "Invalid status! Use 'ACTIVE' or 'INACTIVE'");
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with ID: " + id));

        member.setStatus(status);
        Member updated = memberRepository.save(member);
        return mapToDTO(updated);
    }

    // ─── DELETE ───────────────────────────────────────────
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "Member not found with ID: " + id);
        }
        memberRepository.deleteById(id);
    }

    // ─── SEARCH ───────────────────────────────────────────
    public List<MemberDTO> searchByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<MemberDTO> getActiveMembers() {
        return memberRepository.findByStatus("ACTIVE")
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── MAPPER METHODS ───────────────────────────────────
    public MemberDTO mapToDTO(Member member) {
        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .membershipDate(member.getMembershipDate())
                .status(member.getStatus())
                .build();
    }

    private Member mapToEntity(MemberDTO dto) {
        return Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }
}