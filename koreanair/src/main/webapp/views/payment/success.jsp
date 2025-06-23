<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koreanair.model.dto.PaymentVerifyDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제 완료 - 대한항공</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        
        .success-container {
            max-width: 600px;
            margin: 50px auto;
            background: white;
            border-radius: 12px;
            padding: 40px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        
        .success-icon {
            width: 80px;
            height: 80px;
            background-color: #4CAF50;
            border-radius: 50%;
            margin: 0 auto 30px;
            position: relative;
        }
        
        .success-icon::after {
            content: '✓';
            color: white;
            font-size: 40px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
        
        .success-title {
            font-size: 28px;
            font-weight: bold;
            color: #333;
            margin-bottom: 15px;
        }
        
        .success-message {
            font-size: 16px;
            color: #666;
            margin-bottom: 30px;
            line-height: 1.5;
        }
        
        .payment-details {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 25px;
            margin: 30px 0;
            text-align: left;
        }
        
        .detail-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #e9ecef;
        }
        
        .detail-row:last-child {
            margin-bottom: 0;
            padding-bottom: 0;
            border-bottom: none;
        }
        
        .detail-label {
            font-weight: 600;
            color: #495057;
        }
        
        .detail-value {
            color: #333;
            font-weight: 500;
        }
        
        .button-group {
            margin-top: 40px;
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin: 0 10px;
            transition: all 0.3s ease;
        }
        
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        
        .btn-primary:hover {
            background-color: #0056b3;
        }
        
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        
        .btn-secondary:hover {
            background-color: #545b62;
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="success-icon"></div>
        
        <h1 class="success-title">결제가 완료되었습니다!</h1>
        <p class="success-message">
            결제 검증이 성공적으로 완료되었습니다.<br>
            예약 확인서는 이메일로 발송되었습니다.
        </p>
        
        <%
                PaymentVerifyDTO paymentInfo = (PaymentVerifyDTO) request.getAttribute("paymentInfo");
                            if (paymentInfo != null) {
                %>
        <div class="payment-details">
            <div class="detail-row">
                <span class="detail-label">결제 번호</span>
                <span class="detail-value"><%= paymentInfo.getImpUid() %></span>
            </div>
            <div class="detail-row">
                <span class="detail-label">주문 번호</span>
                <span class="detail-value"><%= paymentInfo.getMerchantUid() %></span>
            </div>
            <div class="detail-row">
                <span class="detail-label">결제 금액</span>
                <span class="detail-value"><%= String.format("%,d", paymentInfo.getAmount()) %>원</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">결제 방법</span>
                <span class="detail-value"><%= paymentInfo.getPayMethod() != null ? paymentInfo.getPayMethod() : "카드결제" %></span>
            </div>
            <div class="detail-row">
                <span class="detail-label">결제 상태</span>
                <span class="detail-value">완료</span>
            </div>
            <% if (paymentInfo.getPaidAt() != null && !paymentInfo.getPaidAt().isEmpty()) { %>
            <div class="detail-row">
                <span class="detail-label">결제 완료 시간</span>
                <span class="detail-value"><%= paymentInfo.getPaidAt() %></span>
            </div>
            <% } %>
        </div>
        <% } %>
        
        <div class="button-group">
            <a href="/views/reservationDetail.jsp" class="btn btn-primary">예약 내역 확인</a>
            <a href="/index.jsp" class="btn btn-secondary">메인으로</a>
        </div>
    </div>
</body>
</html> 