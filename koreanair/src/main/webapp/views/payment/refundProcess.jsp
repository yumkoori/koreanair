<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>환불 확인 - 대한항공</title>
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- CSS 파일 링크 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/refundProcess.css">
</head>
<body>
    <!-- 배경 데코레이션 -->
    <div class="background-decoration">
        <div class="floating-circle circle-1"></div>
        <div class="floating-circle circle-2"></div>
        <div class="floating-circle circle-3"></div>
    </div>

    <!-- 환불 확인 컨테이너 -->
    <div class="confirmation-container">
        <!-- 아이콘을 더 화려하게 -->
        <div class="icon">
            <i class="fas fa-exclamation-triangle"></i>
        </div>
        
        <!-- 메인 메시지 -->
        <div class="confirmation-message">
            <i class="fas fa-undo-alt" style="margin-right: 10px; color: #0064de;"></i>
            정말 환불하시겠습니까?
        </div>
        
        <!-- 예약 정보 표시 (향상된 디자인) -->
        <c:if test="${not empty bookingId or not empty passengerName}">
            <div class="booking-info">
                <div class="booking-info-header">
                    <i class="fas fa-ticket-alt"></i>
                    <span>예약 정보</span>
                </div>
                <c:if test="${not empty bookingId}">
                    <div class="info-row">
                        <span class="info-label">
                            <i class="fas fa-hashtag"></i>
                            예약번호
                        </span>
                        <span class="info-value">${bookingId}</span>
                    </div>
                </c:if>
                <c:if test="${not empty passengerName}">
                    <div class="info-row">
                        <span class="info-label">
                            <i class="fas fa-user"></i>
                            승객명
                        </span>
                        <span class="info-value">${passengerName}</span>
                    </div>
                </c:if>
            </div>
        </c:if>
        
        <!-- 환불 안내 메시지 -->
        <div class="refund-notice">
            <div class="notice-header">
                <i class="fas fa-info-circle"></i>
                <span>환불 안내</span>
            </div>
            <ul class="notice-list">
                <li><i class="fas fa-check-circle"></i> 환불 처리는 최대 3-5 영업일이 소요됩니다</li>
                <li><i class="fas fa-check-circle"></i> 환불 수수료가 발생할 수 있습니다</li>
                <li><i class="fas fa-check-circle"></i> 환불 완료 시 SMS/이메일로 알려드립니다</li>
            </ul>
        </div>
        
        <!-- 버튼 컨테이너 -->
        <div class="button-container">
            <button class="btn btn-yes" onclick="handleRefund()">
                <i class="fas fa-check"></i>
                <span>예</span>
            </button>
            <button class="btn btn-no" onclick="handleCancel()">
                <i class="fas fa-times"></i>
                <span>아니오</span>
            </button>
        </div>
    </div>

    <!-- 로딩 화면 (향상된 디자인) -->
    <div class="loading" id="loading">
        <div class="loading-container">
            <div class="spinner-container">
                <div class="spinner"></div>
                <div class="spinner-inner"></div>
            </div>
            <div class="loading-text">
                <i class="fas fa-credit-card"></i>
                환불 처리 중...
            </div>
            <div class="loading-subtext">잠시만 기다려 주세요</div>
        </div>
    </div>

    <!-- 환불 완료 메시지 (향상된 디자인) -->
    <div class="result-message" id="resultMessage" style="display: none;">
        <div class="result-content">
            <div class="result-icon-container">
                <div class="result-icon" id="resultIcon"></div>
            </div>
            <div class="result-text-container">
                <div class="result-title" id="resultTitle"></div>
                <div class="result-text" id="resultText"></div>
            </div>
            <button class="btn btn-ok" onclick="goToMain()">
                <i class="fas fa-home"></i>
                <span>메인으로</span>
            </button>
        </div>
    </div>

    <!-- 예약번호를 JavaScript에서 사용할 수 있도록 숨김 필드로 전달 -->
    <input type="hidden" id="bookingId" value="${param.bookingId}">
    <input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">

    <!-- JavaScript 파일 링크 -->
    <script>
        const contextPath = "${pageContext.request.contextPath}";
        
        // 페이지 로드 애니메이션
        window.addEventListener('load', function() {
            document.body.classList.add('loaded');
        });
        
        // 배경 원 애니메이션
        function animateCircles() {
            const circles = document.querySelectorAll('.floating-circle');
            circles.forEach((circle, index) => {
                const delay = index * 2000;
                setTimeout(() => {
                    circle.style.animation = `float 6s ease-in-out infinite ${delay}ms`;
                }, delay);
            });
        }
        
        // 페이지 로드 시 애니메이션 시작
        document.addEventListener('DOMContentLoaded', animateCircles);
    </script>
    <script src="${pageContext.request.contextPath}/js/refundProcess.js"></script>
</body>
</html>

<%--
사용 예시:
1. 예약 목록에서 환불 버튼 클릭 시:
   <a href="${pageContext.request.contextPath}/refundProcess.do?bookingId=${booking.id}" class="refund-btn">환불하기</a>

2. 컨트롤러를 통해 접근하는 경우:
   <form action="${pageContext.request.contextPath}/refundProcess.do" method="get">
       <input type="hidden" name="bookingId" value="${booking.id}">
       <button type="submit">환불하기</button>
   </form>

3. 파라미터 전달이 필요한 경우:
   <a href="${pageContext.request.contextPath}/refundProcess.do?bookingId=BKDON002&passengerName=홍길동">환불하기</a>
--%>