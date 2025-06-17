package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.User;

public class LoginHandler implements CommandHandler {
    private UserDAO userDAO;
    
    public LoginHandler() {
        this.userDAO = new UserDAO();
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
            default:
                return "/views/login/login.jsp";
        }
    }
    
    // 로그인 폼 표시
    private String showLoginForm(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        return "/views/login/login.jsp";
    }
    
    // 로그인 처리
    private String handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        
        if (userId == null || password == null || userId.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "아이디와 비밀번호를 입력해주세요.");
            return "/views/login/login.jsp";
        }
        
        User user = userDAO.loginUser(userId, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            request.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "/views/login/login.jsp";
        }
    }
    
    // 아이디 중복 체크 (AJAX 요청용)
    private String checkUserId(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String userId = request.getParameter("userId");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"exists\": true}");
            return null; // AJAX 응답이므로 뷰 없음
        }
        
        boolean exists = userDAO.isUserIdExists(userId);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"exists\": " + exists + "}");
        
        return null; // AJAX 응답이므로 뷰 없음
    }
} 