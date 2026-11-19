package com.example.demo.security;

import com.example.demo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(User user) {
        return generateToken(user, "access", accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, "refresh", refreshTokenExpiration);
    }

    private String generateToken(
            User user,
            String tokenType,
            long expiration
    ) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getSystemRole().name())
                .claim("type", tokenType)
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(expiresAt)
                .signWith(signingKey)
                .compact();
    }

    public boolean isAccessTokenValid(String token, User user) {
        try {
            Claims claims = extractAllClaims(token);

            return claims.getSubject().equals(user.getUsername()) && "access".equals(claims.get("type", String.class)) && claims.getExpiration().after(new Date());
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token, User user) {
        try {
            Claims claims = extractAllClaims(token);

            return claims.getSubject().equals(user.getUsername())
                    && "refresh".equals(claims.get("type", String.class))
                    && claims.getExpiration().after(new Date());
        } catch (Exception exception) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
