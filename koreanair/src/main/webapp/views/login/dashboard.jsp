<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koreanair.model.dto.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // AuthenticationFilter에서 이미 인증을 확인했으므로 바로 사용자 정보 가져오기
    User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AirLogin - 대시보드</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class="container dashboard-container">
        <h1>대시보드</h1>
        
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
        %>
        
        <div class="user-info">
            <h3>회원 정보</h3>
            <p><strong>아이디:</strong> <%= user.getUserId() %></p>
            <p><strong>한글 이름:</strong> <%= user.getKoreanName() %></p>
            <p><strong>영문 이름:</strong> <%= user.getEnglishName() %></p>
            <% if (user.getBirthDate() != null) { %>
                <p><strong>생년월일:</strong> <%= new SimpleDateFormat("yyyy년 MM월 dd일").format(user.getBirthDate()) %></p>
            <% } %>
            <p><strong>성별:</strong> <%= "M".equals(user.getGender()) ? "남성" : "여성" %></p>
            <p><strong>이메일:</strong> <%= user.getEmail() %></p>
            <p><strong>휴대폰:</strong> <%= user.getPhone() %></p>
            <% if (user.getAddress() != null && !user.getAddress().trim().isEmpty()) { %>
                <p><strong>주소:</strong> <%= user.getAddress() %></p>
            <% } %>
            <% if (user.getRegDate() != null) { %>
                <p><strong>가입일:</strong> <%= new SimpleDateFormat("yyyy년 MM월 dd일").format(user.getRegDate()) %></p>
            <% } %>
            
            <!-- 카카오 연동 정보 표시 -->
            <% if (user.getLoginType() != null) { %>
                <p><strong>로그인 타입:</strong> 
                    <% if ("kakao".equals(user.getLoginType())) { %>
                        <span style="color: #FEE500; font-weight: bold;">카카오 계정</span>
                    <% } else if ("both".equals(user.getLoginType())) { %>
                        <span style="color: #007bff; font-weight: bold;">일반 + 카카오 연동</span>
                    <% } else { %>
                        일반 계정
                    <% } %>
                </p>
            <% } %>
            
            <% if (user.getKakaoId() != null) { %>
                <p><strong>카카오 연동:</strong> 연동됨</p>
                <% if (user.getProfileImage() != null) { %>
                    <p><strong>프로필 이미지:</strong></p>
                    <img src="<%= user.getProfileImage() %>" alt="프로필 이미지" 
                         style="width: 80px; height: 80px; border-radius: 50%; margin: 10px 0;">
                <% } %>
            <% } %>
        </div>
        
        <div class="actions">
            <a href="./" class="btn btn-primary">메인 페이지로</a>
            <a href="logout.do" class="btn btn-secondary">로그아웃</a>
        </div>
        
        <div class="delete-section">
            <h3>회원탈퇴</h3>
            <% if ("kakao".equals(user.getLoginType())) { %>
                <p>카카오 계정으로 가입하신 경우 확인 버튼만 클릭하시면 탈퇴됩니다.</p>
                <p><strong>주의:</strong> 탈퇴 후에는 모든 정보가 삭제되며 복구할 수 없습니다.</p>
                
                <form action="deleteAccount.do" method="post" onsubmit="return confirmKakaoDeleteAccount()">
                    <input type="hidden" name="loginType" value="kakao">
                    <button type="submit" class="btn btn-danger">회원탈퇴</button>
                </form>
            <% } else { %>
            <p>회원탈퇴를 원하시면 비밀번호를 입력하고 탈퇴 버튼을 클릭해주세요.</p>
            <p><strong>주의:</strong> 탈퇴 후에는 모든 정보가 삭제되며 복구할 수 없습니다.</p>
            
            <form action="deleteAccount.do" method="post" onsubmit="return confirmDeleteAccount()">
                <div class="form-group">
                    <label for="deletePassword">비밀번호 확인</label>
                    <input type="password" id="deletePassword" name="confirmPassword" required 
                           placeholder="현재 비밀번호를 입력하세요">
                </div>
                <button type="submit" class="btn btn-danger">회원탈퇴</button>
            </form>
            <% } %>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html> 