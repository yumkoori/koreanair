package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.service.KakaoService;

public class KakaoLoginHandler implements CommandHandler {
    private KakaoService kakaoService;
    
    public KakaoLoginHandler() {
        this.kakaoService = new KakaoService();
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
        String kakaoLoginUrl = kakaoService.getKakaoLoginUrl(request);
        System.out.println("카카오 로그인 URL: " + kakaoLoginUrl);
        response.sendRedirect(kakaoLoginUrl);
        return null;
    }
    
    // 카카오 콜백 처리
    private String handleKakaoCallback(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String code = request.getParameter("code");
        String error = request.getParameter("error");
        HttpSession session = request.getSession();
        
        try {
            String result = kakaoService.processKakaoCallback(code, error, request, response, session);
            
            if (result.startsWith("redirect:")) {
                String redirectUrl = result.substring("redirect:".length());
                response.sendRedirect(request.getContextPath() + redirectUrl);
                return null;
            }
            
            return result;
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            return "/views/login/login.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "카카오 로그인 처리 중 오류가 발생했습니다.");
            return "/views/login/login.jsp";
        }
    }
} 