package com.prueba.tecnica.backend.venko.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    public JwtAuthenticationFilter(JwtService jwtService, 
                                  UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        System.out.println("=== JWT FILTER START ===");
        System.out.println("🔵 URI: " + request.getRequestURI());
        System.out.println("🔵 Method: " + request.getMethod());
        System.out.println("🔵 Auth Header: " + request.getHeader("Authorization"));
        
        // 1. SALTA endpoints de autenticación (register, login, logout)
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/auth/")) {
            System.out.println("🟢 Skipping auth endpoint: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        
        // 2. VERIFICA si hay header Authorization
        final String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("🟡 No Bearer token found");
            filterChain.doFilter(request, response);
            return;
        }
        
        // 3. EXTRAE el token JWT
        final String jwt = authHeader.substring(7);
        System.out.println("🔵 JWT Token: " + (jwt.length() > 20 ? jwt.substring(0, 20) + "..." : jwt));
        
        // 4. EXTRAE el email del token
        final String userEmail = jwtService.extractUsername(jwt);
        System.out.println("🔵 User email from token: " + userEmail);
        
        // 5. VALIDA el token
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("🔵 Validating token for user: " + userEmail);
            
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                System.out.println("✅ User found in DB: " + userDetails.getUsername());
                
                if (jwtService.validateToken(jwt, userDetails)) {
                    System.out.println("✅ Token is valid");
                    
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    
                    authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("✅ User authenticated: " + userEmail);
                } else {
                    System.out.println("❌ Token validation failed");
                }
            } catch (Exception e) {
                System.out.println("❌ Error loading user: " + e.getMessage());
                // No lances la excepción, deja que la petición continúe
            }
        } else {
            System.out.println("🟡 User email is null or user already authenticated");
        }
        
        System.out.println("=== JWT FILTER END ===");
        filterChain.doFilter(request, response);
    }
}