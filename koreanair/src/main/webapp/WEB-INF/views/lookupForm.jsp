<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>예약 조회 - Korean Air</title>
    <%-- 공통 CSS 또는 필요한 CSS 파일을 여기에 링크합니다. --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lookupForm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>

   <jsp:include page="/views/common/header.jsp" />

    <main class="lookup-main-content">
        <div class="lookup-container">
            <div class="lookup-header">
                <i class="fa-solid fa-ticket-alt"></i>
                <h2>예약 조회</h2>
            </div>

            <%-- 예약 조회 실패 시 메시지를 표시합니다 --%>
            <% if (session.getAttribute("lookupFailed") != null) { %>
                <div class="error-message">
                    입력하신 정보와 일치하는 예약이 없습니다. 다시 확인해주세요.
                </div>
            <% 
                session.removeAttribute("lookupFailed"); // 메시지 출력 후 세션에서 제거
               } 
            %>

            <form class="lookup-form" action="${pageContext.request.contextPath}/lookup" method="post">
                <div class="form-group full-width">
                    <label for="bookingId">예약번호 또는 항공권번호</label>
                    <input type="text" id="bookingId" name="bookingId" placeholder="예) A1B2C3 또는 1801234567890" required>
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
                    <label for="departureDate">출발일</label>
                    <input type="date" id="departureDate" name="departureDate" required>
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
                    <button type="submit" class="btn btn-primary">예약 조회 <i class="fa-solid fa-arrow-right"></i></button>
                </div>
            </form>
        </div>
    </main>

    <jsp:include page="/views/common/footer.jsp" />

</body>
</html>