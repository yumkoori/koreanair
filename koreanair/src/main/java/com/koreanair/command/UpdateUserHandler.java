package com.koreanair.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.User;
import com.koreanair.model.service.UserService;

public class UpdateUserHandler implements CommandHandler {
    private UserService userService;
    
    public UpdateUserHandler() {
        this.userService = new UserService();
    }
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            return "/views/login/login.jsp";
        }
        
        String method = request.getMethod();
        
        if ("POST".equals(method)) {
            return processUpdate(request, response, currentUser, session);
        } else {
            // GET 요청은 대시보드로 리다이렉트
            return "redirect:/dashboard.do";
        }
    }
    
    private String processUpdate(HttpServletRequest request, HttpServletResponse response, 
                               User currentUser, HttpSession session) throws ServletException, IOException {
        
        String updateType = request.getParameter("updateType");
        
        try {
            if ("profile".equals(updateType)) {
                return updateProfile(request, currentUser, session);
            } else if ("password".equals(updateType)) {
                return updatePassword(request, currentUser);
            } else {
                request.setAttribute("error", "잘못된 요청입니다.");
                return "/views/login/dashboard.jsp";
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "처리 중 오류가 발생했습니다.");
            return "/views/login/dashboard.jsp";
        }
    }
    
    private String updateProfile(HttpServletRequest request, User currentUser, HttpSession session) 
            throws ServletException, IOException {
        
        String koreanName = request.getParameter("koreanName");
        String englishName = request.getParameter("englishName");
        String birthDateStr = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        try {
            boolean success = userService.updateProfile(currentUser, koreanName, englishName, 
                                                       birthDateStr, gender, email, phone, address, session);
            
            if (success) {
                request.setAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
            } else {
                request.setAttribute("error", "회원 정보 수정에 실패했습니다.");
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("error", "회원 정보 수정 중 오류가 발생했습니다.");
        }
        
        return "/views/login/dashboard.jsp";
    }
    
    private String updatePassword(HttpServletRequest request, User currentUser) 
            throws ServletException, IOException {
        
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        try {
            boolean success = userService.updatePassword(currentUser, currentPassword, 
                                                        newPassword, confirmPassword);
            
            if (success) {
                request.setAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
            } else {
                request.setAttribute("error", "비밀번호 변경에 실패했습니다.");
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("error", "비밀번호 변경 중 오류가 발생했습니다.");
        }
        
        return "/views/login/dashboard.jsp";
    }
} 