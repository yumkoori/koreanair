package com.koreanair.model.service;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.User;
import com.koreanair.util.KakaoApiService;
import com.koreanair.util.KakaoApiService.KakaoUserInfo;
import com.koreanair.util.KakaoConfig;

public class KakaoService {
    private UserDAO userDAO;
    
    public KakaoService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * 카카오 로그인 URL 생성
     */
    public String getKakaoLoginUrl(HttpServletRequest request) {
        return KakaoConfig.getKakaoLoginUrl(request);
    }
    
    /**
     * 카카오 콜백 처리
     */
    public String processKakaoCallback(String code, String error, HttpServletRequest request, 
                                     HttpServletResponse response, HttpSession session) throws Exception {
        
        if (error != null) {
            throw new IllegalArgumentException("카카오 로그인이 취소되었습니다.");
        }
        
        if (code == null) {
            throw new IllegalArgumentException("카카오 로그인 중 오류가 발생했습니다.");
        }
        
        // 1. 인가 코드로 액세스 토큰 요청
        String redirectUri = KakaoConfig.getRedirectUri(request);
        String accessToken = KakaoApiService.getAccessToken(code, redirectUri);
        
        // 2. 액세스 토큰으로 사용자 정보 요청
        KakaoUserInfo kakaoUserInfo = KakaoApiService.getUserInfo(accessToken);
        
        // 3. 카카오 ID로 기존 사용자 조회
        User existingUser = userDAO.getUserByKakaoId(kakaoUserInfo.getId());
        
        if (existingUser != null) {
            // 기존 카카오 사용자 - 로그인 처리
            session.setAttribute("user", existingUser);
            session.setAttribute("kakaoAccessToken", accessToken); // 카카오 액세스 토큰 저장
            return "redirect:/";
        }
        
        // 4. 이메일로 기존 일반 계정 조회 (연동 처리)
        if (kakaoUserInfo.getEmail() != null) {
            User emailUser = userDAO.getUserByEmail(kakaoUserInfo.getEmail());
            if (emailUser != null) {
                // 기존 일반 계정에 카카오 연동
                boolean linkSuccess = userDAO.linkKakaoToExistingUser(
                    kakaoUserInfo.getEmail(), 
                    kakaoUserInfo.getId(), 
                    kakaoUserInfo.getProfileImageUrl()
                );
                
                if (linkSuccess) {
                    // 연동 성공 메시지를 세션에 저장
                    session.setAttribute("linkSuccessMessage", "기존 계정과 카카오 계정이 연동되었습니다. 로그인해주세요.");
                    return "redirect:/loginForm.do";
                }
            }
        }
        
        // 5. 신규 사용자 - 회원가입 처리 준비
        session.setAttribute("kakaoUserInfo", kakaoUserInfo);
        session.setAttribute("tempKakaoAccessToken", accessToken);
        
        return "/views/login/kakao_signup.jsp";
    }
    
    /**
     * 카카오 회원가입 완료 처리
     */
    public boolean completeKakaoSignup(HttpSession session, String koreanName, String englishName, 
                                     String birthDateStr, String gender, String phone, String address) throws Exception {
        
        KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) session.getAttribute("kakaoUserInfo");
        
        if (kakaoUserInfo == null) {
            throw new IllegalArgumentException("세션이 만료되었습니다. 다시 로그인해주세요.");
        }
        
        // 입력값 검증
        if (koreanName == null || englishName == null || birthDateStr == null || 
            gender == null || phone == null ||
            koreanName.trim().isEmpty() || englishName.trim().isEmpty() || 
            birthDateStr.trim().isEmpty() || gender.trim().isEmpty() || 
            phone.trim().isEmpty()) {
            throw new IllegalArgumentException("모든 필수 항목을 입력해주세요.");
        }
        
        // 생년월일 변환
        Date birthDate = Date.valueOf(birthDateStr);
        
        // 고유한 사용자 ID 생성 (카카오 ID 기반)
        String userId = "kakao_" + kakaoUserInfo.getId();
        
        // User 객체 생성
        User user = new User();
        user.setUserId(userId);
        user.setKoreanName(koreanName);
        user.setEnglishName(englishName);
        user.setBirthDate(birthDate);
        user.setGender(gender);
        user.setEmail(kakaoUserInfo.getEmail());
        user.setPhone(phone);
        user.setAddress(address != null ? address : "");
        user.setKakaoId(kakaoUserInfo.getId());
        user.setLoginType("kakao");
        user.setProfileImage(kakaoUserInfo.getProfileImageUrl());
        
        // 데이터베이스에 저장
        boolean success = userDAO.insertKakaoUser(user);
        
        if (success) {
            // 세션에서 임시 정보 제거
            session.removeAttribute("kakaoUserInfo");
            session.removeAttribute("tempKakaoAccessToken");
            
            // 성공 메시지를 세션에 저장
            session.setAttribute("signupSuccessMessage", "카카오 회원가입이 완료되었습니다. 카카오 로그인을 이용해주세요.");
        }
        
        return success;
    }
} 