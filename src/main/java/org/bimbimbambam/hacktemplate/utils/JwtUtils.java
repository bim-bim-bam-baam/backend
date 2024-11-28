package org.bimbimbambam.hacktemplate.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtUtils {
    private String secretKey;
    private Long expirationTime;

    public Jwt generateToken(String username, Long id, String roles) {
        return new Jwt(Jwts.builder()
                .setSubject(username)
                .claim("id", id)
                .claim("roles", roles)
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

    public String extractRoles(Jwt token) {
        return (String) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.token())
                .getBody()
                .get("roles");
    }

    public Long extractId(Jwt token) {
        return (Long) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.token())
                .getBody()
                .get("id");
    }

    public boolean hasAdminRole(Jwt token) {
        String roles = extractRoles(token);
        if (roles != null) {
            String[] roleArray = roles.split(",");
            for (String role : roleArray) {
                if (role.trim().equalsIgnoreCase("admin")) {
                    return true;
                }
            }
        }
        return false;
    }
}
