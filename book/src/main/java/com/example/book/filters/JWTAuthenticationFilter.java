package com.example.book.filters;

import com.example.book.service.JwtService;
import com.example.book.service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailServiceImpl apiUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing authentication for '{}'", request.getRequestURL());
        // Authorization: Bearer <jwt-token>
        String header = request.getHeader("Authorization");
        log.info("Authorization header: '{}'", header);

        // Check if the header is null or does not start with Bearer
        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token from the header
        String token = header.replace("Bearer ", "");
        log.info("Token: '{}'", token);

        // Validate the token
        boolean isValid = jwtService.validateToken(token);
        log.info("Token is valid: '{}'", isValid);
        if (!isValid) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // If the token is valid, set the authentication in the SecurityContext
        String username = jwtService.extractUsername(token);
        log.info("Username: '{}'", username);
        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = apiUserService.loadUserByUsername(username);
        log.info("UserDetails: '{}'", userDetails);
        log.info("Authorities: '{}'", userDetails.getAuthorities());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
