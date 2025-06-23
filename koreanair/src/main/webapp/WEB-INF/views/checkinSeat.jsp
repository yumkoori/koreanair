<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>좌석 배정 - 대한항공</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="<%= contextPath %>/css/checkinSeat.css">
</head>
<body>
	<div class="checkin-page-container">
		<form id="seatForm" action="<%= contextPath %>/checkinSeat.do"
			method="POST">
			<input type="hidden" name="bookingId"
				value="${reservation.bookingId}"> <input type="hidden"
				name="flightId" value="${reservation.flightId}"> <input
				type="hidden" id="seatId" name="seatId" value="">

			<div class="page-header">
				<h2>좌석 배정</h2>
				<button type="button" class="close-btn" onclick="history.back()">&times;
					닫기</button>
			</div>

			<div class="flight-info-bar">
				<span class="route">${reservation.departureAirportId} -
					${reservation.arrivalAirportId}</span> <span class="flight-details">
					<i class="fa-solid fa-plane"></i> ${reservation.flightId} <i
					class="fa-solid fa-calendar-alt"></i> <fmt:formatDate
						value="${reservation.departureTime}" pattern="MM월 dd일 (E) HH:mm" />
				</span>
			</div>

			<div class="page-content-area">
				<div class="seat-map-view">
					<div id="airplaneContainer" class="airplane"></div>
				</div>

				<div class="info-sidebar">
					<div class="info-box passenger-box">
						<h4>탑승객</h4>
						<div class="passenger-name active">
							<i class="fa fa-user"></i> ${reservation.lastName}
							${reservation.firstName}
						</div>
					</div>
					<div class="info-box selected-seat-box">
						<h4>선택된 좌석</h4>
						<div id="selectedSeatDisplay">
							<p class="placeholder">좌석을 선택하세요</p>
						</div>
					</div>
					<div class="info-box legend-box">
						<h4>좌석 안내</h4>
						<ul class="legend-list">
							<li><span class="legend-icon available"></span> 선택 가능</li>
							<li><span class="legend-icon my-seat"></span> 나의 좌석</li>
							<li><span class="legend-icon prestige"></span> 프레스티지석</li>
							<li><span class="legend-icon unavailable"></span> 선택 불가</li>
						</ul>
					</div>
				</div>
			</div>

			<div class="page-footer">
				<span class="additional-charge">추가 요금 <strong>₩0</strong></span>
				<button type="submit" class="btn-submit">완료</button>
			</div>
		</form>
	</div>

	<script src="<%= contextPath %>/js/checkinSeat.js"></script>
</body>
</html>