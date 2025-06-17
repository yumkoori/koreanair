package com.koreanair.command;

import java.sql.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.User;
import com.koreanair.util.KakaoApiService;
import com.koreanair.util.KakaoApiService.KakaoUserInfo;
import com.koreanair.util.KakaoConfig;

public class KakaoLoginHandler implements CommandHandler {
    private UserDAO userDAO;
    
    public KakaoLoginHandler() {
        this.userDAO = new UserDAO();
    }
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        System.out.println("KakaoLoginHandler - URI: " + uri + ", Action: " + action);
        
        switch (action) {
            case "/kakao/login.do":
                System.out.println("카카오 로그인 시작");
                return handleKakaoLogin(request, response);
            case "/kakao/callback.do":
                System.out.println("카카오 콜백 처리");
                return handleKakaoCallback(request, response);
            default:
                System.out.println("기본 로그인 페이지로 이동");
                return "/views/login/login.jsp";
        }
    }
    
    // 카카오 로그인 시작 (카카오 인증 페이지로 리다이렉트)
    private String handleKakaoLogin(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String kakaoLoginUrl = KakaoConfig.getKakaoLoginUrl(request);
        System.out.println("카카오 로그인 URL: " + kakaoLoginUrl);
        response.sendRedirect(kakaoLoginUrl);
        return null;
    }
    
    // 카카오 콜백 처리
    private String handleKakaoCallback(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String code = request.getParameter("code");
        String error = request.getParameter("error");
        
        if (error != null) {
            request.setAttribute("error", "카카오 로그인이 취소되었습니다.");
            return "/views/login/login.jsp";
        }
        
        if (code == null) {
            request.setAttribute("error", "카카오 로그인 중 오류가 발생했습니다.");
            return "/views/login/login.jsp";
        }
        
        try {
            // 1. 인가 코드로 액세스 토큰 요청
            String redirectUri = KakaoConfig.getRedirectUri(request);
            String accessToken = KakaoApiService.getAccessToken(code, redirectUri);
            
            // 2. 액세스 토큰으로 사용자 정보 요청
            KakaoUserInfo kakaoUserInfo = KakaoApiService.getUserInfo(accessToken);
            
            // 3. 카카오 ID로 기존 사용자 조회
            User existingUser = userDAO.getUserByKakaoId(kakaoUserInfo.getId());
            
            if (existingUser != null) {
                // 기존 카카오 사용자 - 로그인 처리
                HttpSession session = request.getSession();
                session.setAttribute("user", existingUser);
                session.setAttribute("kakaoAccessToken", accessToken); // 카카오 액세스 토큰 저장
                response.sendRedirect(request.getContextPath() + "/");
                return null;
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
                        HttpSession session = request.getSession();
                        session.setAttribute("linkSuccessMessage", "기존 계정과 카카오 계정이 연동되었습니다. 로그인해주세요.");
                        
                        // PRG 패턴 적용: 로그인 페이지로 리다이렉트
                        response.sendRedirect(request.getContextPath() + "/loginForm.do");
                        return null;
                    }
                }
            }
            
            // 5. 신규 사용자 - 회원가입 처리
            return handleKakaoSignup(request, response, kakaoUserInfo, accessToken);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "카카오 로그인 처리 중 오류가 발생했습니다.");
            return "/views/login/login.jsp";
        }
    }
    
    // 카카오 신규 사용자 회원가입 처리
    private String handleKakaoSignup(HttpServletRequest request, HttpServletResponse response, 
                                   KakaoUserInfo kakaoUserInfo, String accessToken) throws Exception {
        
        // 카카오 정보를 세션에 임시 저장
        HttpSession session = request.getSession();
        session.setAttribute("kakaoUserInfo", kakaoUserInfo);
        session.setAttribute("tempKakaoAccessToken", accessToken); // 임시로 액세스 토큰 저장
        
        // 추가 정보 입력 페이지로 이동
        return "/views/login/kakao_signup.jsp";
    }
    
    // 카카오 회원가입 완료 처리
    public String completeKakaoSignup(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        HttpSession session = request.getSession();
        KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) session.getAttribute("kakaoUserInfo");
        
        if (kakaoUserInfo == null) {
            request.setAttribute("error", "세션이 만료되었습니다. 다시 로그인해주세요.");
            return "/views/login/login.jsp";
        }
        
        // 추가 정보 받기
        String koreanName = request.getParameter("koreanName");
        String englishName = request.getParameter("englishName");
        String birthDateStr = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        // 입력값 검증
        if (koreanName == null || englishName == null || birthDateStr == null || 
            gender == null || phone == null ||
            koreanName.trim().isEmpty() || englishName.trim().isEmpty() || 
            birthDateStr.trim().isEmpty() || gender.trim().isEmpty() || 
            phone.trim().isEmpty()) {
            request.setAttribute("error", "모든 필수 항목을 입력해주세요.");
            return "/views/login/kakao_signup.jsp";
        }
        
        try {
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
                
                // 로그인 처리
                User newUser = userDAO.getUserByKakaoId(kakaoUserInfo.getId());
                session.setAttribute("user", newUser);
                // 새로 가입한 카카오 사용자는 액세스 토큰이 없으므로 저장하지 않음
                
                response.sendRedirect(request.getContextPath() + "/");
                return null;
            } else {
                request.setAttribute("error", "회원가입 중 오류가 발생했습니다.");
                return "/views/login/kakao_signup.jsp";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "회원가입 처리 중 오류가 발생했습니다.");
            return "/views/login/kakao_signup.jsp";
        }
    }
} 