package com.db.bankingapi.configurations;


import com.db.bankingapi.exceptions.JwtTokenMalformedException;
import com.db.bankingapi.exceptions.JwtTokenMissingException;
import com.db.bankingapi.models.Role;
import com.db.bankingapi.models.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTManager {

    @Value("${jwt.secretkey}")
    private String secretKey;
    @Value("${jwt.token.validity}")
    private long expiryTime;
    public User getUser(final String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            User user = new User();
            user.setUserName(body.getSubject());
            List<Role> roles = Arrays.asList(body.get("roles").toString().split(",")).stream().map(r -> new Role(r))
                    .collect(Collectors.toList());
            user.setRoles(roles);
            return user;
        } catch (Exception e) {
            System.out.println(e.getMessage() + " => " + e);
        }
        return null;
    }
    public String generateToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getUserName());
        claims.put("roles", user.getRoles());
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + expiryTime;
        Date exp = new Date(expMillis);
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }
    public void validateToken(final String token) {
        try {
            System.out.println(token);

            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (SignatureException ex) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
    }

}
