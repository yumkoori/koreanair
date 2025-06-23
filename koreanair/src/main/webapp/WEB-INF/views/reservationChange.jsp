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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reservationChange.css">
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
        <form action="#" method="POST">
            <input type="hidden" name="bookingId" value="${reservation.bookingId}">

            <h2 class="section-title-bar">3. 출발지, 도착지, 출발일, 좌석등급을 변경 후 검색하세요.</h2>
            
            <div class="change-form-container">
                 <div class="route-info-box">
                     <div class="airport-details">
                         <div class="airport-code">${reservation.departureAirportId}</div>
                         <div class="airport-name">${reservation.departureAirportName}</div>
                     </div>
                     <i class="fa-solid fa-arrow-right-long"></i>
                      <div class="airport-details">
                         <div class="airport-code">${reservation.arrivalAirportId}</div>
                         <div class="airport-name">${reservation.arrivalAirportName}</div>
                     </div>
                 </div>
                 
                 <div class="date-picker-group">
                     <i class="fa-regular fa-calendar-days"></i>
                     <input type="date" name="newDepartureDate">
                     <select name="seatClass">
                         <option value="ALL">모든 클래스</option>
                         <option value="Y">일반석</option>
                         <option value="C">프레스티지석</option>
                     </select>
                 </div>
            </div>

            <div class="passenger-info-box" style="margin-top: 2rem;">
                <strong>예약 변경 승객</strong>
                <span>${reservation.lastName} ${reservation.firstName}</span>
            </div>

            <div class="form-actions" style="border-top: none; padding-top: 2rem;">
                 <button type="submit" class="btn btn-primary" style="width: auto; padding: 12px 40px;">항공편 검색</button>
            </div>
        </form>
    </main>
    <jsp:include page="/views/common/footer.jsp" />
</body>
</html>