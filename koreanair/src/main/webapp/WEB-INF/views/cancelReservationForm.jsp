<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>항공권 환불 신청 - 대한항공</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/index.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/cancel.css">
<link
	href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

</head>
<body>


	<jsp:include page="/views/common/header.jsp" />

	<div class="cancel-page">
		<div class="container">
			<h1>항공권 환불 신청</h1>

			<form id="cancelForm"
				action="${pageContext.request.contextPath}/cancelReservation.do"
				method="post">

				<input type="hidden" name="bookingId"
					value="${reservation.bookingId}">

				<div class="section">
					<h2>여정 정보</h2>
					<div class="itinerary-info">
						<div class="flight-header">
							예약번호: <strong>${reservation.bookingId}</strong>
						</div>
						<div class="flight-path">
							<div class="airport">
								<div class="airport-code">${reservation.departureAirportId}</div>
								<div class="airport-name">${reservation.departureAirportName}</div>
								<div class="flight-schedule">
									<fmt:formatDate value="${reservation.departureTime}"
										pattern="yyyy.MM.dd (E) HH:mm" />
								</div>
							</div>
							<div class="flight-arrow">
								<i class="fa-solid fa-arrow-right-long"></i>
							</div>
							<div class="airport" style="text-align: right;">
								<div class="airport-code">${reservation.arrivalAirportId}</div>
								<div class="airport-name">${reservation.arrivalAirportName}</div>
								<div class="flight-schedule">
									<fmt:formatDate value="${reservation.arrivalTime}"
										pattern="yyyy.MM.dd (E) HH:mm" />
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="section">
					<h2>탑승객 및 환불 정보</h2>
					<div class="info-box">
						<ul class="info-table">
							<li><span class="label">탑승객</span> <span class="value">${reservation.lastName}
									${reservation.firstName}</span></li>
							<li class="penalty"><span class="label">환불 위약금 및 수수료</span>

								<span class="value"><fmt:formatNumber
										value="${refundDetail.penaltyFee}" pattern="#,##0" /> 마일</span></li>
							<li class="refund-total"><span class="label">총 환불 예정
									마일리지</span> <span class="value"><fmt:formatNumber
										value="${refundDetail.totalRefundMileage}" pattern="#,##0" />
									마일</span></li>
							<li class="refund-total"><span class="label">총 환불 예정
									금액(세금 및 유류할증료)</span> <span class="value">KRW <fmt:formatNumber
										value="${refundDetail.totalRefundAmount}" pattern="#,##0" /></span>
							</li>
						</ul>
					</div>
				</div>

				<div class="section">
					<h2>신청자 정보</h2>
					<div class="applicant-info info-box">
						<ul class="info-table">
							<li><span class="label">이름</span> <span class="value">${sessionScope.user.koreanName}</span>
							</li>
							<li><span class="label">연락처</span> <span class="value">${sessionScope.user.phone}</span>
							</li>
							<li><span class="label">이메일</span> <span class="value">${sessionScope.user.email}</span>
							</li>
						</ul>
					</div>
				</div>

				<div class="section notice-section">
					<h2>안내사항</h2>
					<ul>
						<li>환불 위약금은 1인당 편도 기준으로 부과됩니다.</li>
						<li>환불 접수 후에는 취소가 불가능하오니 신중하게 신청해 주시기 바랍니다.</li>
						<li>환불 금액은 실제 처리 시점에 따라 변동될 수 있으며, 최종 환불 내역은 이메일로 안내됩니다.</li>
					</ul>

					<div class="checkbox-group">
						<input type="checkbox" id="agreement1" name="agreement1" required>
						<label for="agreement1">환불 위약금 및 규정을 확인하였습니다. <span
							class="required">(필수)</span></label>
					</div>
					<div class="checkbox-group">
						<input type="checkbox" id="agreement2" name="agreement2" required>
						<label for="agreement2">상기 내용에 모두 동의하며 환불을 신청합니다. <span
							class="required">(필수)</span></label>
					</div>
				</div>

				<div class="form-actions">

					<button type="button" id="cancelBtn" class="btn btn-secondary">신청
						취소</button>
					<button type="button" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/views/payment/refundProcess.jsp?bookingId=${reservation.bookingId}'">
    환불 신청
</button>
				</div>
			</form>
		</div>
	</div>

	<jsp:include page="/views/common/footer.jsp" />

	<script src="${pageContext.request.contextPath}/js/cancel.js"></script>
</body>
</html>