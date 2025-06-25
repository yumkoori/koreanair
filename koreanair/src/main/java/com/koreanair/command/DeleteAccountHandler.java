package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.User;
import com.koreanair.model.service.UserService;

public class DeleteAccountHandler implements CommandHandler {
    private UserService userService;
    
    public DeleteAccountHandler() {
        this.userService = new UserService();
    }
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        switch (action) {
            case "/deleteAccount.do":
                return handleDeleteAccount(request, response);
            default:
                return "redirect:/";
        }
    }
    
    // 회원탈퇴 처리
    private String handleDeleteAccount(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            return "redirect:/loginForm.do";
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/loginForm.do";
        }
        
        String loginType = request.getParameter("loginType");
        String confirmPassword = request.getParameter("confirmPassword");
        
        try {
            boolean success = userService.deleteAccount(user, confirmPassword, loginType);
            
            if (success) {
                session.invalidate(); // 세션 무효화
                request.setAttribute("message", "회원탈퇴가 완료되었습니다.");
                return "/views/login/login.jsp";
            } else {
                request.setAttribute("error", "회원탈퇴 중 오류가 발생했습니다.");
                return "/views/login/dashboard.jsp";
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            return "/views/login/dashboard.jsp";
        } catch (Exception e) {
            request.setAttribute("error", "회원탈퇴 중 오류가 발생했습니다.");
            return "/views/login/dashboard.jsp";
        }
    }
} 