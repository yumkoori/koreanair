<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>승객 파싱 디버깅</title>
    <style>
        body { font-family: 'Noto Sans KR', sans-serif; padding: 20px; }
        .debug-box { background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 8px; border: 1px solid #dee2e6; }
        .passenger-card { background: white; border: 1px solid #ccc; margin: 10px 0; padding: 15px; border-radius: 8px; }
        .passenger-header { font-weight: bold; color: #0064de; margin-bottom: 10px; }
        .error { background: #f8d7da; color: #721c24; }
        .success { background: #d1edff; color: #0c5460; }
    </style>
</head>
<body>
    <h1>승객 파싱 디버깅 페이지</h1>
    
    <%
        // 승객 정보 파싱 로직 (booking.jsp와 동일)
        String passengersParam = request.getParameter("passengers");
        String passengerDisplay = passengersParam != null ? passengersParam : "성인 1명";
        
        out.println("<div class='debug-box'>");
        out.println("<h3>🔍 URL 파라미터 분석</h3>");
        out.println("원본 passengers 파라미터: <strong>" + (passengersParam != null ? passengersParam : "null") + "</strong><br>");
        out.println("URL 쿼리 스트링: <strong>" + (request.getQueryString() != null ? request.getQueryString() : "null") + "</strong><br>");
        
        // URL 디코딩 처리
        String decodedPassengers = passengersParam;
        if (passengersParam != null) {
            try {
                decodedPassengers = java.net.URLDecoder.decode(passengersParam, "UTF-8");
                out.println("디코딩 후 passengers 파라미터: <strong>" + decodedPassengers + "</strong><br>");
            } catch (Exception e) {
                out.println("URL 디코딩 오류: <strong>" + e.getMessage() + "</strong><br>");
                decodedPassengers = passengersParam;
            }
        }
        out.println("</div>");
        
        // 승객 수 파싱
        int adultCount = 0;
        int childCount = 0;
        int infantCount = 0;
        
        out.println("<div class='debug-box'>");
        out.println("<h3>🧮 정규표현식 매칭 결과</h3>");
        
        if (decodedPassengers != null && !decodedPassengers.trim().isEmpty()) {
            // 성인 패턴 매칭
            java.util.regex.Pattern adultPattern = java.util.regex.Pattern.compile("성인[\\s\\+]*(\\d+)명");
            java.util.regex.Matcher adultMatcher = adultPattern.matcher(decodedPassengers);
            if (adultMatcher.find()) {
                adultCount = Integer.parseInt(adultMatcher.group(1));
                out.println("✅ 성인 매칭 성공: " + adultMatcher.group(0) + " → " + adultCount + "명<br>");
            } else {
                out.println("❌ 성인 패턴 매칭 실패<br>");
            }
            
            // 소아 패턴 매칭
            java.util.regex.Pattern childPattern = java.util.regex.Pattern.compile("소아[\\s\\+]*(\\d+)명");
            java.util.regex.Matcher childMatcher = childPattern.matcher(decodedPassengers);
            if (childMatcher.find()) {
                childCount = Integer.parseInt(childMatcher.group(1));
                out.println("✅ 소아 매칭 성공: " + childMatcher.group(0) + " → " + childCount + "명<br>");
            } else {
                out.println("ℹ️ 소아 패턴 매칭 안됨 (정상)<br>");
            }
            
            // 유아 패턴 매칭
            java.util.regex.Pattern infantPattern = java.util.regex.Pattern.compile("유아[\\s\\+]*(\\d+)명");
            java.util.regex.Matcher infantMatcher = infantPattern.matcher(decodedPassengers);
            if (infantMatcher.find()) {
                infantCount = Integer.parseInt(infantMatcher.group(1));
                out.println("✅ 유아 매칭 성공: " + infantMatcher.group(0) + " → " + infantCount + "명<br>");
            } else {
                out.println("ℹ️ 유아 패턴 매칭 안됨 (정상)<br>");
            }
            
            // 패턴 매칭이 모두 실패한 경우를 위한 fallback
            if (adultCount == 0 && childCount == 0 && infantCount == 0) {
                out.println("⚠️ 모든 패턴 매칭 실패, 기본값 설정<br>");
                adultCount = 1; // 기본값
            }
        } else {
            out.println("⚠️ passengers 파라미터 없음, 기본값 설정<br>");
            adultCount = 1; // 기본값
        }
        out.println("</div>");
        
        int totalPassengers = adultCount + childCount + infantCount;
        
        out.println("<div class='debug-box success'>");
        out.println("<h3>📊 최종 파싱 결과</h3>");
        out.println("성인: <strong>" + adultCount + "</strong>명<br>");
        out.println("소아: <strong>" + childCount + "</strong>명<br>");
        out.println("유아: <strong>" + infantCount + "</strong>명<br>");
        out.println("총 승객: <strong>" + totalPassengers + "</strong>명<br>");
        out.println("</div>");
    %>
    
    <div class="debug-box">
        <h3>🎯 동적 카드 생성 테스트</h3>
        <p>아래에 파싱된 승객 수만큼 카드가 생성되어야 합니다:</p>
        
        <%
            int passengerIndex = 0;
            
            // 성인 승객 카드 생성
            for (int i = 1; i <= adultCount; i++) {
                passengerIndex++;
        %>
        <div class="passenger-card">
            <div class="passenger-header">성인 <%= i %> (카드 ID: passengerCard<%= passengerIndex %>)</div>
            <div>이 카드가 보인다면 JSP 루프가 정상 작동 중입니다!</div>
        </div>
        <% } %>
        
        <%
            // 소아 승객 카드 생성
            for (int i = 1; i <= childCount; i++) {
                passengerIndex++;
        %>
        <div class="passenger-card">
            <div class="passenger-header">소아 <%= i %> (카드 ID: passengerCard<%= passengerIndex %>)</div>
            <div>이 카드가 보인다면 JSP 루프가 정상 작동 중입니다!</div>
        </div>
        <% } %>
        
        <%
            // 유아 승객 카드 생성
            for (int i = 1; i <= infantCount; i++) {
                passengerIndex++;
        %>
        <div class="passenger-card">
            <div class="passenger-header">유아 <%= i %> (카드 ID: passengerCard<%= passengerIndex %>)</div>
            <div>이 카드가 보인다면 JSP 루프가 정상 작동 중입니다!</div>
        </div>
        <% } %>
    </div>
    
    <div class="debug-box">
        <h3>🔗 테스트 링크</h3>
        <a href="?passengers=성인 1명">성인 1명</a> | 
        <a href="?passengers=성인 2명">성인 2명</a> | 
        <a href="?passengers=성인+2명">성인+2명</a> | 
        <a href="?passengers=성인 2명, 소아 1명">성인 2명, 소아 1명</a>
    </div>
    
    <div class="debug-box">
        <h3>➡️ 다음 단계</h3>
        <p>위 테스트가 성공하면 <a href="views/booking/booking.jsp?passengers=성인+2명">실제 booking.jsp</a>로 이동해서 확인하세요.</p>
    </div>
</body>
</html> 