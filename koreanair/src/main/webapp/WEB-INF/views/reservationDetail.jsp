<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, user-scalable=yes">
    <meta name="format-detection" content="telephone=no">
    <title>예약 상세 - 대한항공</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reservationDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/views/common/header.jsp" />

<c:choose>
    <c:when test="${not empty reservation}">
        <c:set var="durationInMillis" value="${reservation.arrivalTime.time - reservation.departureTime.time}" />
        <c:set var="durationInMinutes" value="${durationInMillis / (1000 * 60)}" />
        <c:set var="durationHours" value="${durationInMinutes / 60}" />
        <c:set var="durationMinutes" value="${durationInMinutes % 60}" />

        <div class="detail-header-bg">
            <div class="detail-header-inner">
                <div class="detail-booking-id">
                    예약번호 : <span class="booking-id-value">${reservation.bookingId}</span>
                </div>
                <div class="detail-edit-link">
                    예약 별명을 지어주세요 <i class="fa-regular fa-pen-to-square"></i>
                </div>
            </div>
        </div>
        <div class="detail-main-card">
            
            <div class="journey-card-outer">
                <div class="journey-card-header">
                    <div class="journey-badge"><i class="fa-solid fa-location-dot"></i> 여정 1</div>
                    <div class="journey-route">${reservation.departureAirportId} <i class="fa-solid fa-arrow-right-long"></i> ${reservation.arrivalAirportId}</div>
                    <div class="journey-arrow"><i class="fa-solid fa-chevron-down"></i></div>
                </div>

                <div class="journey-card-body">
                    <div class="journey-details-left">
                        <div class="journey-date">
                            <fmt:formatDate value="${reservation.departureTime}" pattern="yyyy년 MM월 dd일 (E)" />
                        </div>

                        <div class="flight-path-info">
                            <div class="flight-time-location">
                                <div class="time"><fmt:formatDate value="${reservation.departureTime}" pattern="HH:mm" /></div>
                                <div class="city">${reservation.departureAirportName}</div>
                                <div class="airport-code">${reservation.departureAirportId}</div>
                            </div>

                            <div class="flight-path-graphic">
                                <div class="duration">
                                    <fmt:formatNumber value="${durationHours}" pattern="0" />시간 
                                    <fmt:formatNumber value="${durationMinutes}" pattern="0" />분
                                </div>
                                <div class="path-line">
                                    <span class="dot start"></span>
                                    <span class="line"></span>
                                    <i class="fa-solid fa-plane-up"></i>
                                    <span class="line"></span>
                                    <span class="dot end"></span>
                                </div>
                            </div>
                            
                            <div class="flight-time-location">
                                <div class="time"><fmt:formatDate value="${reservation.arrivalTime}" pattern="HH:mm" /></div>
                                <div class="city">${reservation.arrivalAirportName}</div>
                                <div class="airport-code">${reservation.arrivalAirportId}</div>
                            </div>
                        </div>
                        <div class="checkin-button-container">
                            <c:choose>
                                <c:when test="${not empty reservation.assignedSeat}">
                                    <a href="${pageContext.request.contextPath}/checkinDetail.do?bookingId=${reservation.bookingId}" class="checkin-btn">
                                        <i class="fas fa-user-check"></i> 체크인
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/checkinDetail.do?bookingId=${reservation.bookingId}" class="checkin-btn">
                                        <i class="fas fa-plane-departure"></i> 온라인 체크인
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="flight-extra-info">
                            <span>${reservation.flightId}</span>
                            <span>&middot;</span>
                            <span><c:out value="${not empty reservation.aircraftType ? reservation.aircraftType : 'B737-900'}" /></span>
                             <span>&middot;</span>
                            <span>${reservation.cabinClass}</span>
                        </div>
                    </div>
                    <div class="journey-services-right">
                        <div class="services-box">
                            <h3 class="services-title">좌석 및 서비스 신청</h3>
                            <div class="service-items">
                                <c:choose>
                                    <c:when test="${not empty reservation.assignedSeat}">
                                        <a href="${pageContext.request.contextPath}/checkinSeat.do?bookingId=${reservation.bookingId}" class="service-item">
                                            <i class="fa-solid fa-chair"></i>
                                            <span>${reservation.assignedSeat} (좌석 변경)</span>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/checkinSeat.do?bookingId=${reservation.bookingId}" class="service-item">
                                            <i class="fa-solid fa-chair"></i>
                                            <span>좌석 신청</span>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                                <a href="#" id="openServicesModalBtn" class="service-item">
                                    <i class="fa-solid fa-ellipsis"></i> <span>기타 서비스</span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
                <div id="servicesModal" class="modal-overlay" style="display:none;">
                    <div class="modal-container">
                        <div class="modal-header">
                            <h2>기타 부가서비스 신청/변경</h2>
                            <button id="closeServicesModalBtn" class="modal-close-btn">
                                <i class="fa-solid fa-xmark"></i>
                            </button>
                        </div>
                        <div class="modal-body">
                            <a href="#" class="modal-service-link">
                                <i class="fa-solid fa-dog"></i> 반려동물 동반
                            </a>
                            <a href="#" class="modal-service-link">
                                <i class="fa-solid fa-wheelchair"></i> 도움이 필요한 승객
                            </a>
                        </div>
                        <div class="modal-footer">
                            <button id="footerCloseServicesModalBtn" class="btn-secondary">닫기</button>
                        </div>
                    </div>
                </div>
                <div class="section-card passenger-section">
                    <div class="section-title">
                        <span>탑승객 정보</span>
                        <span class="section-toggle"><i class="fa-solid fa-chevron-down"></i></span>
                    </div>
                    <div class="section-divider"></div>
                    <div class="passenger-table">
                        <div class="passenger-row passenger-row-header">
                            <div class="passenger-cell label">대표자 연락처</div>
                            <div class="passenger-cell value">
                                <div class="contact-item">
                                    <i class="fa-solid fa-phone"></i> 전화번호&nbsp; <span
                                        class="strong">${reservation.phone}</span>
                                </div>
                                <div class="contact-item">
                                    <i class="fa-solid fa-envelope"></i> 이메일&nbsp; <span
                                        class="strong">${reservation.email}</span>
                                </div>
                            </div>
                        </div>
                        <div class="passenger-row passenger-row-header2">
                            <div class="passenger-cell label">탑승객/회원번호</div>
                            <div class="passenger-cell label">연락처</div>
                            <div class="passenger-cell label">항공권 번호</div>
                            <div class="passenger-cell label">e-티켓/구매증서</div>
                        </div>
                        <div class="passenger-row">
                            <div class="passenger-cell value">
                                <span class="strong">${reservation.lastName} ${reservation.firstName}</span><br>
                                (KE) ${reservation.memberId}
                            </div>
                            <div class="passenger-cell value">
                                ${reservation.phone}<br>
                                ${reservation.email}
                                <button class="edit-btn" title="수정"><i class="fa-regular fa-pen-to-square"></i></button>
                            </div>
                            <div class="passenger-cell value">
                                <c:choose>
                                    <c:when test="${not empty reservation.ticketNumber}">${reservation.ticketNumber}</c:when>
                                    <c:otherwise>정보 없음</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="passenger-cell value">
                                <a href="#" class="ticket-link">e-티켓/영수증 보기</a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="section-card action-section">
                    <div class="section-title">
                        <span>여행 변경 및 취소</span>
                        <span class="section-toggle"><i class="fa-solid fa-chevron-down"></i></span>
                    </div>
                    <div class="section-divider"></div>
					 <div class="action-btn-row">
					    <a href="${pageContext.request.contextPath}/changeReservationSelect.do?bookingId=${reservation.bookingId}" class="action-btn">
					        <div class="action-btn-icon"><i class="fa-regular fa-calendar-days"></i></div>
					        <div class="action-btn-label">
					            <span>예약변경</span>
					            <i class="fa-solid fa-chevron-right"></i>
					        </div>
					    </a>
					    <a href="${pageContext.request.contextPath}/cancelReservationForm.do?bookingId=${reservation.bookingId}" class="action-btn">
					        <div class="action-btn-icon"><i class="fas fa-times-circle"></i></div>
					        <div class="action-btn-label">
					            <span>예약 취소</span>
					        </div>
					    </a>
					</div>
                </div>

            </c:when>
    <c:otherwise>
        <div class="container">
            <div class="card">
                <div class="card-content" style="text-align: center;">
                    <h2>오류</h2>
                    <p>요청하신 예약 정보를 찾을 수 없습니다.</p>
                    <a href="${pageContext.request.contextPath}/index.jsp">홈으로 돌아가기</a>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/views/common/footer.jsp" />

<script src="${pageContext.request.contextPath}/js/reservationDetail.js"></script>

</body>
</html>