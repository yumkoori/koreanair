package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.User;
import com.koreanair.util.KakaoApiService;

public class LogoutHandler implements CommandHandler {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        switch (action) {
            case "/logout.do":
                return handleLogout(request, response);
            default:
                return "redirect:/";
        }
    }
    
    // 로그아웃 처리
    private String handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        HttpSession session = request.getSession(false);
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
            return null;
        }
        
        return "redirect:/";
    }
} 