<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>항공권 예약 - 대한항공</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap" rel="stylesheet">
    
    <!-- contextPath 및 bookingId 설정 -->
    <script>
        window.contextPath = '${pageContext.request.contextPath}';
        
        // 서버에서 전달된 bookingId 먼저 시도
        window.bookingId = '${bookingId}';
        
        // bookingId가 비어있으면 URL 파라미터에서 가져오기
        if (!window.bookingId || window.bookingId === '') {
            const urlParams = new URLSearchParams(window.location.search);
            window.bookingId = urlParams.get('bookingId') || '';
            console.log('⚠️ URL 파라미터에서 bookingId 추출:', window.bookingId);
        }
        
        // 그래도 없으면 임시 ID 생성
        if (!window.bookingId || window.bookingId === '') {
            window.bookingId = 'TEMP-' + Date.now();
            console.log('⚠️ 임시 bookingId 생성:', window.bookingId);
        }
        
        window.outBookingId = '${outBookingId}';
        window.returnBookingId = '${returnBookingId}';
        
        console.log('✅ contextPath 설정됨:', window.contextPath);
        console.log('✅ 최종 bookingId:', window.bookingId);
        console.log('outBookingId:', window.outBookingId);
        console.log('returnBookingId:', window.returnBookingId);
        
        // 디버깅을 위한 추가 정보
        console.log('🔍 서버에서 전달된 bookingId (raw):', '${bookingId}');
        console.log('🔍 현재 URL:', window.location.href);
    </script>
</head>
<body>
    <jsp:include page="/views/common/header.jsp" />

    <main class="booking-container">
        <!-- 진행 단계 표시 -->
        <div class="progress-bar">
            <div class="progress-step completed">
                <div class="step-number">01</div>
                <div class="step-label">항공편 선택</div>
            </div>
            <div class="progress-step active">
                <div class="step-number">02</div>
                <div class="step-label">승객 정보</div>
            </div>
            <div class="progress-step">
                <div class="step-number">03</div>
                <div class="step-label">결제</div>
            </div>
        </div>

        <div class="main-content">
            <div class="content-left">
                <!-- 여정 정보 섹션 -->
                <section class="journey-info">
                    <div class="section-header">
                        <h2>여정 정보</h2>
                        <button class="share-btn">
                            <i class="fas fa-share-alt"></i>
                            공유
                        </button>
                    </div>

                    <%
                        // URL 파라미터에서 예약 정보 가져오기
                        String tripType = request.getParameter("tripType");
                        String departure = request.getParameter("departure");
                        String arrival = request.getParameter("arrival");
                        String departureDate = request.getParameter("departureDate");
                        String returnDate = request.getParameter("returnDate");
                        
                        // 편도 여행 정보
                        String flightId = request.getParameter("flightId");
                        String fareType = request.getParameter("fareType");
                        
                        // 왕복 여행 정보
                        String outboundFlightId = request.getParameter("outboundFlightId");
                        String returnFlightId = request.getParameter("returnFlightId");
                        String outboundFareType = request.getParameter("outboundFareType");
                        String returnFareType = request.getParameter("returnFareType");
                        String outboundPrice = request.getParameter("outboundPrice");
                        String returnPrice = request.getParameter("returnPrice");
                        
                        boolean isRoundTrip = "round".equals(tripType);
                    %>
                    
                    <!-- 가는 편 -->
                    <div class="flight-card">
                        <div class="flight-header">
                            <h3>가는 편</h3>
                            <div class="route">
                                <span class="departure"><%=departure != null ? departure : "CJU"%> <%=departure != null && departure.equals("CJU") ? "제주" : departure != null && departure.equals("GMP") ? "서울/김포" : ""%></span>
                                <i class="fas fa-long-arrow-alt-right"></i>
                                <span class="arrival"><%=arrival != null ? arrival : "GMP"%> <%=arrival != null && arrival.equals("GMP") ? "서울/김포" : arrival != null && arrival.equals("CJU") ? "제주" : ""%></span>
                            </div>
                            <button class="details-btn">상세 보기</button>
                        </div>
                        <div class="flight-details">
                            <div class="flight-time">
                                <%
                                    if (departureDate != null) {
                                        String formattedDate = departureDate.replace("-", "년 ").replace("-", "월 ") + "일";
                                        out.print(formattedDate + " 08:15 - 09:30");
                                    } else {
                                        out.print("2025년 06월 19일 08:15 - 09:30");
                                    }
                                %>
                            </div>
                            <div class="flight-info">
                                <span class="flight-number"><%=isRoundTrip ? (outboundFlightId != null ? outboundFlightId : "KE1142") : (flightId != null ? flightId : "KE1142")%></span>
                                <span class="aircraft"><%=isRoundTrip ? (outboundFareType != null ? outboundFareType : "일반석") : (fareType != null ? fareType : "일반석")%></span>
                            </div>
                        </div>
                    </div>

                    <% if (isRoundTrip) { %>
                    <!-- 오는 편 (왕복일 때만 표시) -->
                    <div class="flight-card">
                        <div class="flight-header">
                            <h3>오는 편</h3>
                            <div class="route">
                                <span class="departure"><%=arrival != null ? arrival : "GMP"%> <%=arrival != null && arrival.equals("GMP") ? "서울/김포" : arrival != null && arrival.equals("CJU") ? "제주" : ""%></span>
                                <i class="fas fa-long-arrow-alt-right"></i>
                                <span class="arrival"><%=departure != null ? departure : "CJU"%> <%=departure != null && departure.equals("CJU") ? "제주" : departure != null && departure.equals("GMP") ? "서울/김포" : ""%></span>
                            </div>
                            <button class="details-btn">상세 보기</button>
                        </div>
                        <div class="flight-details">
                            <div class="flight-time">
                                <%
                                    if (returnDate != null) {
                                        String formattedReturnDate = returnDate.replace("-", "년 ").replace("-", "월 ") + "일";
                                        out.print(formattedReturnDate + " 06:15 - 07:30");
                                    } else {
                                        out.print("2025년 06월 27일 06:15 - 07:30");
                                    }
                                %>
                            </div>
                            <div class="flight-info">
                                <span class="flight-number"><%=returnFlightId != null ? returnFlightId : "KE5153"%></span>
                                <span class="aircraft"><%=returnFareType != null ? returnFareType : "일반석"%></span>
                                <div class="special-service">
                                    <i class="fas fa-snowflake" style="color: #00bcd4;"></i>
                                    <span>진에어 운항</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </section>

                <!-- 승객 정보 입력 폼 -->
                <section class="passenger-info">
                    <div class="section-header">
                        <h2>승객 정보</h2>
                        <div class="required-note">
                            <i class="fas fa-info-circle"></i>
                            필수 입력 사항입니다
                        </div>
                    </div>

                    <%
                        // 승객 정보 파싱 로직
                        String passengersParam = request.getParameter("passengers");
                        String passengerDisplay = passengersParam != null ? passengersParam : "성인 1명";
                        
                        System.out.println("=== 승객 정보 파싱 ===");
                        System.out.println("원본 passengers 파라미터: " + passengersParam);
                        System.out.println("URL 쿼리 스트링: " + request.getQueryString());
                        
                        // URL 디코딩 처리
                        String decodedPassengers = passengersParam;
                        if (passengersParam != null) {
                            try {
                                decodedPassengers = java.net.URLDecoder.decode(passengersParam, "UTF-8");
                                System.out.println("디코딩 후 passengers 파라미터: " + decodedPassengers);
                            } catch (Exception e) {
                                System.out.println("URL 디코딩 오류: " + e.getMessage());
                                decodedPassengers = passengersParam;
                            }
                        }
                        
                        // 승객 수 파싱
                        int adultCount = 0;
                        int childCount = 0;
                        int infantCount = 0;
                        
                        if (decodedPassengers != null && !decodedPassengers.trim().isEmpty()) {
                            // 더 유연한 패턴으로 변경 (공백과 특수문자 처리)
                            // "성인 2명", "성인+2명", "성인2명" 등 다양한 형태 지원
                            java.util.regex.Pattern adultPattern = java.util.regex.Pattern.compile("성인[\\s\\+]*(\\d+)명");
                            java.util.regex.Matcher adultMatcher = adultPattern.matcher(decodedPassengers);
                            if (adultMatcher.find()) {
                                adultCount = Integer.parseInt(adultMatcher.group(1));
                                System.out.println("성인 매칭됨: " + adultMatcher.group(0) + " -> " + adultCount);
                            } else {
                                System.out.println("성인 패턴 매칭 실패");
                            }
                            
                            java.util.regex.Pattern childPattern = java.util.regex.Pattern.compile("소아[\\s\\+]*(\\d+)명");
                            java.util.regex.Matcher childMatcher = childPattern.matcher(decodedPassengers);
                            if (childMatcher.find()) {
                                childCount = Integer.parseInt(childMatcher.group(1));
                                System.out.println("소아 매칭됨: " + childMatcher.group(0) + " -> " + childCount);
                            }
                            
                            java.util.regex.Pattern infantPattern = java.util.regex.Pattern.compile("유아[\\s\\+]*(\\d+)명");
                            java.util.regex.Matcher infantMatcher = infantPattern.matcher(decodedPassengers);
                            if (infantMatcher.find()) {
                                infantCount = Integer.parseInt(infantMatcher.group(1));
                                System.out.println("유아 매칭됨: " + infantMatcher.group(0) + " -> " + infantCount);
                            }
                            
                            // 패턴 매칭이 모두 실패한 경우를 위한 fallback
                            if (adultCount == 0 && childCount == 0 && infantCount == 0) {
                                System.out.println("모든 패턴 매칭 실패, 기본값 설정");
                                adultCount = 1; // 기본값
                            }
                        } else {
                            System.out.println("passengers 파라미터 없음, 기본값 설정");
                            adultCount = 1; // 기본값
                        }
                        
                        System.out.println("최종 결과 - 성인: " + adultCount + "명, 소아: " + childCount + "명, 유아: " + infantCount + "명");
                        
                        int totalPassengers = adultCount + childCount + infantCount;
                        int passengerIndex = 0;
                        
                        // 디버깅을 위한 상세 로그
                        System.out.println("총 승객 수: " + totalPassengers);
                        System.out.println("성인 승객 폼 생성 시작 (adultCount=" + adultCount + ")");
                    %>

                    <!-- 승객 정보 입력 폼 -->
                    <form id="passengerInfoForm" class="passenger-info-form">
                        
                        <%
                            // 성인 승객 폼 생성
                            System.out.println("🔄 JSP: 성인 승객 폼 생성 루프 시작, adultCount=" + adultCount);
                            for (int i = 1; i <= adultCount; i++) {
                                passengerIndex++;
                                System.out.println("🔄 JSP: 성인 승객 " + i + " 카드 생성 중, passengerIndex=" + passengerIndex);
                        %>
                        <!-- 성인 승객 <%= i %> 정보 카드 -->
                        <div class="passenger-card" id="passengerCard<%= passengerIndex %>">
                            <!-- 승객 헤더 (클릭 시 토글) -->
                            <div class="passenger-card-header" onclick="togglePassengerCard('passengerCard<%= passengerIndex %>')">
                                <div class="passenger-title">
                                    <h3>성인 <%= i %></h3>
                                    <span class="passenger-badge">성인</span>
                                </div>
                                <div class="passenger-summary" id="passengerSummary<%= passengerIndex %>" style="display: none;">
                                    <span class="summary-text">김 또는 KIM / 대한 또는 DAEHAN</span>
                                </div>
                                <i class="fas fa-chevron-down toggle-icon" id="toggleIcon<%= passengerIndex %>"></i>
                            </div>
                            
                            <!-- 승객 폼 내용 (접힐 수 있는 부분) -->
                            <div class="passenger-card-content" id="passengerContent<%= passengerIndex %>">
                                <div class="passenger-form-grid">
                                    <!-- 국적 -->
                                    <div class="form-group full-width">
                                        <label for="nationality<%= passengerIndex %>" class="required">국적</label>
                                        <select id="nationality<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].nationality" required>
                                            <option value="">대한민국</option>
                                            <option value="KR" selected>대한민국</option>
                                            <option value="US">미국</option>
                                            <option value="JP">일본</option>
                                            <option value="CN">중국</option>
                                            <option value="OTHER">기타</option>
                                        </select>
                                    </div>

                                    <!-- 승객 성 -->
                                    <div class="form-group">
                                        <label for="lastName<%= passengerIndex %>" class="required">승객 성</label>
                                        <input type="text" id="lastName<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].lastName" 
                                               placeholder="예) 김 또는 KIM" required>
                                    </div>

                                    <!-- 승객 이름 -->
                                    <div class="form-group">
                                        <label for="firstName<%= passengerIndex %>" class="required">승객 이름</label>
                                        <input type="text" id="firstName<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].firstName" 
                                               placeholder="예) 대한 또는 DAEHAN" required>
                                    </div>

                                    <!-- 성별 -->
                                    <div class="form-group">
                                        <label for="gender<%= passengerIndex %>" class="required">성별</label>
                                        <select id="gender<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].gender" required>
                                            <option value="">선택</option>
                                            <option value="M">남성</option>
                                            <option value="F">여성</option>
                                        </select>
                                    </div>

                                    <!-- 생년월일 -->
                                    <div class="form-group">
                                        <label for="birthDate<%= passengerIndex %>" class="required">생년월일(YYYY.MM.DD.)</label>
                                        <input type="text" id="birthDate<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].birthDate" 
                                               placeholder="YYYY.MM.DD" pattern="\d{4}\.\d{2}\.\d{2}" required>
                                    </div>

                                    <!-- 직업 항공사 -->
                                    <div class="form-group">
                                        <label for="jobAirline<%= passengerIndex %>">직업 항공사</label>
                                        <select id="jobAirline<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].jobAirline">
                                            <option value="">선택하지 않음</option>
                                            <option value="KE">대한항공</option>
                                            <option value="OZ">아시아나항공</option>
                                            <option value="7C">제주항공</option>
                                            <option value="OTHER">기타</option>
                                        </select>
                                    </div>

                                    <!-- 회원번호 -->
                                    <div class="form-group">
                                        <label for="memberNumber<%= passengerIndex %>">회원번호 <i class="fas fa-question-circle help-icon" title="항공사 회원번호를 입력하여 주십시오"></i></label>
                                        <input type="text" id="memberNumber<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].memberNumber" 
                                               placeholder="항공사 회원번호를 입력하여 주십시오">
                                    </div>
                                </div>

                                <!-- 가는 여정의 개인 할인 -->
                                <div class="discount-section">
                                    <h4>가는 여정의 개인 할인</h4>
                                    <div class="discount-grid">
                                        <div class="form-group">
                                            <label for="discountType<%= passengerIndex %>">할인</label>
                                            <select id="discountType<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].discountType">
                                                <option value="">선택</option>
                                                <option value="student">학생</option>
                                                <option value="senior">경로</option>
                                                <option value="disabled">장애인</option>
                                            </select>
                                        </div>

                                        <div class="form-group">
                                            <label for="returnDiscountType<%= passengerIndex %>">오는 여정의 개인 할인</label>
                                            <select id="returnDiscountType<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].returnDiscountType">
                                                <option value="">선택</option>
                                                <option value="student">학생</option>
                                                <option value="senior">경로</option>
                                                <option value="disabled">장애인</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <!-- 저장 버튼 -->
                                <div class="passenger-form-actions">
                                    <button type="button" class="passenger-save-btn" onclick="savePassengerInfo(<%= passengerIndex %>)">
                                        저장
                                    </button>
                                </div>
                            </div>
                        </div>
                        <% 
                            System.out.println("🔄 JSP: 성인 승객 " + i + " 카드 생성 완료");
                        } 
                        System.out.println("🔄 JSP: 성인 승객 폼 생성 루프 종료");
                        %>

                        <%
                            // 소아 승객 폼 생성
                            System.out.println("🔄 JSP: 소아 승객 폼 생성 루프 시작, childCount=" + childCount);
                            for (int i = 1; i <= childCount; i++) {
                                passengerIndex++;
                                System.out.println("🔄 JSP: 소아 승객 " + i + " 카드 생성 중, passengerIndex=" + passengerIndex);
                        %>
                        <!-- 소아 승객 <%= i %> 정보 카드 -->
                        <div class="passenger-card" id="passengerCard<%= passengerIndex %>">
                            <!-- 승객 헤더 (클릭 시 토글) -->
                            <div class="passenger-card-header" onclick="togglePassengerCard('passengerCard<%= passengerIndex %>')">
                                <div class="passenger-title">
                                    <h3>소아 <%= i %></h3>
                                    <span class="passenger-badge child">소아</span>
                                </div>
                                <div class="passenger-summary" id="passengerSummary<%= passengerIndex %>" style="display: none;">
                                    <span class="summary-text">김 또는 KIM / 대한 또는 DAEHAN</span>
                                </div>
                                <i class="fas fa-chevron-down toggle-icon" id="toggleIcon<%= passengerIndex %>"></i>
                            </div>
                            
                            <!-- 승객 폼 내용 (접힐 수 있는 부분) -->
                            <div class="passenger-card-content" id="passengerContent<%= passengerIndex %>">
                                <div class="passenger-form-grid">
                                    <!-- 국적 -->
                                    <div class="form-group full-width">
                                        <label for="nationality<%= passengerIndex %>" class="required">국적</label>
                                        <select id="nationality<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].nationality" required>
                                            <option value="">대한민국</option>
                                            <option value="KR" selected>대한민국</option>
                                            <option value="US">미국</option>
                                            <option value="JP">일본</option>
                                            <option value="CN">중국</option>
                                            <option value="OTHER">기타</option>
                                        </select>
                                    </div>

                                    <!-- 승객 성 -->
                                    <div class="form-group">
                                        <label for="lastName<%= passengerIndex %>" class="required">승객 성</label>
                                        <input type="text" id="lastName<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].lastName" 
                                               placeholder="예) 김 또는 KIM" required>
                                    </div>

                                    <!-- 승객 이름 -->
                                    <div class="form-group">
                                        <label for="firstName<%= passengerIndex %>" class="required">승객 이름</label>
                                        <input type="text" id="firstName<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].firstName" 
                                               placeholder="예) 대한 또는 DAEHAN" required>
                                    </div>

                                    <!-- 성별 -->
                                    <div class="form-group">
                                        <label for="gender<%= passengerIndex %>" class="required">성별</label>
                                        <select id="gender<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].gender" required>
                                            <option value="">선택</option>
                                            <option value="M">남성</option>
                                            <option value="F">여성</option>
                                        </select>
                                    </div>

                                    <!-- 생년월일 -->
                                    <div class="form-group">
                                        <label for="birthDate<%= passengerIndex %>" class="required">생년월일(YYYY.MM.DD.)</label>
                                        <input type="text" id="birthDate<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].birthDate" 
                                               placeholder="YYYY.MM.DD" pattern="\d{4}\.\d{2}\.\d{2}" required>
                                    </div>

                                    <!-- 직업 항공사 -->
                                    <div class="form-group">
                                        <label for="jobAirline<%= passengerIndex %>">직업 항공사</label>
                                        <select id="jobAirline<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].jobAirline">
                                            <option value="">선택하지 않음</option>
                                            <option value="KE">대한항공</option>
                                            <option value="OZ">아시아나항공</option>
                                            <option value="7C">제주항공</option>
                                            <option value="OTHER">기타</option>
                                        </select>
                                    </div>

                                    <!-- 회원번호 -->
                                    <div class="form-group">
                                        <label for="memberNumber<%= passengerIndex %>">회원번호 <i class="fas fa-question-circle help-icon" title="항공사 회원번호를 입력하여 주십시오"></i></label>
                                        <input type="text" id="memberNumber<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].memberNumber" 
                                               placeholder="항공사 회원번호를 입력하여 주십시오">
                                    </div>
                                </div>

                                <!-- 가는 여정의 개인 할인 -->
                                <div class="discount-section">
                                    <h4>가는 여정의 개인 할인</h4>
                                    <div class="discount-grid">
                                        <div class="form-group">
                                            <label for="discountType<%= passengerIndex %>">할인</label>
                                            <select id="discountType<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].discountType">
                                                <option value="">선택</option>
                                                <option value="student">학생</option>
                                                <option value="senior">경로</option>
                                                <option value="disabled">장애인</option>
                                            </select>
                                        </div>

                                        <div class="form-group">
                                            <label for="returnDiscountType<%= passengerIndex %>">오는 여정의 개인 할인</label>
                                            <select id="returnDiscountType<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].returnDiscountType">
                                                <option value="">선택</option>
                                                <option value="student">학생</option>
                                                <option value="senior">경로</option>
                                                <option value="disabled">장애인</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <!-- 저장 버튼 -->
                                <div class="passenger-form-actions">
                                    <button type="button" class="passenger-save-btn" onclick="savePassengerInfo(<%= passengerIndex %>)">
                                        저장
                                    </button>
                                </div>
                            </div>
                        </div>
                        <% } %>

                        <%
                            // 유아 승객 폼 생성
                            for (int i = 1; i <= infantCount; i++) {
                                passengerIndex++;
                        %>
                        <!-- 유아 승객 <%= i %> 정보 카드 -->
                        <div class="passenger-card" id="passengerCard<%= passengerIndex %>">
                            <!-- 승객 헤더 (클릭 시 토글) -->
                            <div class="passenger-card-header" onclick="togglePassengerCard('passengerCard<%= passengerIndex %>')">
                                <div class="passenger-title">
                                    <h3>유아 <%= i %></h3>
                                    <span class="passenger-badge infant">유아</span>
                                </div>
                                <div class="passenger-summary" id="passengerSummary<%= passengerIndex %>" style="display: none;">
                                    <span class="summary-text">김 또는 KIM / 대한 또는 DAEHAN</span>
                                </div>
                                <i class="fas fa-chevron-down toggle-icon" id="toggleIcon<%= passengerIndex %>"></i>
                            </div>
                            
                            <!-- 승객 폼 내용 (접힐 수 있는 부분) -->
                            <div class="passenger-card-content" id="passengerContent<%= passengerIndex %>">
                                <div class="passenger-form-grid">
                                    <!-- 국적 -->
                                    <div class="form-group full-width">
                                        <label for="nationality<%= passengerIndex %>" class="required">국적</label>
                                        <select id="nationality<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].nationality" required>
                                            <option value="">대한민국</option>
                                            <option value="KR" selected>대한민국</option>
                                            <option value="US">미국</option>
                                            <option value="JP">일본</option>
                                            <option value="CN">중국</option>
                                            <option value="OTHER">기타</option>
                                        </select>
                                    </div>

                                    <!-- 승객 성 -->
                                    <div class="form-group">
                                        <label for="lastName<%= passengerIndex %>" class="required">승객 성</label>
                                        <input type="text" id="lastName<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].lastName" 
                                               placeholder="예) 김 또는 KIM" required>
                                    </div>

                                    <!-- 승객 이름 -->
                                    <div class="form-group">
                                        <label for="firstName<%= passengerIndex %>" class="required">승객 이름</label>
                                        <input type="text" id="firstName<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].firstName" 
                                               placeholder="예) 대한 또는 DAEHAN" required>
                                    </div>

                                    <!-- 성별 -->
                                    <div class="form-group">
                                        <label for="gender<%= passengerIndex %>" class="required">성별</label>
                                        <select id="gender<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].gender" required>
                                            <option value="">선택</option>
                                            <option value="M">남성</option>
                                            <option value="F">여성</option>
                                        </select>
                                    </div>

                                    <!-- 생년월일 -->
                                    <div class="form-group">
                                        <label for="birthDate<%= passengerIndex %>" class="required">생년월일(YYYY.MM.DD.)</label>
                                        <input type="text" id="birthDate<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].birthDate" 
                                               placeholder="YYYY.MM.DD" pattern="\d{4}\.\d{2}\.\d{2}" required>
                                    </div>

                                    <!-- 직업 항공사 (유아에게는 필요없음) -->
                                    <div class="form-group">
                                        <label for="jobAirline<%= passengerIndex %>">직업 항공사</label>
                                        <select id="jobAirline<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].jobAirline" disabled>
                                            <option value="">유아는 해당 없음</option>
                                        </select>
                                    </div>

                                    <!-- 회원번호 (유아에게는 필요없음) -->
                                    <div class="form-group">
                                        <label for="memberNumber<%= passengerIndex %>">회원번호</label>
                                        <input type="text" id="memberNumber<%= passengerIndex %>" name="passengers[<%= passengerIndex - 1 %>].memberNumber" 
                                               placeholder="유아는 해당 없음" disabled>
                                    </div>
                                </div>

                                <!-- 유아는 할인 섹션 없음 -->
                                <div class="discount-section">
                                    <h4>할인 적용 안됨</h4>
                                    <p style="color: #666; font-size: 14px;">유아는 개인 할인이 적용되지 않습니다.</p>
                                </div>

                                <!-- 저장 버튼 -->
                                <div class="passenger-form-actions">
                                    <button type="button" class="passenger-save-btn" onclick="savePassengerInfo(<%= passengerIndex %>)">
                                        저장
                                    </button>
                                </div>
                            </div>
                        </div>
                        <% } %>
                    </form>
                </section>

                <!-- 연락처 정보 섹션 (별도 분리) -->
                <section class="contact-info">
                    <div class="section-header">
                        <h2>연락처 정보</h2>
                        <div class="required-note">
                            <i class="fas fa-info-circle"></i>
                            필수 입력 사항입니다
                        </div>
                    </div>

                    <form id="contactForm" class="contact-form">
                        <!-- 연락처 정보 -->
                        <div class="contact-section">
                            <div class="form-grid">
                                <div class="form-group">
                                    <label for="email" class="required">이메일</label>
                                    <input type="email" id="email" name="email" 
                                           placeholder="example@email.com" required>
                                    <small class="form-help">예약 확인서가 발송됩니다</small>
                                </div>

                                <div class="form-group">
                                    <label for="phone" class="required">연락처</label>
                                    <div class="phone-input">
                                        <select name="countryCode">
                                            <option value="+82">+82 (한국)</option>
                                            <option value="+1">+1 (미국)</option>
                                            <option value="+81">+81 (일본)</option>
                                            <option value="+86">+86 (중국)</option>
                                        </select>
                                        <input type="tel" id="phone" name="phone" 
                                               placeholder="010-1234-5678" required>
                                    </div>
                                </div>
                            </div>
                            
                            <%
                                // 세션에서 사용자 정보 확인
                                HttpSession userSession = request.getSession();
                                com.koreanair.model.dto.User user = (com.koreanair.model.dto.User) userSession.getAttribute("user");
                                
                                if (user == null) {
                                    // 비회원인 경우 비밀번호 등록 필드 표시
                            %>
                            <!-- 비회원 비밀번호 등록 -->
                            <div class="guest-password-section">
                                <h4 class="password-section-title">
                                    <i class="fas fa-lock"></i>
                                    비회원 예약 비밀번호
                                </h4>
                                <p class="password-section-desc">예약 조회 시 사용할 비밀번호를 설정해주세요</p>
                                
                                <div class="form-grid">
                                    <div class="form-group">
                                        <label for="guestPassword" class="required">비밀번호</label>
                                        <input type="password" id="guestPassword" name="guestPassword" 
                                               placeholder="영문, 숫자 조합 8자 이상" 
                                               pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{8,}$" 
                                               minlength="8" required>
                                        <small class="form-help">영문과 숫자를 포함하여 8자 이상 입력해주세요</small>
                                    </div>

                                    <div class="form-group">
                                        <label for="guestPasswordConfirm" class="required">비밀번호 확인</label>
                                        <input type="password" id="guestPasswordConfirm" name="guestPasswordConfirm" 
                                               placeholder="비밀번호를 다시 입력해주세요" required>
                                        <small class="form-help password-match-message"></small>
                                    </div>
                                </div>
                                
                                <div class="password-actions">
                                    <button type="button" id="confirmPasswordBtn" class="password-confirm-btn">
                                        <i class="fas fa-check"></i>
                                        비밀번호 확인
                                    </button>
                                </div>
                                
                                <div class="password-notice-inline">
                                    <div class="notice-item">
                                        <i class="fas fa-info-circle"></i>
                                        <span>설정하신 비밀번호는 예약 조회 및 변경 시 사용됩니다</span>
                                    </div>
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                    </form>
                </section>

                <!-- 약관 동의 섹션 (별도 분리) -->
                <section class="terms-info">
                    <div class="section-header">
                        <h2>약관 동의</h2>
                    </div>

                    <form id="termsForm" class="terms-form">
                        <!-- 약관 동의 -->
                        <div class="terms-section">
                            <div class="terms-list">
                                <label class="checkbox-option">
                                    <input type="checkbox" name="agreeAll" id="agreeAll">
                                    <span class="checkmark"></span>
                                    전체 동의
                                </label>
                                
                                <label class="checkbox-option required-term">
                                    <input type="checkbox" name="agreeTerms" required>
                                    <span class="checkmark"></span>
                                    <span class="required">[필수]</span> 항공 운송 약관 동의
                                    <a href="#" class="view-terms">보기</a>
                                </label>
                                
                                <label class="checkbox-option required-term">
                                    <input type="checkbox" name="agreePrivacy" required>
                                    <span class="checkmark"></span>
                                    <span class="required">[필수]</span> 개인정보 처리방침 동의
                                    <a href="#" class="view-terms">보기</a>
                                </label>
                                
                                <label class="checkbox-option">
                                    <input type="checkbox" name="agreeMarketing">
                                    <span class="checkmark"></span>
                                    [선택] 마케팅 정보 수신 동의
                                    <a href="#" class="view-terms">보기</a>
                                </label>
                            </div>
                        </div>
                    </form>
                </section>


            </div>

            <!-- 우측 요금 요약 -->
            <div class="content-right">
                <%
                    // 총액 계산
                    String totalPriceParam = request.getParameter("totalPrice");
                    String passengerCountParam = request.getParameter("passengerCount");
                    String individualPriceParam = request.getParameter("individualPrice");
                    int totalPrice = 163200; // 기본값
                    
                    // 디버깅 로그
                    System.out.println("=== booking.jsp 가격 정보 ===");
                    System.out.println("totalPrice 파라미터: " + totalPriceParam);
                    System.out.println("passengerCount 파라미터: " + passengerCountParam);
                    System.out.println("individualPrice 파라미터: " + individualPriceParam);
                    System.out.println("URL 쿼리 스트링: " + request.getQueryString());
                    
                    if (totalPriceParam != null && !totalPriceParam.isEmpty()) {
                        try {
                            totalPrice = Integer.parseInt(totalPriceParam);
                            System.out.println("✅ totalPrice 파싱 성공: " + totalPrice);
                        } catch (NumberFormatException e) {
                            totalPrice = 163200; // 파싱 오류 시 기본값
                            System.out.println("❌ totalPrice 파싱 오류, 기본값 사용: " + totalPrice);
                        }
                    } else {
                        System.out.println("⚠️ totalPrice 파라미터가 없음, 기본값 사용: " + totalPrice);
                    }
                    
                    // 운임은 총액의 80%, 유류할증료 10%, 세금 등 10%로 계산
                    int baseFare = (int)(totalPrice * 0.8);
                    int fuelSurcharge = (int)(totalPrice * 0.1);
                    int taxesAndFees = totalPrice - baseFare - fuelSurcharge;
                    
                    // 천 단위 콤마 포맷팅을 위한 함수
                    java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
                %>
                
                <div class="fare-summary">
                    <h3>항공 운송료</h3>
                    
                    <div class="fare-details">
                        <div class="fare-item">
                            <span>운임</span>
                            <span><%=df.format(baseFare)%> 원</span>
                        </div>
                        <div class="fare-item">
                            <span>유류할증료</span>
                            <span><%=df.format(fuelSurcharge)%> 원</span>
                        </div>
                        <div class="fare-item">
                            <span>세금, 수수료 및 기타 요금</span>
                            <span><%=df.format(taxesAndFees)%> 원</span>
                        </div>
                    </div>
                    
                    <div class="fare-total">
                        <div class="total-amount">
                            <span>총액</span>
                            <span class="amount"><%=df.format(totalPrice)%> 원</span>
                        </div>
                        <div class="tax-note">
                            <i class="fas fa-info-circle"></i>
                            변경 및 환불 규정
                        </div>
                    </div>
                </div>

                <!-- 최종 결제 금액 -->
                <div class="payment-summary">
                    <div class="final-amount">
                        <span>최종 결제 금액</span>
                        <span class="amount"><%=df.format(totalPrice)%> 원</span>
                    </div>
                    <button type="button" class="payment-btn" onclick="processPayment()">
                        결제하기
                    </button>
                    
                    <!-- 숨겨진 필드로 예약 정보 저장 -->
                    <input type="hidden" id="bookingTripType" value="<%=tripType != null ? tripType : "oneway"%>">
                    <input type="hidden" id="bookingFlightId" value="<%=flightId != null ? flightId : ""%>">
                    <input type="hidden" id="bookingOutboundFlightId" value="<%=outboundFlightId != null ? outboundFlightId : ""%>">
                    <input type="hidden" id="bookingReturnFlightId" value="<%=returnFlightId != null ? returnFlightId : ""%>">
                    <input type="hidden" id="bookingTotalPrice" value="<%=totalPrice%>">
                    <input type="hidden" id="bookingDeparture" value="<%=departure != null ? departure : ""%>">
                    <input type="hidden" id="bookingArrival" value="<%=arrival != null ? arrival : ""%>">
                </div>
            </div>
        </div>
    </main>

    <script>
        // booking.jsp에서 받은 가격 정보를 JavaScript로 전달
        window.bookingInfo = {
            totalPrice: <%= totalPrice %>,
            totalPriceParam: "<%= totalPriceParam != null ? totalPriceParam : "null" %>",
            passengerCountParam: "<%= passengerCountParam != null ? passengerCountParam : "null" %>",
            individualPriceParam: "<%= individualPriceParam != null ? individualPriceParam : "null" %>"
        };
        
        // 승객 정보를 JavaScript로 전달
        window.passengerInfo = {
            adultCount: <%= adultCount %>,
            childCount: <%= childCount %>,
            infantCount: <%= infantCount %>,
            totalPassengers: <%= totalPassengers %>,
            passengersParam: "<%= passengersParam != null ? passengersParam : "null" %>"
        };
        
        console.log("💰 === booking.jsp 가격 정보 ===");
        console.log("최종 총액:", window.bookingInfo.totalPrice.toLocaleString('ko-KR'), '원');
        console.log("totalPrice 파라미터:", window.bookingInfo.totalPriceParam);
        console.log("passengerCount 파라미터:", window.bookingInfo.passengerCountParam);
        console.log("individualPrice 파라미터:", window.bookingInfo.individualPriceParam);
        console.log("URL:", window.location.href);
        
        console.log("👥 === 승객 정보 ===");
        console.log("성인:", window.passengerInfo.adultCount, "명");
        console.log("소아:", window.passengerInfo.childCount, "명");
        console.log("유아:", window.passengerInfo.infantCount, "명");
        console.log("총 승객:", window.passengerInfo.totalPassengers, "명");
        console.log("passengers 파라미터:", window.passengerInfo.passengersParam);
    </script>
    <script src="${pageContext.request.contextPath}/js/booking.js"></script>
</body>
</html> 