package org.bimbimbambam.hacktemplate.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.bimbimbambam.hacktemplate.exception.ForbiddenException;
import org.bimbimbambam.hacktemplate.exception.UnauthorizedException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        return Long.valueOf((Integer) Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.token())
                .getBody()
                .get("id"));
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

    public void forceAdminRole(Jwt token) {
        if (!hasAdminRole(token)) {
            throw new ForbiddenException("No admin rights");
        }
    }

    public Jwt getJwtToken() {
        String authorizationHeader = getAuthorizationHeader();
        if (!checkAuthorizationHeader(authorizationHeader)) {
            throw new ForbiddenException("Missing or invalid Authorization header");
        }
        Jwt token = new Jwt(authorizationHeader.substring(7));

        if (!validateToken(token)) {
            throw new ForbiddenException("Invalid or expired token");
        }
        return token;
    }

    private String getAuthorizationHeader() {
        return RequestContextHolder.getRequestAttributes()
                instanceof ServletRequestAttributes attributes
                ? attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION)
                : null;
    }

    private boolean checkAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }
}
