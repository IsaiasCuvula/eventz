package com.bersyte.eventz.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtFilter(
            JWTService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter (request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(token);
            Authentication authentication = SecurityContextHolder
                    .getContext().getAuthentication();

            if (userEmail != null && authentication == null) {

                UserDetails userDetails = userDetailsService
                        .loadUserByUsername (userEmail);

                if (jwtService.isTokenValid (token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken (
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities ()
                            );
                    authToken.setDetails (
                            new WebAuthenticationDetailsSource ()
                                    .buildDetails (request)
                    );

                    SecurityContextHolder
                            .getContext ()
                            .setAuthentication (authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
            errorResponse (response, request, e.getLocalizedMessage ());
        }
    }

    private void errorResponse(
            HttpServletResponse response,
            HttpServletRequest request,
            String errorMessage
    ) throws IOException {
        // Prepare response map values
        String path = request.getRequestURI ();
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String timestamp = LocalDateTime.now ().toString ();

        // Build JSON response manually
        String jsonResponse = String.format ("{\"path\":\"%s\",\"error\":\"%s\"," +
                        "\"timestamp\":\"%s\",\"status\":%d,}",
                path, errorMessage, timestamp, status);

        // Write JSON response
        response.setStatus (HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType ("application/json");
        response.getWriter ().write (jsonResponse);
    }
}
