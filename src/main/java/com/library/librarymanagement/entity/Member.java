package com.library.librarymanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "membership_date")
    private LocalDate membershipDate;

    @Column(name = "status")
    private String status;

    @PrePersist
    public void prePersist() {
        if (this.status == null)        status = "ACTIVE";
        if (this.membershipDate == null) membershipDate = LocalDate.now();
    }
}