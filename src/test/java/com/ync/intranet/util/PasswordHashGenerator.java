package com.ync.intranet.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 비밀번호 해시 생성기
 * "1234"의 실제 BCrypt 해시를 생성
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "1234";
        String hash = encoder.encode(password);

        System.out.println("========================================");
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
        System.out.println("========================================");

        // 검증
        boolean matches = encoder.matches(password, hash);
        System.out.println("Verification: " + matches);

        // 기존 해시 검증
        String oldHash = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
        boolean oldMatches = encoder.matches(password, oldHash);
        System.out.println("Old hash verification: " + oldMatches);
    }
}
