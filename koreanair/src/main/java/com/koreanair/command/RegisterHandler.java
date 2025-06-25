package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.model.service.AuthService;

public class RegisterHandler implements CommandHandler {
    private AuthService authService;
    
    public RegisterHandler() {
        this.authService = new AuthService();
    }
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        switch (action) {
            case "/register.do":
                return handleRegister(request, response);
            case "/registerForm.do":
                return showRegisterForm(request, response);
            default:
                return "/views/login/register.jsp";
        }
    }
    
    // 회원가입 폼 표시
    private String showRegisterForm(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        return "/views/login/register.jsp";
    }
    
    // 회원가입 처리
    private String handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String koreanName = request.getParameter("koreanName");
        String englishName = request.getParameter("englishName");
        String birthDateStr = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        try {
            boolean success = authService.registerUser(userId, password, confirmPassword, 
                                                     koreanName, englishName, birthDateStr, 
                                                     gender, email, phone, address);
            
            if (success) {
                request.setAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
                return "/views/login/login.jsp";
            } else {
                request.setAttribute("error", "회원가입 중 오류가 발생했습니다.");
                return "/views/login/register.jsp";
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            return "/views/login/register.jsp";
        } catch (Exception e) {
            request.setAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "/views/login/register.jsp";
        }
    }
} 