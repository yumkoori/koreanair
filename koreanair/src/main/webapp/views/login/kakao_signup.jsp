<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koreanair.util.KakaoApiService.KakaoUserInfo" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>카카오 회원가입 - AirLogin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class="container">
        <h1>카카오 회원가입</h1>
        <p style="margin-bottom: 2rem; color: #666;">추가 정보를 입력해주세요</p>
        
        <%
            // 에러 메시지 표시
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="error"><%= error %></div>
        <%
            }
            
            // 세션에서 카카오 사용자 정보 가져오기
            KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) session.getAttribute("kakaoUserInfo");
            if (kakaoUserInfo == null) {
        %>
            <div class="error">세션이 만료되었습니다. 다시 로그인해주세요.</div>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/loginForm.do" class="btn btn-primary">로그인 페이지로</a>
            </div>
        <%
            } else {
        %>
            <!-- 카카오 사용자 정보 표시 -->
            <div class="kakao-info" style="background: #f8f9fa; padding: 1rem; border-radius: 8px; margin-bottom: 1.5rem;">
                <h3>카카오 계정 정보</h3>
                <% if (kakaoUserInfo.getProfileImageUrl() != null) { %>
                    <img src="<%= kakaoUserInfo.getProfileImageUrl() %>" alt="프로필 이미지" 
                         style="width: 60px; height: 60px; border-radius: 50%; margin-bottom: 10px;">
                <% } %>
                <p><strong>닉네임:</strong> <%= kakaoUserInfo.getNickname() != null ? kakaoUserInfo.getNickname() : "미제공" %></p>
                <p><strong>이메일:</strong> <%= kakaoUserInfo.getEmail() != null ? kakaoUserInfo.getEmail() : "미제공" %></p>
            </div>
            
            <!-- 추가 정보 입력 폼 -->
            <form action="${pageContext.request.contextPath}/kakao/signup.do" method="post" onsubmit="return validateKakaoSignupForm()">
                <div class="form-group">
                    <label for="koreanName">한글 이름 *</label>
                    <input type="text" id="koreanName" name="koreanName" required 
                           placeholder="한글 이름을 입력하세요"
                           value="<%= kakaoUserInfo.getNickname() != null ? kakaoUserInfo.getNickname() : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="englishName">영문 이름 *</label>
                    <input type="text" id="englishName" name="englishName" required 
                           placeholder="영문 이름을 입력하세요">
                </div>
                
                <div class="form-group">
                    <label for="birthDate">생년월일 *</label>
                    <input type="date" id="birthDate" name="birthDate" required>
                </div>
                
                <div class="form-group">
                    <label for="gender">성별 *</label>
                    <select id="gender" name="gender" required>
                        <option value="">선택하세요</option>
                        <option value="M">남성</option>
                        <option value="F">여성</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="phone">전화번호 *</label>
                    <input type="tel" id="phone" name="phone" required 
                           placeholder="010-1234-5678" pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}">
                </div>
                
                <div class="form-group">
                    <label for="address">주소</label>
                    <input type="text" id="address" name="address" 
                           placeholder="주소를 입력하세요 (선택사항)">
                </div>
                
                <button type="submit" class="btn btn-primary">회원가입 완료</button>
            </form>
            
            <div class="actions" style="margin-top: 1rem;">
                <a href="${pageContext.request.contextPath}/loginForm.do" class="btn btn-secondary">취소</a>
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
    
    <script>
        function validateKakaoSignupForm() {
            const koreanName = document.getElementById('koreanName').value.trim();
            const englishName = document.getElementById('englishName').value.trim();
            const birthDate = document.getElementById('birthDate').value;
            const gender = document.getElementById('gender').value;
            const phone = document.getElementById('phone').value.trim();
            
            if (!koreanName || !englishName || !birthDate || !gender || !phone) {
                alert('모든 필수 항목을 입력해주세요.');
                return false;
            }
            
            // 전화번호 형식 검증
            const phonePattern = /^[0-9]{3}-[0-9]{4}-[0-9]{4}$/;
            if (!phonePattern.test(phone)) {
                alert('전화번호는 010-1234-5678 형식으로 입력해주세요.');
                return false;
            }
            
            return true;
        }
        
        // 전화번호 자동 하이픈 추가
        document.getElementById('phone').addEventListener('input', function(e) {
            let value = e.target.value.replace(/[^0-9]/g, '');
            if (value.length >= 3 && value.length <= 7) {
                value = value.replace(/(\d{3})(\d{1,4})/, '$1-$2');
            } else if (value.length >= 8) {
                value = value.replace(/(\d{3})(\d{4})(\d{1,4})/, '$1-$2-$3');
            }
            e.target.value = value;
        });
    </script>
</body>
</html> 