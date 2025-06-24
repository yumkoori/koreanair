<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>환불 확인 - 대한항공</title>
    
    <!-- CSS 파일 링크 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/refundProcess.css">
</head>
<body>
    <!-- 환불 확인 컨테이너 -->
    <div class="confirmation-container">
        <div class="icon">❓</div>
        <div class="confirmation-message">
            정말 환불하시겠습니까?
        </div>
        
        <!-- 예약 정보 표시 -->
        <c:if test="${not empty bookingId or not empty passengerName}">
            <div class="booking-info">
                <c:if test="${not empty bookingId}">
                    <div class="info-row">
                        <span class="info-label">예약번호:</span>
                        <span class="info-value">${bookingId}</span>
                    </div>
                </c:if>
            </div>
        </c:if>
        
        <div class="button-container">
            <button class="btn btn-yes" onclick="handleRefund()">예</button>
            <button class="btn btn-no" onclick="handleCancel()">아니오</button>
        </div>
    </div>

    <!-- 로딩 화면 -->
    <div class="loading" id="loading">
        <div>
            <div class="spinner"></div>
            <div class="loading-text">환불 처리 중...</div>
        </div>
    </div>

    <!-- 환불 완료 메시지 -->
    <div class="result-message" id="resultMessage" style="display: none;">
        <div class="result-content">
            <div class="result-icon" id="resultIcon"></div>
            <div class="result-text" id="resultText"></div>
            <button class="btn btn-ok" onclick="goToMain()">확인</button>
        </div>
    </div>

    <!-- 예약번호를 JavaScript에서 사용할 수 있도록 숨김 필드로 전달 -->
    										<!--${bookingId} -->
    <input type="hidden" id="bookingId" value="">
    <input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">

    <!-- JavaScript 파일 링크 -->
    <script>const contextPath = "${pageContext.request.contextPath}";</script>
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