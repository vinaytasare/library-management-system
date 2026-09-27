package com.library.librarymanagement.controller;

import com.library.librarymanagement.dto.*;
import com.library.librarymanagement.entity.User;
import com.library.librarymanagement.exception.LibraryException;
import com.library.librarymanagement.repository.UserRepository;
import com.library.librarymanagement.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ─── REGISTER ─────────────────────────────────────────
    // POST http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody AuthRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new LibraryException(
                    "Email already registered: " + request.getEmail());
        }

        // Save user with encrypted password
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);

        // Generate token immediately after registration
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AuthResponse.builder()
                        .token(token)
                        .email(user.getEmail())
                        .role(user.getRole())
                        .message("Registration successful!")
                        .build());
    }

    // ─── LOGIN ────────────────────────────────────────────
    // POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request) {

        // Find user by email
        // ✅ Fixed — includes name
        User user = User.builder()
                .name(request.getName()) // ← ADD THIS LINE
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        // Verify password
        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {
            throw new LibraryException("Invalid email or password!");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(token)
                        .email(user.getEmail())
                        .role(user.getRole())
                        .message("Login successful!")
                        .build());
    }
}