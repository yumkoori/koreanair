<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AirLogin - 오류</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class="container">
        <h1>오류 발생</h1>
        
        <%
            String error = (String) request.getAttribute("error");
            if (error == null) {
                error = "알 수 없는 오류가 발생했습니다.";
            }
        %>
        
        <div class="error">
            <%= error %>
        </div>
        
        <div class="actions">
            <a href="./" class="btn btn-primary">홈으로 돌아가기</a>
            <button onclick="history.back()" class="btn btn-secondary">이전 페이지로</button>
        </div>
        
        <div class="links">
            <p style="color: #999; font-size: 12px; margin-top: 2rem;">
                문제가 지속되면 관리자에게 문의해주세요.
            </p>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html> 