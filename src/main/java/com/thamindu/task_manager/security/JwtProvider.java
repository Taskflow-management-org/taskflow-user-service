package com.thamindu.task_manager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtProvider {

    private final SecretKey key;
    private final long jwtExpirationMs;

    public JwtProvider(@Value("${jwt.secret-key}") String secretKey,
                       @Value("${jwt.secret.expiration-ms}") long jwtExpirationMs
    )
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(Authentication authentication){
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        assert userPrincipal != null;
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Instant now = Instant.now();
        Instant expirationInstant = now.plusMillis(jwtExpirationMs);
        Date issuedAt = Date.from(now);
        Date expiration = Date.from(expirationInstant);

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("roles", roles)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key, Jwts.SIG.HS256)
                .compact();

    }

    public String extractUsername(String token){
        return parseClaims(token).getSubject();
    }

    public boolean validationToken(String token){
        try {
            parseClaims(token);
            return true;
        }catch (SignatureException e){
            log.error("Invalid JWT signature: {}", e.getMessage());
        }catch (ExpiredJwtException | UnsupportedJwtException e){
            log.error("JWT token is expired: {}", e.getMessage());
        }catch (MalformedJwtException e){
            log.error("Invalid JWT token format: {}", e.getMessage());
        }catch (IllegalArgumentException e){
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }


    private Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}


























