package br.com.edu.alunos.utfpr.protrack.security.filters;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.edu.alunos.utfpr.protrack.application.services.auth.PlatformUserDetailsService;
import br.com.edu.alunos.utfpr.protrack.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final PlatformUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(final JwtTokenProvider tokenProvider,
            final PlatformUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        final String token = tokenProvider.resolveToken(request);

        if (token != null && tokenProvider.validateToken(token)) {
            final String username = tokenProvider.getUsername(token);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            final var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
