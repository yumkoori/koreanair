package com.koreanair.util;

import java.security.SecureRandom;
import java.util.Base64;
import javax.servlet.http.HttpSession;

public class TokenUtil {
    
    private static final String PAYMENT_TOKEN_KEY = "PAYMENT_TOKEN";
    private static final String CSRF_TOKEN_KEY = "CSRF_TOKEN";
    private static final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * 이중결제 방지 토큰 생성
     */
    public static String generatePaymentToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    
    /**
     * CSRF 토큰 생성
     */
    public static String generateCSRFToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    
    /**
     * 세션에 이중결제 방지 토큰 저장
     */
    public static void storePaymentToken(HttpSession session, String token) {
        session.setAttribute(PAYMENT_TOKEN_KEY, token);
    }
    
    /**
     * 세션에 CSRF 토큰 저장
     */
    public static void storeCSRFToken(HttpSession session, String token) {
        session.setAttribute(CSRF_TOKEN_KEY, token);
    }
    
    /**
     * 이중결제 방지 토큰 검증 및 소비 (한 번만 사용 가능)
     */
    public static boolean validateAndConsumePaymentToken(HttpSession session, String token) {
        if (session == null || token == null) {
            return false;
        }
        
        String storedToken = (String) session.getAttribute(PAYMENT_TOKEN_KEY);
        if (storedToken != null && storedToken.equals(token)) {
            // 토큰 소비 (한 번 사용 후 삭제)
            session.removeAttribute(PAYMENT_TOKEN_KEY);
            return true;
        }
        return false;
    }
    
    /**
     * CSRF 토큰 검증 (소비하지 않음, 세션 동안 유지)
     */
    public static boolean validateCSRFToken(HttpSession session, String token) {
        if (session == null || token == null) {
            return false;
        }
        
        String storedToken = (String) session.getAttribute(CSRF_TOKEN_KEY);
        return storedToken != null && storedToken.equals(token);
    }
    
    /**
     * 세션에서 CSRF 토큰 가져오기
     */
    public static String getCSRFToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(CSRF_TOKEN_KEY);
    }
    
    /**
     * 세션에서 이중결제 방지 토큰 가져오기
     */
    public static String getPaymentToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(PAYMENT_TOKEN_KEY);
    }
} 