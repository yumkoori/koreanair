package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.User;
import com.koreanair.model.service.AuthService;

public class LoginHandler implements CommandHandler {
    private AuthService authService;
    
    public LoginHandler() {
        this.authService = new AuthService();
    }
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        switch (action) {
            case "/login.do":
                return handleLogin(request, response);
            case "/loginForm.do":
                return showLoginForm(request, response);
            case "/checkUserId.do":
                return checkUserId(request, response);
            case "/checkEmail.do":
                return checkEmail(request, response);
            default:
                return "/views/login/login.jsp";
        }
    }
    
    // 로그인 폼 표시
    private String showLoginForm(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        // URL 파라미터로 전달된 targetUrl 처리
        String targetUrlParam = request.getParameter("targetUrl");
        HttpSession session = request.getSession();
        if (targetUrlParam != null && !targetUrlParam.isEmpty()) {
            session.setAttribute("targetUrl", targetUrlParam);
        }
        
        // 세션에서 메시지들 확인
        if (session != null) {
            // 회원가입 성공 메시지 확인
            String signupSuccessMessage = (String) session.getAttribute("signupSuccessMessage");
            if (signupSuccessMessage != null) {
                request.setAttribute("message", signupSuccessMessage);
                session.removeAttribute("signupSuccessMessage");
            }
            
            // 카카오 연동 성공 메시지 확인
            String linkSuccessMessage = (String) session.getAttribute("linkSuccessMessage");
            if (linkSuccessMessage != null) {
                request.setAttribute("message", linkSuccessMessage);
                session.removeAttribute("linkSuccessMessage");
            }
        }
        
        // 저장된 아이디 쿠키 확인
        String savedUserId = authService.getSavedUserId(request);
        if (savedUserId != null) {
                        request.setAttribute("savedUserId", savedUserId);
                        request.setAttribute("rememberChecked", true);
        }
        
        return "/views/login/login.jsp";
    }
    
    // 로그인 처리
    private String handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        try {
            User user = authService.authenticateUser(userId, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
                
                // 세션 설정 및 예약 목록 조회
                authService.setupUserSession(session, user);
                
                // 아이디 저장 쿠키 처리
                authService.handleRememberMe(response, userId, remember);
                
                // 목적지 URL 확인 및 리다이렉션
            String targetUrl = (String) session.getAttribute("targetUrl");
                session.removeAttribute("targetUrl");

            if (targetUrl != null && !targetUrl.isEmpty()) {
                return "redirect:" + targetUrl;
            } else {
                return "redirect:/";
            }
        } else {
            request.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
                return "/views/login/login.jsp";
            }
        } catch (Exception e) {
            request.setAttribute("error", "아이디와 비밀번호를 입력해주세요.");
            return "/views/login/login.jsp";
        }
    }
    
    // 아이디 중복 체크 (AJAX 요청용)
    private String checkUserId(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String userId = request.getParameter("userId");
        
        boolean exists = authService.isUserIdExists(userId);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"exists\": " + exists + "}");
        
        return null; // AJAX 응답이므로 뷰 없음
    }
    
    // 이메일 중복 체크 (AJAX 요청용)
    private String checkEmail(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String email = request.getParameter("email");
        
        AuthService.EmailCheckResult result = authService.checkEmailForRegistration(email);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"exists\": " + result.isExists() + 
                                  ", \"isKakaoLinkable\": " + result.isKakaoLinkable() + "}");
        
        return null; // AJAX 응답이므로 뷰 없음
    }
}