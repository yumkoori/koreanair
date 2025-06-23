package com.koreanair.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 비밀번호 암호화 및 검증을 위한 유틸리티 클래스
 * BCrypt 해시 알고리즘을 사용합니다.
 */
public class PasswordUtil {
    
    // BCrypt의 기본 라운드 수 (보안성과 성능의 균형)
    private static final int ROUNDS = 12;
    
    /**
     * 평문 비밀번호를 BCrypt로 해시화합니다.
     * 
     * @param plainPassword 평문 비밀번호
     * @return 해시화된 비밀번호
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 null이거나 빈 문자열일 수 없습니다.");
        }
        
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(ROUNDS));
    }
    
    /**
     * 평문 비밀번호와 해시화된 비밀번호를 비교하여 일치 여부를 확인합니다.
     * 
     * @param plainPassword 평문 비밀번호
     * @param hashedPassword 해시화된 비밀번호
     * @return 비밀번호 일치 여부
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            // BCrypt 검증 중 오류 발생 시 false 반환
            return false;
        }
    }
} 