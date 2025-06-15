package com.koreanair.command;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.User;
import com.koreanair.util.KakaoApiService.KakaoUserInfo;

public class KakaoSignupHandler implements CommandHandler {
    private UserDAO userDAO;
    
    public KakaoSignupHandler() {
        this.userDAO = new UserDAO();
    }
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        System.out.println("KakaoSignupHandler - URI: " + uri + ", Action: " + action);
        
        switch (action) {
            case "/kakao/signup.do":
                System.out.println("카카오 회원가입 처리 시작");
                return completeKakaoSignup(request, response);
            default:
                System.out.println("카카오 회원가입 페이지로 이동");
                return "/views/login/kakao_signup.jsp";
        }
    }
    
    // 카카오 회원가입 완료 처리
    private String completeKakaoSignup(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        System.out.println("카카오 회원가입 완료 처리 메소드 진입");
        
        HttpSession session = request.getSession();
        KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) session.getAttribute("kakaoUserInfo");
        String tempAccessToken = (String) session.getAttribute("tempKakaoAccessToken");
        
        System.out.println("세션에서 카카오 사용자 정보: " + (kakaoUserInfo != null ? "존재" : "없음"));
        
        if (kakaoUserInfo == null) {
            System.out.println("카카오 사용자 정보 없음 - 로그인 페이지로 이동");
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
        
        System.out.println("받은 파라미터들:");
        System.out.println("koreanName: " + koreanName);
        System.out.println("englishName: " + englishName);
        System.out.println("birthDate: " + birthDateStr);
        System.out.println("gender: " + gender);
        System.out.println("phone: " + phone);
        System.out.println("address: " + address);
        
        // 입력값 검증
        if (koreanName == null || englishName == null || birthDateStr == null || 
            gender == null || phone == null ||
            koreanName.trim().isEmpty() || englishName.trim().isEmpty() || 
            birthDateStr.trim().isEmpty() || gender.trim().isEmpty() || 
            phone.trim().isEmpty()) {
            System.out.println("필수 항목 누락 - 회원가입 페이지로 돌아감");
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
            System.out.println("데이터베이스에 사용자 정보 저장 시도");
            boolean success = userDAO.insertKakaoUser(user);
            System.out.println("데이터베이스 저장 결과: " + (success ? "성공" : "실패"));
            
            if (success) {
                System.out.println("회원가입 성공 - 로그인 페이지로 리다이렉트");
                // 세션에서 임시 정보 제거
                session.removeAttribute("kakaoUserInfo");
                session.removeAttribute("tempKakaoAccessToken");
                
                // 성공 메시지를 세션에 저장 (리다이렉트 후에도 유지되도록)
                session.setAttribute("signupSuccessMessage", "카카오 회원가입이 완료되었습니다. 카카오 로그인을 이용해주세요.");
                
                // PRG 패턴 적용: 리다이렉트로 GET 요청 유도
                response.sendRedirect(request.getContextPath() + "/loginForm.do");
                return null;
            } else {
                System.out.println("데이터베이스 저장 실패 - 회원가입 페이지로 돌아감");
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