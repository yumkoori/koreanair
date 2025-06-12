package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.User;

public class DeleteAccountHandler implements CommandHandler {
    private UserDAO userDAO;
    
    public DeleteAccountHandler() {
        this.userDAO = new UserDAO();
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
        
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "비밀번호를 입력해주세요.");
            return "/views/login/dashboard.jsp";
        }
        
        // 현재 사용자 정보를 다시 조회하여 비밀번호 확인
        User currentUser = userDAO.loginUser(user.getUserId(), confirmPassword);
        if (currentUser == null) {
            request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "/views/login/dashboard.jsp";
        }
        
        // 회원탈퇴 처리
        boolean success = userDAO.deleteUser(user.getUserId());
        
        if (success) {
            session.invalidate(); // 세션 무효화
            request.setAttribute("message", "회원탈퇴가 완료되었습니다.");
            return "/views/login/login.jsp";
        } else {
            request.setAttribute("error", "회원탈퇴 중 오류가 발생했습니다.");
            return "/views/login/dashboard.jsp";
        }
    }
} 