<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, user-scalable=yes">
<meta name="format-detection" content="telephone=no">
<title>항공권 검색 결과 - 항공사 웹사이트</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/search.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap" rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap"
	rel="stylesheet">
</head>
<body class="airline-main-body">

<script>
    window.contextPath = "${pageContext.request.contextPath}";
    console.log("contextPath:", window.contextPath);
</script>

    <jsp:include page="/views/common/header.jsp" />

	<div class="search-condition-bar">
		<div class="container">
			<div class="search-conditions">
				<div class="location-group">
					<div class="location-selector">
						<span class="location" data-type="departure">${param.departure != null ? param.departure : '인천(ICN)'}</span>
						<div class="location-dropdown">
							<div class="search-box">
								<input type="text" placeholder="도시 또는 공항 검색"> <i
									class="fas fa-search"></i>
							</div>
							<div class="recent-searches">
								<h4>최근 검색</h4>
								<ul>
									<li data-code="ICN">인천국제공항 (ICN)</li>
									<li data-code="GMP">김포국제공항 (GMP)</li>
									<li data-code="PUS">김해국제공항 (PUS)</li>
								</ul>
							</div>
							<div class="popular-airports">
								<h4>주요 도시</h4>
								<ul>
									<li data-code="ICN">인천국제공항 (ICN)</li>
									<li data-code="NRT">나리타국제공항 (NRT)</li>
									<li data-code="HND">하네다국제공항 (HND)</li>
									<li data-code="PEK">베이징수도국제공항 (PEK)</li>
									<li data-code="HKG">홍콩국제공항 (HKG)</li>
								</ul>
							</div>
						</div>
					</div>
					<button class="swap-locations">
						<i class="fas fa-exchange-alt"></i>
					</button>
					<div class="location-selector">
						<span class="location" data-type="arrival">${param.arrival != null ? param.arrival : '도쿄(NRT)'}</span>
						<div class="location-dropdown">
							<div class="search-box">
								<input type="text" placeholder="도시 또는 공항 검색"> <i
									class="fas fa-search"></i>
							</div>
							<div class="recent-searches">
								<h4>최근 검색</h4>
								<ul>
									<li data-code="NRT">나리타국제공항 (NRT)</li>
									<li data-code="HND">하네다국제공항 (HND)</li>
									<li data-code="KIX">간사이국제공항 (KIX)</li>
								</ul>
							</div>
							<div class="popular-airports">
								<h4>주요 도시</h4>
								<ul>
									<li data-code="NRT">나리타국제공항 (NRT)</li>
									<li data-code="HND">하네다국제공항 (HND)</li>
									<li data-code="PEK">베이징수도국제공항 (PEK)</li>
									<li data-code="HKG">홍콩국제공항 (HKG)</li>
									<li data-code="SGN">떤선녓국제공항 (SGN)</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="divider">|</div>
				<div class="date-range">
					<%
						String departureDateDisplay = request.getParameter("departureDate");
						String returnDateDisplay = request.getParameter("returnDate");
						String dateRangeDisplay = "2024.03.20 ~ 2024.03.27"; // 기본값
						
						if (departureDateDisplay != null && returnDateDisplay != null) {
							// 2025-07-15 형식을 2025.07.15 형식으로 변환
							String formattedDepartureDate = departureDateDisplay.replace("-", ".");
							String formattedReturnDate = returnDateDisplay.replace("-", ".");
							dateRangeDisplay = formattedDepartureDate + " ! " + formattedReturnDate;
						}
					%>
					<i class="far fa-calendar-alt"></i> <span><%= dateRangeDisplay %></span>
					<div class="calendar-dropdown">
						<div class="calendar-header">
							<div class="month-selector">
								<button class="prev-month">
									<i class="fas fa-chevron-left"></i>
								</button>
								<span class="current-month">2024년 3월</span>
								<button class="next-month">
									<i class="fas fa-chevron-right"></i>
								</button>
							</div>
						</div>
						<div class="calendar-body">
							<div class="weekdays">
								<div>일</div>
								<div>월</div>
								<div>화</div>
								<div>수</div>
								<div>목</div>
								<div>금</div>
								<div>토</div>
							</div>
							<div class="days"></div>
						</div>
						<div class="calendar-footer">
							<button class="apply-date">적용</button>
							<button class="cancel-date">취소</button>
						</div>
					</div>
				</div>
				<div class="divider">|</div>
				<div class="passengers">
					<span></span>
					<div class="passengers-dropdown">
						<div class="passenger-type">
							<div class="passenger-label">
								<span>성인</span> <small>만 12세 이상</small>
							</div>
							<div class="passenger-count">
								<button class="count-btn decrease">
									<i class="fas fa-minus"></i>
								</button>
								<span class="count adult-count">2</span>
								<button class="count-btn increase">
									<i class="fas fa-plus"></i>
								</button>
							</div>
						</div>
						<div class="passenger-type">
							<div class="passenger-label">
								<span>소아</span> <small>만 2-11세</small>
							</div>
							<div class="passenger-count">
								<button class="count-btn decrease">
									<i class="fas fa-minus"></i>
								</button>
								<span class="count child-count">0</span>
								<button class="count-btn increase">
									<i class="fas fa-plus"></i>
								</button>
							</div>
						</div>
						<div class="passenger-type">
							<div class="passenger-label">
								<span>유아</span> <small>만 2세 미만</small>
							</div>
							<div class="passenger-count">
								<button class="count-btn decrease">
									<i class="fas fa-minus"></i>
								</button>
								<span class="count infant-count">0</span>
								<button class="count-btn increase">
									<i class="fas fa-plus"></i>
								</button>
							</div>
						</div>
						<div class="passengers-footer">
							<button class="apply-passengers">적용</button>
						</div>
					</div>
				</div>
				<div class="divider">|</div>
				<div class="seat-type">
					<span>${param.seatType != null ? param.seatType : '일반석'}</span>
					<div class="seat-type-dropdown">
						<h4>좌석 등급 선택</h4>
						<div class="seat-options">
							<div class="seat-option" data-type="economy">
								<span>일반석</span> <i class="fas fa-check"></i>
							</div>
							<div class="seat-option" data-type="prestige">
								<span>프레스티지석</span> <i class="fas fa-check"></i>
							</div>
							<div class="seat-option" data-type="first">
								<span>일등석</span> <i class="fas fa-check"></i>
							</div>
						</div>
					</div>
				</div>
				<button class="search-again-btn">
					<i class="fas fa-search"></i> 항공편 검색
				</button>
			</div>
		</div>
	</div>

	<div class="date-price-bar">
		<div class="container">
			<div class="date-price-list">
				<%@ page import="java.time.LocalDate, java.time.format.DateTimeFormatter, java.time.DayOfWeek" %>
				<%@ page import="java.util.Locale, java.util.Map" %>
				<%
					String departureDateParam = request.getParameter("departureDate");
					String originalDepartureDateParam = request.getParameter("originalDepartureDate");
					
					// 원래 기준 날짜 설정 (처음 요청된 날짜)
					LocalDate originalBaseDate;
					if (originalDepartureDateParam != null && !originalDepartureDateParam.isEmpty()) {
						try {
							originalBaseDate = LocalDate.parse(originalDepartureDateParam);
						} catch (Exception e) {
							// originalDepartureDate가 없거나 잘못된 경우, departureDate를 원래 날짜로 사용
							originalBaseDate = departureDateParam != null ? LocalDate.parse(departureDateParam) : LocalDate.now();
						}
					} else {
						// 첫 요청인 경우 departureDate를 원래 날짜로 설정
						originalBaseDate = departureDateParam != null ? LocalDate.parse(departureDateParam) : LocalDate.now();
					}
					
					// 현재 선택된 날짜
					LocalDate currentSelectedDate;
					if (departureDateParam != null && !departureDateParam.isEmpty()) {
						try {
							currentSelectedDate = LocalDate.parse(departureDateParam);
						} catch (Exception e) {
							currentSelectedDate = originalBaseDate;
						}
					} else {
						currentSelectedDate = originalBaseDate;
					}
					
					// 항상 원래 기준 날짜를 중심으로 앞뒤 3일씩 총 7일 생성
					LocalDate[] dates = new LocalDate[7];
					for (int i = 0; i < 7; i++) {
						dates[i] = originalBaseDate.minusDays(3).plusDays(i);
					}
					
					// 요일 이름 배열 (한국어)
					String[] dayNames = {"일", "월", "화", "수", "목", "금", "토"};
					
					// 각 날짜별 최저가 계산
					String[] prices = new String[7];
					
					// weekLowPrices 맵에서 각 날짜별 최저가 가져오기
					Map<String, Integer> weekLowPrices = (Map<String, Integer>) request.getAttribute("weekLowPrices");
					
					if (weekLowPrices != null) {
						// 각 날짜에 해당하는 가격 매핑
						for (int i = 0; i < dates.length; i++) {
							LocalDate currentDate = dates[i];
							String dateKey = currentDate.toString(); // 2025-07-15 형식
							
							Integer priceValue = weekLowPrices.get(dateKey);
							if (priceValue != null && priceValue > 0) {
								prices[i] = String.format("%,d원", priceValue);
							} else {
								prices[i] = "가격정보없음";
							}
						}
					} else {
						// weekLowPrices 정보가 없는 경우 기본값
						for (int i = 0; i < 7; i++) {
							prices[i] = "가격정보없음";
						}
					}
					
					for (int i = 0; i < dates.length; i++) {
						LocalDate currentDate = dates[i];
						int dayOfMonth = currentDate.getDayOfMonth();
						String dayOfWeek = dayNames[currentDate.getDayOfWeek().getValue() % 7];
						// 현재 선택된 날짜와 비교해서 active 클래스 적용
						boolean isActive = currentDate.equals(currentSelectedDate);
				%>
				<div class="date-price-item <%= isActive ? "active" : "" %>">
					<div class="date-day"><%= dayOfMonth %></div>
					<div class="date-weekday"><%= dayOfWeek %></div>
					<div class="price-amount"><%= prices[i] %></div>
				</div>
				<%
					}
				%>
			</div>
		</div>
	</div>



	<div class="filter-options-bar">
		<div class="container">
			<div class="filter-buttons">
				<div class="filter-dropdown sort-dropdown">
					<button class="filter-btn sort-btn">
						<i class="fas fa-sort"></i> <span class="selected-option">추천순
							정렬</span>
					</button>
					<div class="dropdown-content sort-options">
						<h4>정렬 기준 선택</h4>
						<div class="radio-options">
							<label class="radio-option"> <input type="radio"
								name="sort-option" value="recommended" checked> <span
								class="radio-label">추천순</span>
							</label> <label class="radio-option"> <input type="radio"
								name="sort-option" value="departure-time"> <span
								class="radio-label">출발 시간 순</span>
							</label> <label class="radio-option"> <input type="radio"
								name="sort-option" value="arrival-time"> <span
								class="radio-label">도착 시간 순</span>
							</label> <label class="radio-option"> <input type="radio"
								name="sort-option" value="duration"> <span
								class="radio-label">여행 시간 순</span>
							</label> <label class="radio-option"> <input type="radio"
								name="sort-option" value="price"> <span
								class="radio-label">최저 요금 순</span>
							</label>
						</div>
					</div>
				</div>

				<div class="filter-dropdown stopover-dropdown">
					<button class="filter-btn stopover-btn">
						<i class="fas fa-plane"></i> <span class="selected-option">직항
							및 경유</span>
					</button>
					<div class="dropdown-content stopover-options">
						<h4>경유 선택</h4>
						<div class="radio-options">
							<label class="radio-option"> <input type="radio"
								name="stopover-option" value="all" checked> <span
								class="radio-label">전체</span>
							</label> <label class="radio-option"> <input type="radio"
								name="stopover-option" value="direct"> <span
								class="radio-label">직항</span>
							</label> <label class="radio-option"> <input type="radio"
								name="stopover-option" value="stopover"> <span
								class="radio-label">경유</span>
							</label>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<section class="search-results">
		<div class="container">
			<div class="search-summary">
				<%
					String leg = request.getParameter("leg");
					String tripType = request.getParameter("tripType");
					boolean isReturnLeg = "return".equals(leg);
					boolean isRoundTrip = "round".equals(tripType);
				%>
				
				<% if (isRoundTrip && isReturnLeg) { %>
					<h2>복항편 선택 - 항공권 검색 결과</h2>
					<div id="outbound-selection-info" class="outbound-info" style="background-color: #f0f8ff; padding: 10px; margin-bottom: 15px; border-radius: 5px; border-left: 4px solid #0064de;">
						<div style="font-weight: bold; color: #0064de; margin-bottom: 5px;">선택된 가는 편</div>
						<div id="outbound-details" style="font-size: 14px; color: #666;">
							<!-- JavaScript로 채워질 내용 -->
						</div>
					</div>
					<h3 style="color: #0064de; margin-bottom: 10px;">
						<i class="fas fa-plane" style="transform: rotate(180deg); margin-right: 8px;"></i>
						복항편을 선택해주세요
					</h3>
				<% } else { %>
					<h2>항공권 검색 결과</h2>
					<% if (isRoundTrip) { %>
						<div style="color: #0064de; font-weight: 500; margin-bottom: 10px;">
							<i class="fas fa-plane" style="margin-right: 8px;"></i>
							가는 편을 선택해주세요
						</div>
					<% } %>
				<% } %>
				
				<div class="route-info">
					<span class="departure">${param.departure != null ? param.departure : '서울(ICN)'}</span>
					<i class="fas fa-arrow-right"></i> <span class="arrival">${param.arrival != null ? param.arrival : '도쿄(NRT)'}</span>
					<span class="date">${param.departureDate != null ? param.departureDate : '2024년 3월 20일'}</span>
				</div>
				<div class="filter-options">
					<select class="sort-by">
						<option value="price">가격순</option>
						<option value="duration">소요시간순</option>
						<option value="departure">출발시간순</option>
					</select>
					<button class="filter-btn">
						<i class="fas fa-filter"></i> 필터
					</button>
				</div>
			</div>

			<div class="flights-list">
				<c:forEach var="flight" items="${flightList}">
					<div class="flight-card new-layout">
						<!-- flight 기본 정보 -->
						<div class="flight-info-column">
							<div class="flight-times">
								<div class="departure-block">
									<div class="departure-code">${param.departure}</div>
								</div>
								<div class="flight-duration">
									<div class="duration-time">${flight.durationMinutes}분</div>

									<div class="flight-path">
										<div class="path-line"></div>
										<i class="fas fa-plane"></i>
									</div>
								</div>
								<div class="arrival-block">
									<div class="arrival-code">${param.arrival}</div>
								</div>
							</div>
							<div class="airline-info">
								<span class="flight-number">${flight.flightId}</span>
								<button class="details-btn">상세 보기</button>
							</div>
						</div>

						<!-- 좌석 수 초기화 -->
						<c:set var="economySeats" value="0" />
						<c:set var="prestigeSeats" value="0" />
						<c:set var="firstSeats" value="0" />

						<!-- 좌석 리스트 탐색 -->
						<c:forEach var="seat" items="${flightSeat[flight]}">
							<c:if test="${seat.className == '일반석'}">
								<c:set var="economySeats" value="${seat.availableSeatCount}" />
							</c:if>
							<c:if test="${seat.className == '프레스티지석'}">
								<c:set var="prestigeSeats" value="${seat.availableSeatCount}" />
							</c:if>
							<c:if test="${seat.className == '일등석'}">
								<c:set var="firstSeats" value="${seat.availableSeatCount}" />
							</c:if>
						</c:forEach>

						<!-- 좌석 가격 정보 -->
						<div class="fare-columns">
							<!-- 일반석 -->
							<div class="fare-column economy clickable-fare" data-fare-type="일반석" data-flight-id="${flight.flightId}">
								<div class="fare-type">일반석</div>
								<c:set var="economyFound" value="false" />
								<c:forEach var="seat" items="${flightSeat[flight]}">
									<c:if test="${seat.className == '일반석'}">
										<c:set var="economyFound" value="true" />
										<c:choose>
											<c:when test="${seat.availableSeatCount > 0}">
												<div class="fare-price" data-price="${seat.price}">
													<span class="currency">₩</span> <span class="amount">${seat.price}</span>
												</div>
											</c:when>
											<c:otherwise>
												<div class="fare-price no-available">매진</div>
											</c:otherwise>
										</c:choose>
										<div
											class="fare-status ${seat.availableSeatCount > 0 ? 'available' : 'unavailable'}">
											${seat.availableSeatCount}석</div>
									</c:if>
								</c:forEach>
								<c:if test="${!economyFound}">
									<div class="fare-price no-available">매진</div>
									<div class="fare-status unavailable">좌석 정보 없음</div>
								</c:if>
							</div>

							<!-- 프레스티지석 -->
							<div class="fare-column prestige clickable-fare" data-fare-type="프레스티지석" data-flight-id="${flight.flightId}">
								<div class="fare-type">프레스티지석</div>
								<c:set var="prestigeFound" value="false" />
								<c:forEach var="seat" items="${flightSeat[flight]}">
									<c:if test="${seat.className == '프레스티지석'}">
										<c:set var="prestigeFound" value="true" />
										<c:choose>
											<c:when test="${seat.availableSeatCount > 0}">
												<div class="fare-price" data-price="${seat.price}">
													<span class="currency">₩</span><span class="amount">${seat.price}</span>
												</div>
											</c:when>
											<c:otherwise>
												<div class="fare-price no-available">매진</div>
											</c:otherwise>
										</c:choose>
										<div
											class="fare-status ${seat.availableSeatCount > 0 ? 'available' : 'unavailable'}">
											${seat.availableSeatCount}석</div>
									</c:if>
								</c:forEach>
								<c:if test="${!prestigeFound}">
									<div class="fare-price no-available">매진</div>
									<div class="fare-status unavailable">좌석 정보 없음</div>
								</c:if>
							</div>


							<!-- 일등석 -->
							<div class="fare-column first clickable-fare" data-fare-type="일등석" data-flight-id="${flight.flightId}">
								<div class="fare-type">일등석</div>
								<c:set var="firstFound" value="false" />
								<c:forEach var="seat" items="${flightSeat[flight]}">
									<c:if test="${seat.className == '일등석'}">
										<c:set var="firstFound" value="true" />
										<c:choose>
											<c:when test="${seat.availableSeatCount > 0}">
												<div class="fare-price" data-price="${seat.price}">
													<span class="currency">₩</span><span class="amount">${seat.price}</span>
												</div>
											</c:when>
											<c:otherwise>
												<div class="fare-price no-available">매진</div>
											</c:otherwise>
										</c:choose>
										<div
											class="fare-status ${seat.availableSeatCount > 0 ? 'available' : 'unavailable'}">
											${seat.availableSeatCount}석</div>
									</c:if>
								</c:forEach>
								<c:if test="${!firstFound}">
									<div class="fare-price no-available">매진</div>
									<div class="fare-status unavailable">좌석 정보 없음</div>
								</c:if>
							</div>

						</div>
					</div>
				</c:forEach>
			</div>







		</div>
	</section>

	<!-- Fare details popup that will appear when a fare is clicked -->
	<div id="fareDetailsPopup" class="fare-details-popup">
		<div class="fare-details-content">
			<button id="closePopupBtn" class="close-popup">&times;</button>
			<div class="fare-details-header">
				<div id="fareTitle" class="fare-title"></div>
				<div id="fareLargePrice" class="fare-large-price"></div>
				<div id="fareSeats" class="fare-seats"></div>
			</div>
			<div class="fare-details-info">
				<div class="fare-flight-info">
					<span id="fareFlightNumber" class="fare-flight-number"></span>, <span
						id="fareAirline" class="fare-airline"></span>
				</div>
				<div class="fare-details-grid">
					<div class="detail-row">
						<div class="detail-label">변경 수수료</div>
						<div id="changeFee" class="detail-value change-fee"></div>
					</div>
					<div class="detail-row">
						<div class="detail-label">환불 위약금</div>
						<div id="cancelFee" class="detail-value cancel-fee"></div>
					</div>
					<div class="detail-row">
						<div class="detail-label">무료 위탁 수하물</div>
						<div id="baggageInfo" class="detail-value baggage-info"></div>
					</div>
					<div class="detail-row">
						<div class="detail-label">마일리지 좌석승급</div>
						<div id="upgradePossibility"
							class="detail-value upgrade-possibility"></div>
					</div>
					<div class="detail-row">
						<div class="detail-label">적립 마일리지</div>
						<div id="mileageAccrual" class="detail-value mileage-accrual"></div>
					</div>
				</div>
				<div class="fare-actions">
					<button id="seatsPreviewBtn" class="seats-preview-btn">
						<i class="fas fa-chair"></i> 좌석 정보 미리보기
					</button>
					<button id="selectFareBtn" class="select-fare-btn">선택하기</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Flight details popup that will appear when the "상세보기" button is clicked -->
	<div id="flightDetailsPopup" class="flight-details-popup">
		<div class="flight-details-content">
			<button id="closeFlightDetailsBtn" class="close-popup">&times;</button>
			<div class="flight-details-header">
				<h2>여정 정보</h2>
				<div class="route-summary">
					<span id="flightDetailDeparture">출발지 ICN 서울/인천</span> <span
						id="flightDetailArrival">도착지 NRT 도쿄/나리타</span>
				</div>
				<div id="flightDetailDuration" class="flight-duration-summary">총
					2시간 25분 여정</div>
			</div>
			<div class="flight-details-info">
				<div class="flight-info-row">
					<div class="flight-number-aircraft">
						<div id="flightDetailNumber" class="flight-number">항공편명
							KE5741</div>
						<div id="flightDetailAircraft" class="aircraft-type">항공기종
							B737-800</div>
						<div id="flightDetailOperator" class="operator">진에어 운항</div>
					</div>
					<div class="flight-amenities">
						<div class="amenities-title">기내 어메니티</div>
						<div class="amenities-icons">
							<i class="fas fa-wifi" title="와이파이"></i> <i class="fas fa-plug"
								title="전원"></i> <i class="fas fa-tv" title="개인 모니터"></i>
						</div>
					</div>
				</div>
				<div class="journey-details">
					<div class="journey-point departure-details">
						<h3>출발지</h3>
						<div id="flightDetailDepartureCode" class="airport-code">ICN
							서울/인천</div>
						<div id="flightDetailDepartureTime" class="time-info">출발시간
							2025년 05월 22일 (목) 07:25</div>
						<div id="flightDetailDepartureTerminal" class="terminal-info">터미널
							2</div>
					</div>
					<div class="journey-duration">
						<div class="duration-line">
							<i class="fas fa-plane"></i>
						</div>
						<div id="flightDetailJourneyTime" class="duration-time">2시간
							25분 소요</div>
					</div>
					<div class="journey-point arrival-details">
						<h3>도착지</h3>
						<div id="flightDetailArrivalCode" class="airport-code">NRT
							도쿄/나리타</div>
						<div id="flightDetailArrivalTime" class="time-info">도착시간
							2025년 05월 22일 (목) 09:50</div>
						<div id="flightDetailArrivalTerminal" class="terminal-info">터미널
							1</div>
					</div>
				</div>
				<div class="flight-details-actions">
					<button id="confirmFlightDetailsBtn" class="confirm-btn">확인</button>
				</div>
			</div>
		</div>
	</div>

	<div id="popupOverlay" class="popup-overlay"></div>

	<!-- Fixed bottom payment bar -->
	<div class="bottom-payment-bar">
		<div class="container">
			<div class="payment-content">
				<div class="total-section">
					<span class="total-label">총액</span>
					<div class="total-amount">0원</div>
				</div>
				<div class="currency-section">
					<button class="currency-btn">
						<span class="currency-code">KRW</span> <i
							class="fas fa-chevron-down"></i>
					</button>
					<div class="currency-dropdown">
						<div class="currency-option selected" data-currency="KRW">
							<span>KRW</span> <i class="fas fa-check"></i>
						</div>
						<div class="currency-option" data-currency="USD">
							<span>USD</span>
						</div>
						<div class="currency-option" data-currency="JPY">
							<span>JPY</span>
						</div>
						<div class="currency-option" data-currency="EUR">
							<span>EUR</span>
						</div>
					</div>
				</div>
				<div class="next-section">
					<% if (isRoundTrip && isReturnLeg) { %>
						<button class="next-btn">예약 완료</button>
					<% } else { %>
						<button class="next-btn">다음 여정</button>
					<% } %>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="/views/common/footer.jsp" />

	<%
		// 승객 수 정보를 JavaScript로 전달하기 위한 변수 준비
		String passengersParamForJS = request.getParameter("passengers");
		String displayPassengersForJS = "성인 2명"; // 기본값
		
		// 디버깅: 원본 파라미터 출력
		System.out.println("=== JSP 디버깅 ===");
		System.out.println("원본 passengers 파라미터: " + passengersParamForJS);
		System.out.println("URL 쿼리 스트링: " + request.getQueryString());
		
		if (passengersParamForJS != null && !passengersParamForJS.isEmpty()) {
			displayPassengersForJS = passengersParamForJS.replaceAll("\\s+", " ").trim();
			System.out.println("처리된 passengers: " + displayPassengersForJS);
		} else {
			System.out.println("passengers 파라미터가 null이거나 비어있음, 기본값 사용: " + displayPassengersForJS);
		}
		
		// 승객 수 계산
		int adultCount = 2; // 기본값
		int childCount = 0;
		int infantCount = 0;
		
		if (passengersParamForJS != null) {
			java.util.regex.Pattern adultPattern = java.util.regex.Pattern.compile("성인\\s*(\\d+)명");
			java.util.regex.Pattern childPattern = java.util.regex.Pattern.compile("소아\\s*(\\d+)명");
			java.util.regex.Pattern infantPattern = java.util.regex.Pattern.compile("유아\\s*(\\d+)명");
			
			java.util.regex.Matcher adultMatcher = adultPattern.matcher(passengersParamForJS);
			java.util.regex.Matcher childMatcher = childPattern.matcher(passengersParamForJS);
			java.util.regex.Matcher infantMatcher = infantPattern.matcher(passengersParamForJS);
			
			if (adultMatcher.find()) {
				adultCount = Integer.parseInt(adultMatcher.group(1));
			}
			if (childMatcher.find()) {
				childCount = Integer.parseInt(childMatcher.group(1));
			}
			if (infantMatcher.find()) {
				infantCount = Integer.parseInt(infantMatcher.group(1));
			}
		}
		
		int totalPassengers = adultCount + childCount; // 유아는 무료
	%>
	<script>
		window.contextPath = "${pageContext.request.contextPath}";
		console.log("contextPath:", window.contextPath);
		
		// 승객 수 정보를 JavaScript로 전달
		window.passengersInfo = "<%= displayPassengersForJS %>";
		window.passengerCount = <%= totalPassengers %>;
		window.adultCount = <%= adultCount %>;
		window.childCount = <%= childCount %>;
		window.infantCount = <%= infantCount %>;
		
		console.log("🚀 JSP에서 JavaScript로 승객 정보 전달 완료");
		console.log("📝 원본 파라미터:", "<%= passengersParamForJS != null ? passengersParamForJS : "null" %>");
		console.log("📝 표시용 문자열:", window.passengersInfo);
		console.log("👥 총 승객 수:", window.passengerCount);
		console.log("👨 성인:", window.adultCount, "👶 소아:", window.childCount, "🍼 유아:", window.infantCount);
	</script>
	<script src="${pageContext.request.contextPath}/js/search.js"></script>
	<script src="${pageContext.request.contextPath}/js/seat-selection.js"></script>
</body>
</html>
