<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koreanair.model.dto.User" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AirLogin - 홈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class="container">
        <h1>AirLogin</h1>
        <p style="margin-bottom: 2rem; color: #666;">안전하고 간편한 회원 관리 시스템</p>
        
        <%
            // 메시지 표시
            String message = (String) request.getAttribute("message");
            String error = (String) request.getAttribute("error");
            
            if (message != null) {
        %>
            <div class="success"><%= message %></div>
        <%
            }
            if (error != null) {
        %>
            <div class="error"><%= error %></div>
        <%
            }
            
            // 세션에서 사용자 정보 확인
            User user = (User) session.getAttribute("user");
            if (user != null) {
        %>
            <!-- 로그인된 사용자를 위한 화면 -->
            <div class="user-info">
                <h3>환영합니다, <%= user.getKoreanName() %>님!</h3>
                <p>로그인 상태입니다.</p>
            </div>
            
            <div class="actions">
                <a href="dashboard.do" class="btn btn-primary">대시보드로 이동</a>
                <a href="logout.do" class="btn btn-secondary">로그아웃</a>
            </div>
        <%
            } else {
        %>
            <!-- 로그인하지 않은 사용자를 위한 로그인 폼 -->
            <form action="login.do" method="post" onsubmit="return validateLoginForm()">
                <div class="form-group">
                    <label for="userId">아이디</label>
                    <input type="text" id="userId" name="userId" required 
                           placeholder="아이디를 입력하세요" autocomplete="username">
                </div>
                
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" id="password" name="password" required 
                           placeholder="비밀번호를 입력하세요" autocomplete="current-password">
                </div>
                
                <button type="submit" class="btn btn-primary">로그인</button>
            </form>
            
            <div class="social-login" style="margin-top: 1.5rem;">
                <div style="text-align: center; margin-bottom: 1rem; color: #666;">
                    <span style="background: white; padding: 0 10px;">또는</span>
                    <hr style="margin-top: -12px; border: none; border-top: 1px solid #ddd;">
                </div>
                <a href="kakao/login.do" class="btn btn-kakao" style="width: 100%; background-color: #FEE500; color: #000; border: none; display: flex; align-items: center; justify-content: center; gap: 8px;">
                    <img src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png" 
                         alt="카카오 로그인" style="width: 20px; height: 20px;">
                    카카오로 로그인
                </a>
            </div>
            
            <div class="actions" style="margin-top: 1rem;">
                <a href="registerForm.do" class="btn btn-secondary">회원가입</a>
            </div>
        <%
            }
        %>
        
        <div class="links">
            <a href="./">메인 페이지로 돌아가기</a>
            <p style="color: #999; font-size: 12px; margin-top: 2rem;">
                © 2024 AirLogin. 모든 권리 보유.
            </p>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html> 