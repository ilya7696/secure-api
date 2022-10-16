package by.tovpenets.secure.api.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private static final byte ACCESS_TOKEN_EXPIRATION_MINUTES = 10;
    private static final byte REFRESH_TOKEN_EXPIRATION_DAYS = 5;

    public String generatedAccessToken(String login) {
        return generateToken(login, TimeUnit.MINUTES.toMillis(ACCESS_TOKEN_EXPIRATION_MINUTES));
    }

    public String generateRefreshToken(String login) {
        return generateToken(login, TimeUnit.DAYS.toMillis(REFRESH_TOKEN_EXPIRATION_DAYS));
    }

    private String generateToken(String login, long expirationTime) {
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validate(String token) {
        try {
            parseToken(token);
            return true;
        } catch (RuntimeException e) {
            log.warn("Invalid token '{}'", token);
            return false;
        }
    }

    public String getLoginFromToken(String token) {
        return parseToken(token).getBody().getSubject();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
