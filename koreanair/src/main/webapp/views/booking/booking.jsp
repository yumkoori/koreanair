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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap" rel="stylesheet">
</head>
<body>
    <header>
        <div class="header-container">
            <div class="logo">
                <i class="fas fa-plane"></i>
                <span>KOREAN AIR</span>
                <i class="fas fa-globe"></i>
            </div>
            <nav class="main-nav">
                <a href="#" class="nav-item">예약</a>
                <a href="#" class="nav-item">여행 준비</a>
                <a href="#" class="nav-item">스카이패스</a>
            </nav>
            <div class="header-right">
                <div class="search-box">
                    <input type="text" placeholder="궁금한 것을 검색해 보세요!">
                    <i class="fas fa-search"></i>
                </div>
                <button class="login-btn">로그인</button>
            </div>
        </div>
    </header>

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

                    <!-- 승객 정보 입력 폼 (분리됨) -->
                    <form id="passengerInfoForm" class="passenger-info-form">
                        <%
                            // 승객 정보 파싱
                            String passengersParam = request.getParameter("passengers");
                            String passengerDisplay = passengersParam != null ? passengersParam : "성인 1명";
                        %>
                        
                        <!-- 승객 정보 카드 -->
                        <div class="passenger-card" id="passengerCard1">
                            <!-- 승객 헤더 (클릭 시 토글) -->
                            <div class="passenger-card-header" onclick="togglePassengerCard('passengerCard1')">
                                <div class="passenger-title">
                                    <h3>성인 1</h3>
                                    <span class="passenger-badge">성인</span>
                                </div>
                                <div class="passenger-summary" id="passengerSummary1" style="display: none;">
                                    <span class="summary-text">김 또는 KIM / 대한 또는 DAEHAN</span>
                                </div>
                                <i class="fas fa-chevron-down toggle-icon" id="toggleIcon1"></i>
                            </div>
                            
                            <!-- 승객 폼 내용 (접힐 수 있는 부분) -->
                            <div class="passenger-card-content" id="passengerContent1">
                                <div class="passenger-form-grid">
                                    <!-- 국적 -->
                                    <div class="form-group full-width">
                                        <label for="nationality1" class="required">국적</label>
                                        <select id="nationality1" name="passengers[0].nationality" required>
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
                                        <label for="lastName1" class="required">승객 성</label>
                                        <input type="text" id="lastName1" name="passengers[0].lastName" 
                                               placeholder="예) 김 또는 KIM" required>
                                    </div>

                                    <!-- 승객 이름 -->
                                    <div class="form-group">
                                        <label for="firstName1" class="required">승객 이름</label>
                                        <input type="text" id="firstName1" name="passengers[0].firstName" 
                                               placeholder="예) 대한 또는 DAEHAN" required>
                                    </div>

                                    <!-- 성별 -->
                                    <div class="form-group">
                                        <label for="gender1" class="required">성별</label>
                                        <select id="gender1" name="passengers[0].gender" required>
                                            <option value="">선택</option>
                                            <option value="M">남성</option>
                                            <option value="F">여성</option>
                                        </select>
                                    </div>

                                    <!-- 생년월일 -->
                                    <div class="form-group">
                                        <label for="birthDate1" class="required">생년월일(YYYY.MM.DD.)</label>
                                        <input type="text" id="birthDate1" name="passengers[0].birthDate" 
                                               placeholder="YYYY.MM.DD" pattern="\d{4}\.\d{2}\.\d{2}" required>
                                    </div>

                                    <!-- 직업 항공사 -->
                                    <div class="form-group">
                                        <label for="jobAirline1">직업 항공사</label>
                                        <select id="jobAirline1" name="passengers[0].jobAirline">
                                            <option value="">선택하지 않음</option>
                                            <option value="KE">대한항공</option>
                                            <option value="OZ">아시아나항공</option>
                                            <option value="7C">제주항공</option>
                                            <option value="OTHER">기타</option>
                                        </select>
                                    </div>

                                    <!-- 회원번호 -->
                                    <div class="form-group">
                                        <label for="memberNumber1">회원번호 <i class="fas fa-question-circle help-icon" title="항공사 회원번호를 입력하여 주십시오"></i></label>
                                        <input type="text" id="memberNumber1" name="passengers[0].memberNumber" 
                                               placeholder="항공사 회원번호를 입력하여 주십시오">
                                    </div>
                                </div>

                                <!-- 가는 여정의 개인 할인 -->
                                <div class="discount-section">
                                    <h4>가는 여정의 개인 할인</h4>
                                    <div class="discount-grid">
                                        <div class="form-group">
                                            <label for="discountType1">할인</label>
                                            <select id="discountType1" name="passengers[0].discountType">
                                                <option value="">선택</option>
                                                <option value="student">학생</option>
                                                <option value="senior">경로</option>
                                                <option value="disabled">장애인</option>
                                            </select>
                                        </div>

                                        <div class="form-group">
                                            <label for="returnDiscountType1">오는 여정의 개인 할인</label>
                                            <select id="returnDiscountType1" name="passengers[0].returnDiscountType">
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
                                    <button type="button" class="passenger-save-btn" onclick="savePassengerInfo()">
                                        저장
                                    </button>
                                </div>
                            </div>
                        </div>
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
        
        console.log("💰 === booking.jsp 가격 정보 ===");
        console.log("최종 총액:", window.bookingInfo.totalPrice.toLocaleString('ko-KR'), '원');
        console.log("totalPrice 파라미터:", window.bookingInfo.totalPriceParam);
        console.log("passengerCount 파라미터:", window.bookingInfo.passengerCountParam);
        console.log("individualPrice 파라미터:", window.bookingInfo.individualPriceParam);
        console.log("URL:", window.location.href);
    </script>
    <script src="${pageContext.request.contextPath}/js/booking.js"></script>
</body>
</html> 