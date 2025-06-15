package com.koreanair.command;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.User;
import com.koreanair.util.PasswordUtil;

public class RegisterHandler implements CommandHandler {
    private UserDAO userDAO;
    
    public RegisterHandler() {
        this.userDAO = new UserDAO();
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
        
        // 입력값 검증
        if (userId == null || password == null || koreanName == null || englishName == null ||
            birthDateStr == null || gender == null || email == null || phone == null ||
            userId.trim().isEmpty() || password.trim().isEmpty() || koreanName.trim().isEmpty() ||
            englishName.trim().isEmpty() || birthDateStr.trim().isEmpty() || gender.trim().isEmpty() ||
            email.trim().isEmpty() || phone.trim().isEmpty()) {
            request.setAttribute("error", "모든 필수 항목을 입력해주세요.");
            return "/views/login/register.jsp";
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "/views/login/register.jsp";
        }
        
        // 비밀번호 강도 검증
        if (!PasswordUtil.isValidPassword(password)) {
            request.setAttribute("error", "비밀번호는 4자 이상이어야 합니다.");
            return "/views/login/register.jsp";
        }
        
        // 생년월일 형식 검증 및 변환
        Date birthDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(birthDateStr);
            birthDate = new Date(parsedDate.getTime());
        } catch (ParseException e) {
            request.setAttribute("error", "생년월일 형식이 올바르지 않습니다.");
            return "/views/login/register.jsp";
        }
        
        // 아이디 중복 체크
        if (userDAO.isUserIdExists(userId)) {
            request.setAttribute("error", "이미 사용중인 아이디입니다.");
            return "/views/login/register.jsp";
        }
        
        // 이메일 중복 체크 (카카오 계정 포함)
        User existingUser = userDAO.getUserByEmail(email);
        if (existingUser != null) {
            if ("kakao".equals(existingUser.getLoginType())) {
                // 카카오 계정이 있는 경우 일반 계정 정보를 추가하여 연동
                existingUser.setUserId(userId);
                existingUser.setPassword(PasswordUtil.hashPassword(password));
                existingUser.setKoreanName(koreanName);
                existingUser.setEnglishName(englishName);
                existingUser.setBirthDate(birthDate);
                existingUser.setGender(gender);
                existingUser.setPhone(phone);
                existingUser.setAddress(address);
                
                boolean success = userDAO.linkNormalToKakaoUser(existingUser);
                if (success) {
                    request.setAttribute("message", "기존 카카오 계정과 연동되어 회원가입이 완료되었습니다. 로그인해주세요.");
                    return "/views/login/login.jsp";
                } else {
                    request.setAttribute("error", "계정 연동 중 오류가 발생했습니다.");
                    return "/views/login/register.jsp";
                }
            } else if ("both".equals(existingUser.getLoginType())) {
                request.setAttribute("error", "해당 이메일로 계정이 이미 존재합니다.");
                return "/views/login/register.jsp";
            } else {
                request.setAttribute("error", "해당 이메일로 계정이 이미 존재합니다.");
                return "/views/login/register.jsp";
            }
        }
        
        // 사용자 등록
        User user = new User();
        user.setUserId(userId);
        // 비밀번호를 BCrypt로 암호화
        user.setPassword(PasswordUtil.hashPassword(password));
        user.setKoreanName(koreanName);
        user.setEnglishName(englishName);
        user.setBirthDate(birthDate);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        
        boolean success = userDAO.insertUser(user);
        
        if (success) {
            request.setAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "/views/login/login.jsp";
        } else {
            request.setAttribute("error", "회원가입 중 오류가 발생했습니다.");
            return "/views/login/register.jsp";
        }
    }
} 