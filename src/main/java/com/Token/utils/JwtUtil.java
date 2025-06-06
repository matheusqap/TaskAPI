package com.Token.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key secretKey = Keys.hmacShaKeyFor("8z!Kk#d3FzPq&Lm7Wd9Rg@Xe2VbTq!Hs5MxLp#ZnVc8Bh@Yq6Cr%GtLmNs7Zk!Xy".getBytes());
    // A chave deve ter pelo menos 256 bits para HS512, por isso repeti a senha

    private final long EXPIRATION = 1000 * 60 * 60; // 1 hora

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return null; // token inválido ou expirado
        }
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        if (claims == null) return null;
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        if (claims == null) return true;
        return claims.getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername != null && tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
