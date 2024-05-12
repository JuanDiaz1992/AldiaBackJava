package com.springboot.aldiabackjava.services;

import com.springboot.aldiabackjava.models.userModels.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;


import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TokenService {
    private final String secretKey = "miClaveSecretaSuperSegura1234567890";

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getIdUser());
        claims.put("username", userDetails.getUsername());
        claims.put("typeUser", userDetails.getTypeUser());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)))
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey);

        return jwtBuilder.compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Firma del token JWT inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Formato del token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token JWT no compatible: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Error al validar token JWT: {}", e.getMessage());
        }

        return false;
    }

}
