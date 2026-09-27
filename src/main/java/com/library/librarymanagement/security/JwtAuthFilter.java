package com.library.librarymanagement.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2: Check if it starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // Step 4: Validate token
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 5: Extract email from token
        String email = jwtUtil.extractEmail(token);

        // Step 6: Load user and set authentication
        if (email != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

            authToken.setDetails(
                new WebAuthenticationDetailsSource()
                    .buildDetails(request)
            );

            // Step 7: Tell Spring Security "this user is authenticated"
            SecurityContextHolder.getContext()
                .setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}