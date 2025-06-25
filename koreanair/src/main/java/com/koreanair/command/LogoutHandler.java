package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.service.AuthService;

public class LogoutHandler implements CommandHandler {
    private AuthService authService;
    
    public LogoutHandler() {
        this.authService = new AuthService();
    }
    
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
        
        boolean isKakaoRedirect = authService.logoutUser(session, response, request);
        
        if (isKakaoRedirect) {
            return null; // 카카오 로그아웃 리다이렉션이 이미 처리됨
        }
        
        return "redirect:/";
    }
} 