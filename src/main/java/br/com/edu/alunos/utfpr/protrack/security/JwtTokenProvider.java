package br.com.edu.alunos.utfpr.protrack.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long validityInMillis = 3600000; // 1 hora

    public JwtTokenProvider(@Value("${jwt.secret}") final String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(final String subject, final Map<String, Object> claims) {
        final Date now = new Date();
        final Date expiry = new Date(now.getTime() + validityInMillis);

        return Jwts.builder().subject(subject).claims(claims).issuedAt(now).expiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }

    public String resolveToken(final HttpServletRequest request) {
        final String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parse(token);
            return true;
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(final String token) {
        final Jwt<?, ?> jwt = Jwts.parser().verifyWith(secretKey).build().parse(token);
        return ((Claims) jwt.getPayload()).getSubject();
    }
}
