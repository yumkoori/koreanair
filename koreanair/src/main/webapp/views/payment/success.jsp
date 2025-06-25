<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koreanair.model.dto.PaymentVerifyDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제 완료 - 대한항공</title>
    <style>
        /* 전체 스타일 초기화 */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Noto Sans KR', sans-serif;
            color: #333;
            line-height: 1.6;
            background: linear-gradient(135deg, #e3f2fd 0%, #f8bbd9 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .success-container {
            max-width: 700px;
            margin: 50px auto;
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            text-align: center;
            backdrop-filter: blur(10px);
            background: rgba(255, 255, 255, 0.95);
        }
        
        .success-icon {
            width: 80px;
            height: 80px;
            background: linear-gradient(135deg, #28a745 0%, #34ce57 100%);
            border-radius: 50%;
            margin: 0 auto 30px;
            position: relative;
            box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
        }
        
        .success-icon::after {
            content: '✓';
            color: white;
            font-size: 40px;
            font-weight: bold;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
        
        .success-title {
            font-size: 28px;
            font-weight: 600;
            color: #333;
            margin-bottom: 15px;
        }
        
        .success-message {
            font-size: 16px;
            color: #666;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        
        .payment-details {
            background: white;
            border: 1px solid #f0f0f0;
            border-radius: 10px;
            padding: 25px;
            margin: 30px 0;
            text-align: left;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
            transition: box-shadow 0.3s ease;
        }
        
        .payment-details:hover {
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }
        
        .detail-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #f0f0f0;
            font-size: 14px;
        }
        
        .detail-row:last-child {
            margin-bottom: 0;
            padding-bottom: 0;
            border-bottom: none;
        }
        
        .detail-label {
            font-weight: 500;
            color: #666;
        }
        
        .detail-value {
            color: #333;
            font-weight: 600;
        }
        
        .button-group {
            margin-top: 40px;
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
        }
        
        .btn {
            padding: 15px 30px;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
            min-width: 150px;
            text-align: center;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #0064de 0%, #0078d4 100%);
            color: white;
            box-shadow: 0 4px 15px rgba(0, 100, 222, 0.3);
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0, 100, 222, 0.4);
        }
        
        .btn-secondary {
            background: white;
            color: #666;
            border: 1px solid #ddd;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        
        .btn-secondary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
            border-color: #0064de;
            color: #0064de;
        }

        /* 반응형 디자인 */
        @media (max-width: 768px) {
            body {
                padding: 10px;
            }
            
            .success-container {
                margin: 20px auto;
                padding: 30px 20px;
            }
            
            .success-title {
                font-size: 24px;
            }
            
            .success-message {
                font-size: 14px;
            }
            
            .payment-details {
                padding: 20px;
                margin: 20px 0;
            }
            
            .detail-row {
                flex-direction: column;
                align-items: flex-start;
                gap: 5px;
                margin-bottom: 12px;
                padding-bottom: 12px;
            }
            
            .button-group {
                flex-direction: column;
                gap: 10px;
            }
            
            .btn {
                padding: 12px 24px;
                font-size: 14px;
                min-width: 120px;
            }
        }
        
        @media (max-width: 480px) {
            .success-container {
                margin: 10px auto;
                padding: 25px 15px;
            }
            
            .success-icon {
                width: 60px;
                height: 60px;
            }
            
            .success-icon::after {
                font-size: 30px;
            }
            
            .success-title {
                font-size: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="success-icon"></div>
        
        <h1 class="success-title">결제가 완료되었습니다!</h1>
        <p class="success-message">
            결제가 정상적으로 완료되었습니다.<br>
			주문 내역은 메인페이지 예약조회를 통해 확인하실 수 있습니다.<br>
			감사합니다.
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
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary">예약 내역 확인</a>
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">메인으로</a>
        </div>
    </div>
</body>
</html> 