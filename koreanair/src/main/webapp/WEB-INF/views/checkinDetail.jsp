<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>체크인 - 대한항공</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/checkinDetail.css">
<link
	href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/index.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
	<jsp:include page="/views/common/header.jsp" />

	<main class="page-container">
		<div class="page-header">
			<c:choose>
				<c:when test="${not empty reservation.assignedSeat}">
					<h1>체크인 정보 확인</h1>
					<p>고객님의 체크인 정보입니다. 예매 내역을 다시 확인하시려면 아래 버튼을 눌러주세요.</p>
				</c:when>
				<c:otherwise>
					<h1>온라인 체크인</h1>
					<p>온라인 체크인 신청 시 항공편 출발 24시간 전에 자동으로 체크인되며, 입력하신 연락처로 탑승권이
						전송됩니다.</p>
				</c:otherwise>
			</c:choose>
		</div>
		<c:if test="${not empty reservation}">
			<div class="info-card journey-card">
				<div class="info-card-body">
					<div class="flight-route">
						<span class="airport-code">${reservation.departureAirportId}</span>
						<i class="fas fa-arrow-right-long"></i> <span class="airport-code">${reservation.arrivalAirportId}</span>
					</div>
					<div class="flight-details">
						<div class="detail-item">
							<span class="label">출발</span> <span class="value"><fmt:formatDate
									value="${reservation.departureTime}"
									pattern="yyyy.MM.dd (E) HH:mm" /></span>
						</div>
						<div class="detail-item">
							<span class="label">편명</span> <span class="value">${reservation.flightId}</span>
						</div>
					</div>
				</div>
			</div>
			<div class="info-card passenger-card">
				<div class="info-card-header">탑승객</div>
				<div class="info-card-body">
					<div class="passenger-info">
						<input type="checkbox" id="passenger" name="passenger" checked
							disabled> <label for="passenger">${reservation.lastName}
							${reservation.firstName}</label>
						<c:choose>
							<c:when test="${not empty reservation.assignedSeat}">
								<span class="status-badge">좌석지정 완료
									(${reservation.assignedSeat})</span>
							</c:when>
							<c:otherwise>
								<span class="status-badge">좌석 선택 가능</span>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>

			<div class="form-actions">
				<c:choose>
					<c:when test="${not empty reservation.assignedSeat}">
						<form
							action="${pageContext.request.contextPath}/reservationDetail"
							method="GET">
							<input type="hidden" name="bookingId"
								value="${reservation.bookingId}">
							<button type="button" class="btn btn-secondary"
								onclick="history.back()">이전</button>
							<button type="submit" class="btn btn-primary">예매내역조회</button>
						</form>
					</c:when>
					<c:otherwise>
						<form action="${pageContext.request.contextPath}/checkinApply.do"
							method="GET">
							<input type="hidden" name="bookingId"
								value="${reservation.bookingId}">
							<button type="button" class="btn btn-secondary"
								onclick="history.back()">취소</button>
							<button type="submit" class="btn btn-primary">다음</button>
						</form>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<c:if test="${empty reservation}">
			<p>예약 정보를 불러올 수 없습니다.</p>
		</c:if>
	</main>
	<jsp:include page="/views/common/footer.jsp" />
</body>
</html>