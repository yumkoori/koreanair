<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AirLogin - 오류</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body class="airline-main-body">
    <jsp:include page="/views/common/header.jsp" />
    <main class="main-content">
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
    </main>
    
    <jsp:include page="/views/common/footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/js/index.js"></script>
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html> 