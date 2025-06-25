package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.service.KakaoService;

public class KakaoSignupHandler implements CommandHandler {
    private KakaoService kakaoService;
    
    public KakaoSignupHandler() {
        this.kakaoService = new KakaoService();
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
        
        try {
            boolean success = kakaoService.completeKakaoSignup(session, koreanName, englishName, 
                                                             birthDateStr, gender, phone, address);
            
            if (success) {
                System.out.println("회원가입 성공 - 로그인 페이지로 리다이렉트");
                
                // PRG 패턴 적용: 리다이렉트로 GET 요청 유도
                response.sendRedirect(request.getContextPath() + "/loginForm.do");
                return null;
            } else {
                System.out.println("데이터베이스 저장 실패 - 회원가입 페이지로 돌아감");
                request.setAttribute("error", "회원가입 중 오류가 발생했습니다.");
                return "/views/login/kakao_signup.jsp";
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("입력값 검증 실패: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            return "/views/login/kakao_signup.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "회원가입 처리 중 오류가 발생했습니다.");
            return "/views/login/kakao_signup.jsp";
        }
    }
} 