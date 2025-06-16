<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>항공권 검색 결과 - 항공사 웹사이트</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/search.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap"
	rel="stylesheet">
</head>
<body>
	<header>
		<div class="header-top">
			<div class="container">
				<div class="logo">
					<a href="index.jsp"> <svg width="150" height="40"
							viewBox="0 0 200 50">
                        <path
								d="M20,10 C30,4 40,10 40,20 C40,30 30,36 20,30 C10,36 0,30 0,20 C0,10 10,4 20,10 Z"
								fill="#0064de" />
                        <path
								d="M50,15 L180,15 C185,15 190,20 190,25 C190,30 185,35 180,35 L50,35"
								stroke="#0064de" stroke-width="5" fill="none" />
                        <text x="60" y="28" font-family="Arial"
								font-size="15" font-weight="bold" fill="#0064de">AIRLINE</text>
                    </svg>
					</a>
				</div>
				<nav class="top-nav">
					<ul>
						<li><a href="#">로그인</a></li>
						<li><a href="#">회원가입</a></li>
						<li><a href="#">마이페이지</a></li>
						<li class="language-selector"><select>
								<option value="ko">한국어</option>
								<option value="en">English</option>
								<option value="ja">日本語</option>
								<option value="zh">中文</option>
						</select></li>
					</ul>
				</nav>
			</div>
		</div>
		<div class="header-main">
			<div class="container">
				<nav class="main-nav">
					<ul>
						<li class="dropdown"><a href="#">예매</a>
							<div class="dropdown-content">
								<a href="#">항공권 예매</a> <a href="#">예매 조회</a> <a href="#">체크인</a>
								<a href="#">운임 안내</a>
							</div></li>
						<li class="dropdown"><a href="#">여행 정보</a>
							<div class="dropdown-content">
								<a href="#">취항지 안내</a> <a href="#">여행 상품</a> <a href="#">여행
									가이드</a> <a href="#">비자 정보</a>
							</div></li>
						<li class="dropdown"><a href="#">서비스</a>
							<div class="dropdown-content">
								<a href="#">기내 서비스</a> <a href="#">공항 서비스</a> <a href="#">특별
									서비스</a> <a href="#">수하물 안내</a>
							</div></li>
						<li class="dropdown"><a href="#">마일리지</a>
							<div class="dropdown-content">
								<a href="#">마일리지 적립</a> <a href="#">마일리지 사용</a> <a href="#">제휴
									프로그램</a> <a href="#">마일리지 몰</a>
							</div></li>
						<li class="dropdown"><a href="#">항공사 소개</a>
							<div class="dropdown-content">
								<a href="#">회사 소개</a> <a href="#">뉴스룸</a> <a href="#">ESG 경영</a>
								<a href="#">채용 정보</a>
							</div></li>
					</ul>
				</nav>
				<div class="search">
					<input type="text" placeholder="검색어를 입력하세요">
					<button>
						<i class="fas fa-search"></i>
					</button>
				</div>
			</div>
		</div>
	</header>

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
							dateRangeDisplay = formattedDepartureDate + " ~ " + formattedReturnDate;
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
					<%
						String passengersParam = request.getParameter("passengers");
						String displayPassengers = "성인 2명"; // 기본값
						if (passengersParam != null && !passengersParam.isEmpty()) {
							// request.getParameter()는 자동으로 URL 디코딩을 수행함
							displayPassengers = passengersParam;
							
							// 혹시 문자가 깨져있다면 정리
							displayPassengers = displayPassengers.replaceAll("\\s+", " ").trim();
						}
					%>
					<span><%= displayPassengers %></span>
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
				<h2>항공권 검색 결과</h2>
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
							<div class="fare-column economy">
								<div class="fare-type">일반석</div>
								<c:forEach var="seat" items="${flightSeat[flight]}">
									<c:if test="${seat.className == '일반석'}">
										<c:choose>
											<c:when test="${seat.availableSeatCount > 0}">
												<div class="fare-price">
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
							</div>

							<!-- 프레스티지석 -->
							<div class="fare-column prestige">
								<div class="fare-type">프레스티지석</div>
								<c:forEach var="seat" items="${flightSeat[flight]}">
									<c:if test="${seat.className == '프레스티지석'}">
										<c:choose>
											<c:when test="${seat.availableSeatCount > 0}">
												<div class="fare-price">
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
							</div>


							<!-- 일등석 -->
							<div class="fare-column first">
								<div class="fare-type">일등석</div>
								<c:forEach var="seat" items="${flightSeat[flight]}">
									<c:if test="${seat.className == '일등석'}">
										<c:choose>
											<c:when test="${seat.availableSeatCount > 0}">
												<div class="fare-price">
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
					<button class="next-btn">다음 여정</button>
				</div>
			</div>
		</div>
	</div>

	<footer>
		<div class="container">
			<div class="footer-content">
				<div class="footer-section">
					<h3>고객 서비스</h3>
					<ul>
						<li><a href="#">고객센터</a></li>
						<li><a href="#">자주 묻는 질문</a></li>
						<li><a href="#">문의하기</a></li>
						<li><a href="#">예약 변경/취소</a></li>
					</ul>
				</div>
				<div class="footer-section">
					<h3>회사 정보</h3>
					<ul>
						<li><a href="#">회사 소개</a></li>
						<li><a href="#">채용 정보</a></li>
						<li><a href="#">투자 정보</a></li>
						<li><a href="#">뉴스룸</a></li>
					</ul>
				</div>
				<div class="footer-section">
					<h3>법적 고지</h3>
					<ul>
						<li><a href="#">이용약관</a></li>
						<li><a href="#">개인정보처리방침</a></li>
						<li><a href="#">운송약관</a></li>
						<li><a href="#">법적 고지문</a></li>
					</ul>
				</div>
				<div class="footer-section">
					<h3>소셜 미디어</h3>
					<div class="social-links">
						<a href="#"><i class="fab fa-facebook"></i></a> <a href="#"><i
							class="fab fa-twitter"></i></a> <a href="#"><i
							class="fab fa-instagram"></i></a> <a href="#"><i
							class="fab fa-youtube"></i></a>
					</div>
				</div>
			</div>
			<div class="footer-bottom">
				<p>&copy; 2024 항공사. All rights reserved.</p>
			</div>
		</div>
	</footer>

	<script src="${pageContext.request.contextPath}/js/search.js"></script>
</body>
</html>
