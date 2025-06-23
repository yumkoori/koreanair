package com.koreanair.command;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.User;
import com.koreanair.util.PasswordUtil;

public class UpdateUserHandler implements CommandHandler {
    
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
        UserDAO userDAO = new UserDAO();
        
        try {
            if ("profile".equals(updateType)) {
                return updateProfile(request, currentUser, userDAO, session);
            } else if ("password".equals(updateType)) {
                return updatePassword(request, currentUser, userDAO);
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
    
    private String updateProfile(HttpServletRequest request, User currentUser, 
                               UserDAO userDAO, HttpSession session) throws ServletException, IOException {
        
        // 카카오 계정인 경우 프로필 수정 제한
        if ("kakao".equals(currentUser.getLoginType())) {
            request.setAttribute("error", "카카오 계정은 프로필 정보를 수정할 수 없습니다.");
            return "/views/login/dashboard.jsp";
        }
        
        String koreanName = request.getParameter("koreanName");
        String englishName = request.getParameter("englishName");
        String birthDateStr = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        // 필수 필드 검증
        if (koreanName == null || koreanName.trim().isEmpty() ||
            englishName == null || englishName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty()) {
            request.setAttribute("error", "필수 항목을 모두 입력해주세요.");
            return "/views/login/dashboard.jsp";
        }
        
        // 이메일 중복 검사 (현재 사용자의 이메일과 다른 경우에만)
        if (!email.equals(currentUser.getEmail())) {
            User existingUser = userDAO.getUserByEmail(email);
            if (existingUser != null) {
                request.setAttribute("error", "이미 사용 중인 이메일입니다.");
                return "/views/login/dashboard.jsp";
            }
        }
        
        // User 객체 생성 및 업데이트
        User updateUser = new User();
        updateUser.setUserId(currentUser.getUserId());
        updateUser.setKoreanName(koreanName.trim());
        updateUser.setEnglishName(englishName.trim());
        updateUser.setGender(gender);
        updateUser.setEmail(email.trim());
        updateUser.setPhone(phone.trim());
        updateUser.setAddress(address != null ? address.trim() : "");
        
        // 생년월일 처리
        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(birthDateStr);
                updateUser.setBirthDate(new Date(utilDate.getTime()));
            } catch (ParseException e) {
                request.setAttribute("error", "생년월일 형식이 올바르지 않습니다.");
                return "/views/login/dashboard.jsp";
            }
        }
        
        // 데이터베이스 업데이트
        boolean success = userDAO.updateUser(updateUser);
        
        if (success) {
            // 세션의 사용자 정보 업데이트
            User updatedUser = userDAO.getUserById(currentUser.getUserId());
            session.setAttribute("user", updatedUser);
            
            request.setAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
        } else {
            request.setAttribute("error", "회원 정보 수정에 실패했습니다.");
        }
        
        return "/views/login/dashboard.jsp";
    }
    
    private String updatePassword(HttpServletRequest request, User currentUser, UserDAO userDAO) 
            throws ServletException, IOException {
        
        // 카카오 계정인 경우 비밀번호 변경 불가
        if ("kakao".equals(currentUser.getLoginType())) {
            request.setAttribute("error", "카카오 계정은 비밀번호를 변경할 수 없습니다.");
            return "/views/login/dashboard.jsp";
        }
        
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // 필수 필드 검증
        if (currentPassword == null || currentPassword.trim().isEmpty() ||
            newPassword == null || newPassword.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "모든 비밀번호 필드를 입력해주세요.");
            return "/views/login/dashboard.jsp";
        }
        
        // 현재 비밀번호 확인
        if (!PasswordUtil.verifyPassword(currentPassword, currentUser.getPassword())) {
            request.setAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
            return "/views/login/dashboard.jsp";
        }
        
        // 새 비밀번호 확인
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "새 비밀번호가 일치하지 않습니다.");
            return "/views/login/dashboard.jsp";
        }
        
        // 비밀번호 길이 검증
        if (newPassword.length() < 4) {
            request.setAttribute("error", "비밀번호는 4자 이상이어야 합니다.");
            return "/views/login/dashboard.jsp";
        }
        
        // 비밀번호 해시화
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        
        // 데이터베이스 업데이트
        boolean success = userDAO.updatePassword(currentUser.getUserId(), hashedPassword);
        
        if (success) {
            request.setAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } else {
            request.setAttribute("error", "비밀번호 변경에 실패했습니다.");
        }
        
        return "/views/login/dashboard.jsp";
    }
} 