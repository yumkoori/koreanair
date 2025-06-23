<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koreanair.model.dto.User" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AirLogin - 로그인</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body class="airline-main-body">
    <jsp:include page="/views/common/header.jsp" />
    <main class="main-content">
    <div class="container">
        <!-- 좌측 섹션 -->
        <div class="left-section">
            <div class="left-content">
                <h1>아직,<br>대한항공 회원이<br>아니세요?</h1>
                <p>회원으로 가입하시고<br>마일리지 혜택을 누려 보세요.</p>
                <button class="signup-btn" onclick="location.href='registerForm.do'">회원가입</button>
            </div>
        </div>
        
        <!-- 우측 섹션 -->
        <div class="right-section">
            <div class="login-tabs">
                <button class="tab-btn active">아이디</button>
                <button class="tab-btn">스카이패스 번호</button>
            </div>
            
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
                <!-- 로그인 폼 -->
                <form action="login.do" method="post" class="login-form" onsubmit="return validateLoginForm()">
                    <div class="form-group">
                        <label for="userId">아이디<span style="color: red;">*</span></label>
                        <input type="text" id="userId" name="userId" required 
                               placeholder="" autocomplete="username"
                               value="<%= request.getAttribute("savedUserId") != null ? request.getAttribute("savedUserId") : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label for="password">비밀번호<span style="color: red;">*</span></label>
                        <input type="password" id="password" name="password" required 
                               placeholder="" autocomplete="current-password">
                    </div>
                    
                    <div class="remember-me">
                        <input type="checkbox" id="remember" name="remember" 
                               <%= Boolean.TRUE.equals(request.getAttribute("rememberChecked")) ? "checked" : "" %>>
                        <label for="remember">아이디 저장</label>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">로그인</button>
                    
                    <div class="links">
                        <a href="#">아이디 찾기</a>
                        <a href="#">비밀번호 찾기</a>
                    </div>
                    
                    <div class="social-login">
                        <p>다른 계정으로 로그인</p>
                        <div class="social-buttons">
                            <button type="button" class="social-btn kakao" onclick="location.href='kakao/login.do'" title="카카오">
                                <i class="fas fa-comment"></i>
                                카카오로 로그인
                            </button>
                        </div>
                    </div>
                </form>
            <%
                }
            %>
            
            <div style="text-align: center; margin-top: 2rem;">
                <a href="./" style="color: #666; font-size: 14px; text-decoration: none;">
                    <i class="fas fa-arrow-left"></i> 로그인에 어려움이 있나요?
                </a>
            </div>
        </div>
    </div>
    </main>
    
    <jsp:include page="/views/common/footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/js/index.js"></script>
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html> 