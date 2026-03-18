package com.yyj.codefarmcommunity.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 */
@Component
public class JwtUtil {

    /**
     * 密钥
     */
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("code-farm-community-secret-key-2026".getBytes());

    /**
     * 过期时间（24小时）
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    
    /**
     * 生成 token
     * @param userId 用户ID
     * @param username 用户名
     * @param claims 额外的声明
     * @return token
     */
    public static String generateToken(Long userId, String username, Map<String, Object> claims) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRE_TIME);
        
        return Jwts.builder()
                .claims(claims)
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expireDate)
                .claim("username", username)
                .signWith(SECRET_KEY)
                .compact();
    }
    
    /**
     * 解析 token
     * @param token token
     * @return Claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * 验证 token 是否过期
     * @param token token
     * @return 是否过期
     */
    public static boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }
    
    /**
     * 验证 token 是否有效
     * @param token token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}