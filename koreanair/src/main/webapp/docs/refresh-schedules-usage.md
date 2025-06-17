# 실시간 스케줄 새로고침 기능 사용법

## 개요
`RefreshSchedulesHandler`는 `FlightScheduleHandler`의 기능을 활용하여 실시간으로 항공편 스케줄 정보를 새로고침하는 기능을 제공합니다.

## 구성 요소

### 1. 백엔드 (Java)
- **RefreshSchedulesHandler.java**: 실시간 스케줄 새로고침을 처리하는 핸들러
- **FlightScheduleService**: 항공편 데이터를 조회하는 서비스 (기존 FlightScheduleHandler와 공유)

### 2. 프론트엔드 (JavaScript)
- **refresh-schedules.js**: 새로고침 기능을 제공하는 JavaScript 라이브러리
- **refresh-controls.html**: 새로고침 UI 컴포넌트

## 사용 방법

### 1. HTML 페이지에 포함하기

```html
<!-- jQuery와 Bootstrap이 먼저 로드되어야 합니다 -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- 새로고침 기능 JavaScript 포함 -->
<script src="/js/refresh-schedules.js"></script>

<!-- 새로고침 컨트롤 UI 포함 -->
<div id="refreshControlsContainer">
    <!-- refresh-controls.html 내용을 여기에 포함 -->
</div>
```

### 2. JavaScript 함수 사용법

#### 수동 새로고침
```javascript
// 기본 새로고침 (오늘 날짜, 전체 항공편)
refreshSchedules();

// 특정 날짜와 타입으로 새로고침
refreshSchedules('2024-01-15', 'domestic');
```

#### 자동 새로고침 설정
```javascript
// 5분 간격으로 자동 새로고침 시작
startAutoRefresh(5);

// 자동 새로고침 중지
stopAutoRefresh();
```

### 3. API 엔드포인트

#### 요청
```
GET /refreshschedules.wi?date=2024-01-15&flightType=all
```

#### 응답 (성공)
```json
{
    "status": "success",
    "message": "스케줄이 성공적으로 새로고침되었습니다.",
    "data": [
        {
            "flightNumber": "KE001",
            "departure": "ICN",
            "arrival": "LAX",
            "departureTime": "14:30",
            "arrivalTime": "08:45",
            "status": "정시"
        }
    ],
    "count": 1,
    "refreshTime": "2024-01-15"
}
```

#### 응답 (오류)
```json
{
    "status": "error",
    "message": "스케줄 새로고침 중 오류가 발생했습니다: 데이터베이스 연결 실패"
}
```

## 주요 기능

### 1. 수동 새로고침
- 새로고침 버튼 클릭
- 키보드 단축키 (Ctrl+R, F5)
- JavaScript 함수 직접 호출

### 2. 자동 새로고침
- 1분, 2분, 5분, 10분, 15분, 30분 간격 설정 가능
- 토글 스위치로 쉽게 활성화/비활성화
- 백그라운드에서 자동 실행

### 3. 필터링
- 날짜 선택
- 항공편 타입 (전체/국내선/국제선)
- FlightScheduleHandler와 동일한 필터 옵션

### 4. 사용자 경험
- 로딩 상태 표시
- 성공/오류 알림 메시지
- 마지막 업데이트 시간 표시
- 반응형 디자인

## 커스터마이징

### 1. 데이터 표시 함수 수정
기본 테이블 업데이트 함수를 수정하거나 새로운 함수를 만들 수 있습니다:

```javascript
function updateScheduleDisplay(scheduleData) {
    // 커스텀 업데이트 로직
    updateMyCustomTable(scheduleData);
}
```

### 2. 알림 스타일 변경
```css
.notification {
    /* 커스텀 스타일 */
    border-radius: 10px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
```

### 3. 새로고침 간격 추가
```html
<select id="refreshInterval" class="form-control form-control-sm">
    <option value="1">1분</option>
    <option value="3">3분</option> <!-- 새로 추가 -->
    <option value="5" selected>5분</option>
</select>
```

## 문제 해결

### 1. 의존성 오류
현재 Servlet API와 Gson 라이브러리 의존성 문제가 있습니다. 다음을 확인하세요:

- Maven/Gradle 설정에 필요한 의존성 추가
- 서버 라이브러리 경로 확인
- 클래스패스 설정 확인

### 2. AJAX 요청 실패
- 서버 URL 경로 확인 (`/refreshschedules.wi`)
- CORS 설정 확인
- 네트워크 연결 상태 확인

### 3. 데이터 표시 문제
- HTML 요소 ID 확인 (`#scheduleTableBody`)
- JavaScript 콘솔에서 오류 메시지 확인
- 데이터 구조 일치 여부 확인

## 예제 통합 코드

```html
<!DOCTYPE html>
<html>
<head>
    <title>항공편 스케줄 관리</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>항공편 스케줄 관리</h1>
        
        <!-- 새로고침 컨트롤 포함 -->
        <!-- refresh-controls.html 내용 -->
        
        <!-- 스케줄 테이블 -->
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>항공편</th>
                    <th>출발지</th>
                    <th>도착지</th>
                    <th>출발시간</th>
                    <th>도착시간</th>
                    <th>상태</th>
                </tr>
            </thead>
            <tbody id="scheduleTableBody">
                <!-- 데이터가 여기에 표시됩니다 -->
            </tbody>
        </table>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="/js/refresh-schedules.js"></script>
</body>
</html>
```

## 참고사항

1. **성능**: 자동 새로고침 간격을 너무 짧게 설정하면 서버 부하가 증가할 수 있습니다.
2. **보안**: 실제 운영 환경에서는 적절한 인증 및 권한 검사를 추가해야 합니다.
3. **오류 처리**: 네트워크 오류나 서버 오류에 대한 적절한 처리가 포함되어 있습니다.
4. **브라우저 호환성**: 최신 브라우저에서 테스트되었으며, IE는 지원하지 않습니다. 