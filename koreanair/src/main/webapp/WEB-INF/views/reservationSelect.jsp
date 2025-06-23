<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>예약 변경 - 대한항공</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reservationDetail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reservationSelect.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body>
    <jsp:include page="/views/common/header.jsp" />

    <main class="page-container">
        <h1 class="page-title">예약 변경</h1>
        <div class="info-box">
            예약 번호 : ${reservation.bookingId}
        </div>

        <form action="${pageContext.request.contextPath}/changeReservationSearch.do" method="POST">
            <input type="hidden" name="bookingId" value="${reservation.bookingId}">

            <h2 class="section-title-bar">1. 변경할 여정을 선택하세요.</h2>
            <div class="journey-select-card">
                 <div class="journey-select-header">
                     <input type="checkbox" checked disabled>
                     <div class="journey-badge"><i class="fa-solid fa-location-dot"></i> 여정 1</div>
                     <div class="journey-route" style="margin-left: 1rem;">${reservation.flightName} &nbsp; ${reservation.departureAirportName} → ${reservation.arrivalAirportName}</div>
                 </div>
            </div>

            <h2 class="section-title-bar">2. 변경 대상 승객을 확인하세요.</h2>
            <div class="passenger-info-box">
                <strong>${reservation.lastName} ${reservation.firstName}</strong>
                <span>회원번호 (KE) ${reservation.memberId}</span>
            </div>

            <div class="notice-box">
                <h4>유의사항</h4>
                <ul>
                    <li>예약 변경 시 좌석/부가서비스가 취소될 수 있으니, 변경 완료 후 다시 신청해 주세요.</li>
                    <li>항공권 운임 규정에 따라 변경이 가능한지 확인해 주세요.</li>
                    <li>항공권 종류에 따라 재발행 수수료와 운임 차액이 발생할 수 있으며, 결제가 필요한 경우 예약 변경이 완료되지 않을 수 있습니다.</li>
                </ul>
            </div>

            <div class="form-actions" style="border-top: none; padding-top: 2rem;">
                 <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
                 <button type="submit" class="btn btn-primary">다음</button>
            </div>
        </form>
    </main>

    <jsp:include page="/views/common/footer.jsp" />
</body>
</html>