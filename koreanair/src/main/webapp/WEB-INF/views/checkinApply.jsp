<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>온라인 체크인 - 대한항공</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/checkinApply.css">
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
			<h1>온라인 체크인</h1>
			<p>탑승권은 입력하신 연락처로 전송됩니다. 정보를 정확하게 입력해주세요.</p>
		</div>

		<form id="applyForm"
			action="${pageContext.request.contextPath}/checkinSeat.do"
			method="get">
			<input type="hidden" name="bookingId"
				value="${reservation.bookingId}"> <input type="hidden"
				name="flightId" value="${reservation.flightId}">

			<div class="form-section">
				<h2>탑승객 연락처 정보</h2>
				<div class="input-group">
					<label for="passengerName">탑승객 이름</label> <input type="text"
						id="passengerName" name="passengerName"
						value="${reservation.lastName} ${reservation.firstName}" readonly>
				</div>

				<div class="input-group-row">
					<div class="input-group country-code">
						<label for="countryCode">국가 번호</label> <input type="text"
							id="countryCode" name="countryCode" value="+82 (대한민국)" readonly>
					</div>
					<div class="input-group">
						<label for="phone">휴대폰 번호</label> <input type="tel" id="phone"
							name="phone" value="${reservation.phone}" readonly>
					</div>
				</div>

				<div class="input-group">
					<label for="email">이메일 주소</label> <input type="email" id="email"
						name="email" value="${reservation.email}" readonly>
				</div>
			</div>
			<div class="form-section agreement-section">
				<h2>규정 및 동의</h2>
				<div class="agreement-item">
					<input type="checkbox" id="agreement1" name="agreement1"> <label
						for="agreement1">위험물 안내 규정에 동의합니다.</label> <span
						class="required-text">(필수)</span>
					<button type="button" class="btn-view">보기</button>
				</div>
				<div class="agreement-item">
					<input type="checkbox" id="agreement2" name="agreement2"> <label
						for="agreement2">개인정보 수집 및 이용에 동의합니다.</label> <span
						class="required-text">(필수)</span>
					<button type="button" class="btn-view">보기</button>
				</div>
			</div>

			<div class="form-actions">
				<button type="button" class="btn btn-secondary"
					onclick="history.back()">이전</button>
				<c:choose>
					<c:when test="${empty sessionScope.user}">
						<%-- 비회원인 경우 로그인 필요 알림 --%>
						<button type="button" class="btn btn-primary" onclick="showLoginRequiredForSeat()">다음</button>
					</c:when>
					<c:otherwise>
						<%-- 로그인 사용자인 경우 정상 진행 --%>
						<button type="submit" class="btn btn-primary">다음</button>
					</c:otherwise>
				</c:choose>
			</div>
		</form>
	</main>

	<jsp:include page="/views/common/footer.jsp" />
	<script src="${pageContext.request.contextPath}/js/checkinApply.js"></script>
</body>
</html>