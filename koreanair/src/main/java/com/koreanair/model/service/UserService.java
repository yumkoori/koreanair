package com.koreanair.model.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.User;
import com.koreanair.util.PasswordUtil;

public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * 프로필 정보 업데이트
     */
    public boolean updateProfile(User currentUser, String koreanName, String englishName, 
                               String birthDateStr, String gender, String email, 
                               String phone, String address, HttpSession session) throws Exception {
        
        // 카카오 계정인 경우 프로필 수정 제한
        if ("kakao".equals(currentUser.getLoginType())) {
            throw new IllegalArgumentException("카카오 계정은 프로필 정보를 수정할 수 없습니다.");
        }
        
        // 필수 필드 검증
        if (koreanName == null || koreanName.trim().isEmpty() ||
            englishName == null || englishName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("필수 항목을 모두 입력해주세요.");
        }
        
        // 이메일 중복 검사 (현재 사용자의 이메일과 다른 경우에만)
        if (!email.equals(currentUser.getEmail())) {
            User existingUser = userDAO.getUserByEmail(email);
            if (existingUser != null) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
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
                throw new IllegalArgumentException("생년월일 형식이 올바르지 않습니다.");
            }
        }
        
        // 데이터베이스 업데이트
        boolean success = userDAO.updateUser(updateUser);
        
        if (success) {
            // 세션의 사용자 정보 업데이트
            User updatedUser = userDAO.getUserById(currentUser.getUserId());
            session.setAttribute("user", updatedUser);
        }
        
        return success;
    }
    
    /**
     * 비밀번호 변경
     */
    public boolean updatePassword(User currentUser, String currentPassword, 
                                String newPassword, String confirmPassword) throws Exception {
        
        // 카카오 계정인 경우 비밀번호 변경 불가
        if ("kakao".equals(currentUser.getLoginType())) {
            throw new IllegalArgumentException("카카오 계정은 비밀번호를 변경할 수 없습니다.");
        }
        
        // 필수 필드 검증
        if (currentPassword == null || currentPassword.trim().isEmpty() ||
            newPassword == null || newPassword.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("모든 비밀번호 필드를 입력해주세요.");
        }
        
        // 현재 비밀번호 확인
        if (!PasswordUtil.verifyPassword(currentPassword, currentUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        
        // 새 비밀번호 확인
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }
        
        // 비밀번호 길이 검증
        if (newPassword.length() < 4) {
            throw new IllegalArgumentException("비밀번호는 4자 이상이어야 합니다.");
        }
        
        // 비밀번호 해시화
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        
        // 데이터베이스 업데이트
        return userDAO.updatePassword(currentUser.getUserId(), hashedPassword);
    }
    
    /**
     * 계정 삭제
     */
    public boolean deleteAccount(User user, String confirmPassword, String loginType) throws Exception {
        
        // 카카오 사용자가 아닌 경우에만 비밀번호 확인
        if (!"kakao".equals(loginType) && !"kakao".equals(user.getLoginType())) {
            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("비밀번호를 입력해주세요.");
            }
            
            // 현재 사용자 정보를 다시 조회하여 비밀번호 확인
            User currentUser = userDAO.loginUser(user.getUserId(), confirmPassword);
            if (currentUser == null) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }
        
        // 회원탈퇴 처리
        boolean success;
        if ("kakao".equals(user.getLoginType()) || user.getUserId() == null) {
            // 카카오 계정이거나 user_id가 없는 경우 이메일 기준으로 삭제
            success = userDAO.deleteUserByEmail(user.getEmail());
        } else {
            // 일반 계정의 경우 user_id 기준으로 삭제
            success = userDAO.deleteUser(user.getUserId());
        }
        
        return success;
    }
} 