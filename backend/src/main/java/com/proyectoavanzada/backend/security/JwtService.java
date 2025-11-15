package com.proyectoavanzada.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret:OWY1YzJmYjgtNTQ1Yi00ZjE2LTk4NzktN2M3YzI1YzQ4YzU2OWY1YzJmYjgtNTQ1Yi00ZjE2LTk4NzktN2M3YzI1YzQ4YzU2}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}") // 24 horas por defecto
    private long jwtExpirationMs;

    public String generateToken(String subject, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String subject) {
        return generateToken(subject, Map.of());
    }

    public boolean isTokenValid(String token, String subject) {
        final String username = extractUsername(token);
        return username.equals(subject) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        try {
            // Intentar decodificar como BASE64
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            // Si falla, usar el secret directamente como string (para secretos que no son BASE64)
            // Necesitamos al menos 256 bits (32 bytes) para HS256
            String secretKey = secret;
            if (secretKey.length() < 32) {
                // Repetir el secret hasta tener al menos 32 caracteres
                while (secretKey.length() < 32) {
                    secretKey += secret;
                }
            }
            // Tomar los primeros 32 bytes
            byte[] keyBytes = secretKey.substring(0, Math.min(32, secretKey.length())).getBytes();
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }
}


