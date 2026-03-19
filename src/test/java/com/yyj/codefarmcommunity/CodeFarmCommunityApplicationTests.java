package com.yyj.codefarmcommunity;

import com.yyj.codefarmcommunity.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class CodeFarmCommunityApplicationTests {

    private final PasswordEncoder passwordEncoder;

    public CodeFarmCommunityApplicationTests(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void generateEncryptedPassword() {
        String rawPassword = "123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
    }

    @Test
    void name() {
        System.out.println("JWT token: " +
                JwtUtil.parseToken("eyJhbGciOiJIUzI1NiJ9.eyJwZXJtaXNzaW9ucyI6WyJzeXN0ZW06dXNlcjpsaXN0Iiwic3lzdGVtOnVzZXI6bWFuYWdlIiwic3lzdGVtOm1hbmFnZSJdLCJyb2xlcyI6WyJVU0VSIl0sInVzZXJOYW1lIjoic3RyaW5nIiwidXNlcklkIjo0LCJpYXQiOjE3NzM4MTg1NTksImV4cCI6MTc3MzkwNDk1OX0.FzT4MC_BJuCuIqxGnZKqoZK2qkhMOHKpMdG2Oh8h180"));
    }
}
