<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제 실패 - 대한항공</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        
        .failure-container {
            max-width: 600px;
            margin: 50px auto;
            background: white;
            border-radius: 12px;
            padding: 40px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        
        .failure-icon {
            width: 80px;
            height: 80px;
            background-color: #dc3545;
            border-radius: 50%;
            margin: 0 auto 30px;
            position: relative;
        }
        
        .failure-icon::after {
            content: '✕';
            color: white;
            font-size: 40px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
        
        .failure-title {
            font-size: 28px;
            font-weight: bold;
            color: #333;
            margin-bottom: 15px;
        }
        
        .failure-message {
            font-size: 16px;
            color: #666;
            margin-bottom: 30px;
            line-height: 1.5;
        }
        
        .error-details {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 8px;
            padding: 20px;
            margin: 30px 0;
            color: #721c24;
        }
        
        .error-title {
            font-weight: 600;
            margin-bottom: 10px;
        }
        
        .help-section {
            background-color: #d1ecf1;
            border: 1px solid #bee5eb;
            border-radius: 8px;
            padding: 20px;
            margin: 30px 0;
            text-align: left;
        }
        
        .help-title {
            font-weight: 600;
            color: #0c5460;
            margin-bottom: 15px;
        }
        
        .help-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        
        .help-list li {
            margin-bottom: 8px;
            color: #0c5460;
        }
        
        .help-list li::before {
            content: '•';
            margin-right: 8px;
            color: #17a2b8;
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
        
        .btn-danger {
            background-color: #dc3545;
            color: white;
        }
        
        .btn-danger:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
    <div class="failure-container">
        <div class="failure-icon"></div>
        
        <h1 class="failure-title">결제 검증에 실패했습니다</h1>
        <p class="failure-message">
            결제 처리 중 문제가 발생했습니다.<br>
            아래 안내사항을 확인하시고 다시 시도해 주세요.
        </p>
        
        <%
            String errorMessage = (String) request.getAttribute("error");
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
        <div class="error-details">
            <div class="error-title">오류 내용</div>
            <div><%= errorMessage %></div>
        </div>
        <% } %>
        
        <div class="help-section">
            <div class="help-title">해결 방법</div>
            <ul class="help-list">
                <li>결제 정보가 정확한지 다시 한번 확인해 주세요</li>
                <li>네트워크 연결 상태를 확인해 주세요</li>
                <li>잠시 후 다시 시도해 주세요</li>
                <li>문제가 지속되면 고객센터로 문의해 주세요</li>
            </ul>
        </div>
        
        <div class="button-group">
            <a href="javascript:history.back();" class="btn btn-primary">다시 시도</a>
            <a href="/index.jsp" class="btn btn-secondary">메인으로</a>
            <a href="tel:1588-2001" class="btn btn-danger">고객센터</a>
        </div>
    </div>
</body>
</html> 