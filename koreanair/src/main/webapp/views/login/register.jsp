<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AirLogin - 회원가입</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap" rel="stylesheet">
    <!-- 다음 우편번호 서비스 -->
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body class="airline-main-body">
    <jsp:include page="/views/common/header.jsp" />
    <main class="main-content">
        <div class="container register-container">
        <h1>회원가입</h1>
        
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
        
        <form action="register.do" method="post" onsubmit="return validateRegisterForm()">
            <div class="form-group">
                <label for="userId">아이디 *</label>
                <input type="text" id="userId" name="userId" required 
                       placeholder="3자 이상의 아이디를 입력하세요" autocomplete="username">
                <div id="userIdCheckResult" style="margin-top: 5px; font-size: 14px;"></div>
            </div>
            
            <div class="form-group">
                <label for="password">비밀번호 *</label>
                <input type="password" id="password" name="password" required 
                       placeholder="4자 이상의 비밀번호를 입력하세요" autocomplete="new-password">
            </div>
            
            <div class="form-group">
                <label for="confirmPassword">비밀번호 확인 *</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required 
                       placeholder="비밀번호를 다시 입력하세요" autocomplete="new-password">
                <div id="passwordCheckResult" style="margin-top: 5px; font-size: 14px;"></div>
            </div>
            
            <div class="form-group">
                <label for="koreanName">한글 이름 *</label>
                <input type="text" id="koreanName" name="koreanName" required 
                       placeholder="한글 이름을 입력하세요" autocomplete="name">
            </div>
            
            <div class="form-group">
                <label for="englishName">영문 이름 *</label>
                <input type="text" id="englishName" name="englishName" required 
                       placeholder="영문 이름을 입력하세요 (예: Hong Gil Dong)">
            </div>
            
            <div class="form-group">
                <label for="birthDate">생년월일 *</label>
                <input type="date" id="birthDate" name="birthDate" required>
            </div>
            
            <div class="form-group">
                <label for="gender">성별 *</label>
                <select id="gender" name="gender" required>
                    <option value="">성별을 선택하세요</option>
                    <option value="M">남성</option>
                    <option value="F">여성</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="email">이메일 주소 *</label>
                <input type="email" id="email" name="email" required 
                       placeholder="이메일을 입력하세요" autocomplete="email">
            </div>
            
            <div class="form-group">
                <label for="phone">휴대폰 번호 *</label>
                <input type="tel" id="phone" name="phone" required 
                       placeholder="휴대폰 번호를 입력하세요 (예: 010-1234-5678)" autocomplete="tel">
            </div>
            
            <div class="form-group">
                <label for="address">자택 주소</label>
                <div class="address-input-group">
                    <div class="address-search">
                        <input type="text" id="postcode" name="postcode" placeholder="우편번호" readonly>
                        <button type="button" class="btn-address-search" onclick="openPostcodeSearch()">
                            <i class="fas fa-search"></i> 주소 검색
                        </button>
                    </div>
                    <input type="text" id="roadAddress" name="roadAddress" placeholder="도로명주소" readonly>
                    <input type="text" id="jibunAddress" name="jibunAddress" placeholder="지번주소" readonly style="display: none;">
                    <input type="text" id="detailAddress" name="detailAddress" placeholder="상세주소를 입력하세요">
                    <input type="hidden" id="address" name="address">
                </div>
                <small class="address-help-text" style="margin-top: 1rem;">
                    <i class="fas fa-info-circle"></i> 
                    * 표시는 필수 입력 사항입니다
                </small>
            </div>
            
            <button type="submit" class="btn btn-primary">회원가입</button>
        </form>
        
        <div class="links">
            <a href="loginForm.do">이미 계정이 있으신가요? 로그인</a>
            <span style="color: #ccc;">|</span>
            <a href="./">메인 페이지로</a>
        </div>
        </div>
    </main>
    
    <jsp:include page="/views/common/footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/js/index.js"></script>
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html> 