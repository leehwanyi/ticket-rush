package com.ticketrush.commoncore.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(String email, String role){
        Date now = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(email)
                        .claim(AUTHORIZATION_KEY,role)
                        .expiration(new Date(now.getTime() + accessTokenExpiration))
                        .issuedAt(now)
                        .signWith(key)
                        .compact();
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }catch (Exception e){
            log.error("Invalidate JWT token: {}", e.getMessage());
        }

        return false;
    }

    public Claims getUserInfoFromToken(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

}
