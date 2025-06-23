<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>체크인 조회 - Korean Air</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkupForm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>

    <jsp:include page="/views/common/header.jsp" />

    <main class="checkup-main-content">
        <div class="checkup-container">
            <div class="checkup-header">
                <i class="fa-solid fa-ticket-alt"></i>
                <h2>체크인 조회</h2>
            </div>
            <form id="checkupForm" class="checkup-form" data-context-path="${pageContext.request.contextPath}">

                <div id="errorMessageContainer" class="full-width"></div>

                <div class="form-group full-width">
                    <label for="checkingId">예약번호 또는 항공권번호</label>
                    <input type="text" id="checkingId" name="checkingId" placeholder="예) A1B2C3 또는 1801234567890" required>
                </div>

                <div class="form-group">
                    <label for="lastName">성</label>
                    <input type="text" id="lastName" name="lastName" placeholder="영문 성 입력" required>
                </div>

                <div class="form-group">
                    <label for="firstName">이름</label>
                    <input type="text" id="firstName" name="firstName" placeholder="영문 이름 입력" required>
                </div>

				<div class="form-group full-width">
					<label for="checkinDepartureDate">출발일</label> <select
						id="checkinDepartureDate" name="departureDate" required></select>
				</div>

				<div class="form-group full-width instruction-text">
                    <span><i class="fa-solid fa-circle-info"></i> 국내선 한글 예약인 경우, 한글 성과 이름을 입력하세요.</span>
                </div>

                <div class="form-group full-width">
                    <label class="checkbox-group">
                        <input type="checkbox" name="agreement" required>
                        <span class="checkmark"></span>
                        <span class="checkbox-label">[필수] 본인의 예약 정보이거나 승객으로부터 조회를 위임 받은 예약 정보입니다.</span>
                    </label>
                </div>

                <div class="form-actions full-width">
                    <button type="button" class="btn btn-secondary" onclick="location.href='${pageContext.request.contextPath}/'">취소</button>
                    <button type="submit" class="btn btn-primary">체크인<i class="fa-solid fa-arrow-right"></i></button>
                </div>
            </form>
        </div>
    </main>

    <jsp:include page="/views/common/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/checkupForm.js"></script>
</body>
</html>