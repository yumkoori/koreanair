package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.User;

public class HomeHandler implements CommandHandler {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        switch (action) {
            case "/":
            case "/index.do":
                return showMainIndex(request, response);
            case "/dashboard.do":
                return showDashboard(request, response);
            
            // [수정] '/lookupForm.do' 요청을 처리하는 case 추가
            case "/lookupForm.do":
                return "/WEB-INF/views/lookupForm.jsp";
                
            case "/checkupForm.do":
                return "/WEB-INF/views/checkupForm.jsp";
                
            default:
                return showMainIndex(request, response);
        }
    }
    
    // 메인 인덱스 페이지 표시 (새로운 항공사 메인 페이지)
    private String showMainIndex(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        return "/index.jsp";
    }
    
    // 대시보드 표시
    private String showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        // AuthenticationFilter에서 이미 인증을 확인했으므로 바로 대시보드 표시
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/loginForm.do";
        }
        return "/views/login/dashboard.jsp";
    }
}