package com.koreanair.model.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.dto.User;
import com.koreanair.util.KakaoApiService;
import com.koreanair.util.KakaoConfig;
import com.koreanair.util.PasswordUtil;

public class AuthService {
    private UserDAO userDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * 사용자 로그인 처리
     */
    public User authenticateUser(String userId, String password) throws Exception {
        if (userId == null || password == null || userId.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        return userDAO.loginUser(userId, password);
    }
    
    /**
     * 로그인 성공 후 세션 설정 및 예약 목록 조회
     */
    public void setupUserSession(HttpSession session, User user) throws Exception {
        session.setAttribute("user", user);
        
        // 로그인 성공 시 예약 목록을 조회하여 세션에 저장
        List<ReservationDTO> userBookings = userDAO.getUserReservations(user.getUserId());
        session.setAttribute("userBookings", userBookings);
    }
    
    /**
     * 아이디 저장 쿠키 처리
     */
    public void handleRememberMe(HttpServletResponse response, String userId, String remember) {
        if ("on".equals(remember)) {
            // 아이디 저장 체크박스가 선택된 경우 - 30일간 쿠키 저장
            Cookie userIdCookie = new Cookie("savedUserId", userId.trim());
            userIdCookie.setMaxAge(30 * 24 * 60 * 60); // 30일
            userIdCookie.setPath("/");
            response.addCookie(userIdCookie);
        } else {
            // 체크박스가 선택되지 않은 경우 - 기존 쿠키 삭제
            Cookie userIdCookie = new Cookie("savedUserId", "");
            userIdCookie.setMaxAge(0);
            userIdCookie.setPath("/");
            response.addCookie(userIdCookie);
        }
    }
    
    /**
     * 저장된 아이디 쿠키 확인
     */
    public String getSavedUserId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("savedUserId".equals(cookie.getName())) {
                    String savedUserId = cookie.getValue();
                    if (savedUserId != null && !savedUserId.trim().isEmpty()) {
                        return savedUserId;
                    }
                    break;
                }
            }
        }
        return null;
    }
    
    /**
     * 아이디 중복 체크
     */
    public boolean isUserIdExists(String userId) throws Exception {
        if (userId == null || userId.trim().isEmpty()) {
            return true;
        }
        return userDAO.isUserIdExists(userId);
    }
    
    /**
     * 이메일 중복 체크
     */
    public boolean isEmailExists(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            return true;
        }
        return userDAO.isEmailExists(email);
    }
    
    /**
     * 회원가입 처리
     */
    public boolean registerUser(String userId, String password, String confirmPassword, 
                              String koreanName, String englishName, String birthDateStr, 
                              String gender, String email, String phone, String address) throws Exception {
        
        // 입력값 검증
        if (userId == null || password == null || koreanName == null || englishName == null ||
            birthDateStr == null || gender == null || email == null || phone == null ||
            userId.trim().isEmpty() || password.trim().isEmpty() || koreanName.trim().isEmpty() ||
            englishName.trim().isEmpty() || birthDateStr.trim().isEmpty() || gender.trim().isEmpty() ||
            email.trim().isEmpty() || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("모든 필수 항목을 입력해주세요.");
        }
        
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        // 생년월일 형식 검증 및 변환
        Date birthDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(birthDateStr);
            birthDate = new Date(parsedDate.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("생년월일 형식이 올바르지 않습니다.");
        }
        
        // 아이디 중복 체크
        if (userDAO.isUserIdExists(userId)) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        
        // 이메일 중복 체크 (카카오 계정 포함)
        User existingUser = userDAO.getUserByEmail(email);
        if (existingUser != null) {
            if ("kakao".equals(existingUser.getLoginType())) {
                // 카카오 계정이 있는 경우 일반 계정 정보를 추가하여 연동
                existingUser.setUserId(userId);
                existingUser.setPassword(PasswordUtil.hashPassword(password));
                existingUser.setKoreanName(koreanName);
                existingUser.setEnglishName(englishName);
                existingUser.setBirthDate(birthDate);
                existingUser.setGender(gender);
                existingUser.setPhone(phone);
                existingUser.setAddress(address);
                
                return userDAO.linkNormalToKakaoUser(existingUser);
            } else {
                throw new IllegalArgumentException("해당 이메일로 계정이 이미 존재합니다.");
            }
        }
        
        // 사용자 등록
        User user = new User();
        user.setUserId(userId);
        user.setPassword(PasswordUtil.hashPassword(password));
        user.setKoreanName(koreanName);
        user.setEnglishName(englishName);
        user.setBirthDate(birthDate);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        
        return userDAO.insertUser(user);
    }
    
    /**
     * 로그아웃 처리
     */
    public boolean logoutUser(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws Exception {
        boolean isKakaoUser = false;
        
        if (session != null) {
            // 카카오 사용자인지 확인
            User user = (User) session.getAttribute("user");
            String kakaoAccessToken = (String) session.getAttribute("kakaoAccessToken");
            
            // 카카오 사용자이고 액세스 토큰이 있으면 카카오 로그아웃 처리
            if (user != null && 
                ("kakao".equals(user.getLoginType()) || "both".equals(user.getLoginType()))) {
                isKakaoUser = true;
                
                // 액세스 토큰이 있으면 API 로그아웃도 시도
                if (kakaoAccessToken != null) {
                    try {
                        KakaoApiService.logoutKakao(kakaoAccessToken);
                    } catch (Exception e) {
                        // 카카오 로그아웃 실패해도 로컬 세션은 무효화
                        System.out.println("카카오 로그아웃 실패: " + e.getMessage());
                    }
                }
            }
            
            session.invalidate();
        }
        
        // 카카오 사용자인 경우 카카오 브라우저 로그아웃 페이지로 리다이렉트
        if (isKakaoUser) {
            String kakaoLogoutUrl = com.koreanair.util.KakaoConfig.getKakaoBrowserLogoutUrl(request);
            response.sendRedirect(kakaoLogoutUrl);
            return true; // 리다이렉션 발생
        }
        
        return false; // 일반 로그아웃
    }
} 