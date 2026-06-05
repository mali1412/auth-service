package com.auth.config;

import java.util.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    // MISMA key que invoice — crítico para que los tokens sean compatibles
    private static final String SECRET_KEY = "8J+YjvCfpJPwn5ic8J+YmvCfmI3wn6Ww8J+ZgvCfpKM=";
    private static final SecretKey secretKey = new SecretKeySpec(
        Base64.getDecoder().decode(SECRET_KEY), "HmacSHA256"
    );

    public String generateToken(Integer userId, String username, String role) {
        // Estructura exacta que espera JwtAuthFilter de invoice
        List<Map<String, String>> roles = new ArrayList<>();
        Map<String, String> roleMap = new HashMap<>();
        roleMap.put("authority", role);
        roles.add(roleMap);

        return Jwts.builder()
            .setSubject(username)
            .claim("id", userId)
            .claim("roles", roles)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }
}