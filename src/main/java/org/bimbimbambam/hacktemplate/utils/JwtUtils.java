package org.bimbimbambam.hacktemplate.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public Jwt generateToken(String username) {
        return new Jwt(Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact());
    }

    public boolean validateToken(Jwt token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.token());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(Jwt token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.token())
                .getBody()
                .getSubject();
    }
}
