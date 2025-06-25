# 대한항공 관리자 시스템 UML 다이어그램

## 1. 패키지 다이어그램 (Package Diagram)

```mermaid
graph TD
    subgraph "com.koreanair"
        subgraph "controller"
            C1["DispatcherServlet"]
            C2["BookingLookupServlet"]
            C3["LoginController"]
        end
        
        subgraph "command"
            CM1["CommandHandler Interface"]
            CM2["HomeHandler"]
            CM3["LoginHandler"]
            CM4["BookingHandler"]
            CM5["FlightScheduleHandler"]
            CM6["ReservationsHandler"]
            CM7["PaymentVerifyHandler"]
            CM8["AdminPage Handlers"]
        end
        
        subgraph "model"
            subgraph "service"
                S1["AuthService"]
                S2["BookingService"]
                S3["PaymentVerifyService"]
                S4["FlightScheduleService"]
                S5["UserService"]
                S6["AdminPage Services"]
            end
            
            subgraph "dao"
                D1["ProjectDao"]
                D2["FlightDAO"]
                D3["AirportDAO"]
                D4["AdminDAO"]
            end
            
            subgraph "dto"
                DT1["User"]
                DT2["AdminReservationDTO"]
                DT3["FlightDTO"]
                DT4["PaymentDTO"]
                DT5["BookingDTO"]
            end
        end
        
        subgraph "filter"
            F1["AuthenticationFilter"]
            F2["EncodingFilter"]
        end
        
        subgraph "util"
            U1["DBConnection"]
            U2["KakaoApiService"]
            U3["KakaoConfig"]
        end
    end
    
    subgraph "External APIs"
        EXT1["Flight Schedule API"]
        EXT2["Kakao Pay API"]
        EXT3["Kakao Login API"]
    end
    
    subgraph "Database"
        DB1["Oracle Database"]
    end
    
    subgraph "Web Layer"
        WEB1["Admin JSP Pages"]
        WEB2["User JSP Pages"]
        WEB3["CSS/JS Resources"]
    end
    
    %% Dependencies
    controller --> command
    command --> service
    service --> dao
    service --> dto
    dao --> util
    dao --> dto
    filter --> util
    util --> EXT1
    util --> EXT2
    util --> EXT3
    util --> DB1
    WEB1 --> controller
    WEB2 --> controller
```

## 2. 유스케이스 다이어그램 (Use Case Diagram)

```mermaid
graph LR
    subgraph "대한항공 관리자 시스템"
        UC1["대시보드 통계 조회"]
        UC2["항공편 스케줄 관리"]
        UC3["실시간 항공편 동기화"]
        UC4["항공편 좌석 관리"]
        UC5["좌석 가격 설정"]
        UC6["예약 검색 및 조회"]
        UC7["예약 상태 관리"]
        UC8["사용자 검색"]
        UC9["사용자 정보 조회"]
        UC10["수익 통계 분석"]
        UC11["인기 노선 분석"]
        UC12["월별 예약 통계"]
        UC13["DB 데이터 저장"]
        UC14["외부 API 연동"]
    end
    
    ADMIN["관리자<br/>(Administrator)"]
    SYS["시스템<br/>(System)"]
    API["외부 API<br/>(External API)"]
    
    %% 관리자 유스케이스
    ADMIN --> UC1
    ADMIN --> UC2
    ADMIN --> UC3
    ADMIN --> UC4
    ADMIN --> UC5
    ADMIN --> UC6
    ADMIN --> UC7
    ADMIN --> UC8
    ADMIN --> UC9
    ADMIN --> UC10
    ADMIN --> UC11
    ADMIN --> UC12
    ADMIN --> UC13
    
    %% 시스템 간 관계
    UC3 --> UC14
    UC14 --> API
    UC2 --> UC13
    UC4 --> UC13
    UC5 --> UC13
    
    %% 확장 관계
    UC1 -.-> UC10
    UC1 -.-> UC11
    UC1 -.-> UC12
    UC6 -.-> UC7
```

## 3. 클래스 다이어그램 (Class Diagram)

```mermaid
classDiagram
    class User {
        -String userId
        -String korean_name
        -String email
        -String phone
        -String birth_date
        +getUserId() String
        +getKorean_name() String
        +getEmail() String
    }
    
    class AdminReservationDTO {
        -String booking_id
        -String user_name
        -String email
        -String phone
        -String start
        -String end
        -String status
        -String seat_class
        -int passenger
        -int totalPrice
        +getBooking_id() String
        +getUser_name() String
    }
    
    class FlightDTO {
        -String flight_id
        -String flightNO
        -String departure_airport_id
        -String arrival_airport_id
        +getFlight_id() String
        +getFlightNO() String
    }
    
    class AirportDTO {
        -String airportId
        -String airportName
        -String cityKor
        -String cityEng
        +getAirportId() String
        +getAirportName() String
    }
    
    class FlightSeatDTO {
        -String seat_id
        -String flight_id
        -String seat_number
        -String class_id
        -boolean is_available
        +getSeat_id() String
        +getFlight_id() String
    }
    
    class PaymentVerifyDTO {
        -String imp_uid
        -String merchant_uid
        -int amount
        -String status
        +getImp_uid() String
        +getMerchant_uid() String
    }
    
    User "1" --> "0..*" AdminReservationDTO : makes
    FlightDTO "1" --> "0..*" FlightSeatDTO : contains
    AdminReservationDTO "0..*" --> "1" FlightDTO : for
    PaymentVerifyDTO "1" --> "1" AdminReservationDTO : payment
```

## 4. 시퀀스 다이어그램 - 항공편 스케줄 동기화

```mermaid
sequenceDiagram
    participant Admin as 관리자
    participant JSP as FlightManagement.jsp
    participant Handler as FlightScheduleHandler
    participant Service as FlightScheduleService
    participant DAO as ProjectDaoImpl
    participant API as External Flight API
    participant DB as Database
    
    Admin->>JSP: 항공편 동기화 버튼 클릭
    JSP->>Handler: /syncDataBtn 요청
    
    Handler->>Service: fetchFlightData(apiUrl)
    Service->>API: HTTP GET 요청
    API-->>Service: XML 응답 데이터
    Service-->>Handler: 파싱된 항공편 데이터
    
    Handler->>Service: saveSchedulesDB(scheduleList)
    Service->>DAO: saveSchdulesDB(scheduleList)
    
    loop 각 항공편 데이터
        DAO->>DB: INSERT INTO flight
        DB-->>DAO: 저장 결과
    end
    
    DAO-->>Service: 저장 완료
    Service-->>Handler: 성공 응답
    Handler-->>JSP: JSON 응답
    JSP-->>Admin: 동기화 완료 메시지
```

## 5. 시퀀스 다이어그램 - 예약 검색 및 조회

```mermaid
sequenceDiagram
    participant Admin as 관리자
    participant JSP as ReservationManagement.jsp
    participant Handler as ReservationsHandler
    participant Service as ReservationService
    participant DAO as ProjectDaoImpl
    participant DB as Database
    
    Admin->>JSP: 검색 조건 입력 및 검색 버튼 클릭
    JSP->>Handler: /reservations.wi 요청<br/>(searchType, searchKeyword, status)
    
    Handler->>Service: reservation(searchType, searchKeyword, status)
    Service->>DAO: reservations(searchType, searchKeyword, status)
    
    DAO->>DB: SELECT with JOIN<br/>(booking, users, flight, airport, booking_status)
    DB-->>DAO: 예약 데이터 ResultSet
    
    loop 각 예약 레코드
        DAO->>DAO: AdminReservationDTO 생성
    end
    
    DAO-->>Service: List<AdminReservationDTO>
    Service-->>Handler: 예약 목록
    Handler-->>JSP: JSON 응답
    JSP-->>Admin: 검색 결과 테이블 표시
```

## 6. 시퀀스 다이어그램 - 좌석 관리

```mermaid
sequenceDiagram
    participant Admin as 관리자
    participant JSP as SeatManagement.jsp
    participant SaveHandler as FlightSeatSaveHandler
    participant LoadHandler as FlightSeatLoadHandler
    participant Service as FlightSeatSaveService
    participant DAO as ProjectDaoImpl
    participant DB as Database
    
    Admin->>JSP: 항공편 선택
    JSP->>LoadHandler: /seatload.wi 요청 (flight_id)
    
    LoadHandler->>DAO: flightSeatload(flight_id)
    DAO->>DB: SELECT flight_seat WHERE flight_id = ?
    DB-->>DAO: 좌석 데이터
    DAO-->>LoadHandler: List<FlightSeatSaveDTO>
    LoadHandler-->>JSP: 좌석 배치도 데이터
    JSP-->>Admin: 좌석 배치도 표시
    
    Admin->>JSP: 좌석 배치 수정 및 저장
    JSP->>SaveHandler: /seatsave.wi 요청 (seatList, flight_id)
    
    SaveHandler->>Service: checkDuplicateSeat(seatList)
    Service->>DAO: checkDuplicateSeat(seatList)
    DAO->>DB: SELECT 중복 체크
    DB-->>DAO: 중복 여부
    DAO-->>Service: 중복 체크 결과
    Service-->>SaveHandler: 검증 완료
    
    SaveHandler->>DAO: seatsave(seatList, flight_id)
    
    loop 각 좌석
        DAO->>DB: INSERT/UPDATE flight_seat
        DB-->>DAO: 저장 결과
    end
    
    DAO-->>SaveHandler: 저장 완료
    SaveHandler-->>JSP: 성공 응답
    JSP-->>Admin: 저장 완료 메시지
```

## 7. 컴포넌트 다이어그램 (Component Diagram)

```mermaid
graph TB
    subgraph "Presentation Layer"
        UI1["Admin JSP Pages<br/>(adminindex.jsp, flightmanagement.jsp,<br/>reservationManagement.jsp, etc.)"]
        UI2["CSS & JavaScript<br/>(Bootstrap, Chart.js, Custom JS)"]
        UI3["Static Resources<br/>(Images, Fonts)"]
    end
    
    subgraph "Web Layer"
        WEB1["DispatcherServlet<br/>(Front Controller)"]
        WEB2["AuthenticationFilter<br/>(Security)"]
        WEB3["EncodingFilter<br/>(UTF-8)"]
    end
    
    subgraph "Command Layer"
        CMD1["Admin Command Handlers<br/>(FlightScheduleHandler,<br/>ReservationsHandler, etc.)"]
        CMD2["User Command Handlers<br/>(LoginHandler, BookingHandler, etc.)"]
        CMD3["Payment Command Handlers<br/>(PaymentVerifyHandler, etc.)"]
    end
    
    subgraph "Service Layer"
        SVC1["Admin Services<br/>(FlightScheduleService,<br/>ReservationService, etc.)"]
        SVC2["User Services<br/>(AuthService, BookingService, etc.)"]
        SVC3["Payment Services<br/>(PaymentVerifyService, etc.)"]
    end
    
    subgraph "Data Access Layer"
        DAO1["ProjectDAO<br/>(Admin Operations)"]
        DAO2["FlightDAO<br/>(Flight Operations)"]
        DAO3["UserDAO<br/>(User Operations)"]
    end
    
    subgraph "Utility Layer"
        UTIL1["DBConnection<br/>(Connection Pool)"]
        UTIL2["KakaoApiService<br/>(External API)"]
    end
    
    subgraph "External Systems"
        EXT1["Flight Schedule API<br/>(Real-time Data)"]
        EXT2["Kakao Pay API<br/>(Payment Gateway)"]
        EXT3["Kakao Login API<br/>(OAuth)"]
    end
    
    subgraph "Database"
        DB1["Oracle Database<br/>(User, Flight, Booking Data)"]
    end
    
    %% Connections
    UI1 --> WEB1
    UI2 --> UI1
    UI3 --> UI1
    
    WEB1 --> CMD1
    WEB1 --> CMD2
    WEB1 --> CMD3
    WEB2 --> WEB1
    WEB3 --> WEB1
    
    CMD1 --> SVC1
    CMD2 --> SVC2
    CMD3 --> SVC3
    
    SVC1 --> DAO1
    SVC2 --> DAO2
    SVC2 --> DAO3
    SVC3 --> DAO1
    
    DAO1 --> UTIL1
    DAO2 --> UTIL1
    DAO3 --> UTIL1
    
    UTIL1 --> DB1
    UTIL2 --> EXT1
    UTIL2 --> EXT2
    UTIL2 --> EXT3
    
    SVC1 --> UTIL2
    SVC3 --> UTIL2
```

## 8. 시퀀스 다이어그램 - 대시보드 통계 조회

```mermaid
sequenceDiagram
    participant Admin as 관리자
    participant JSP as AdminIndex.jsp
    participant StatsHandler as DashBoardStatsHandler
    participant RevenueHandler as SeatRevenueHandler
    participant MonthHandler as MonthReservationHandler
    participant PopularHandler as PopularHandler
    participant DAO as ProjectDaoImpl
    participant DB as Database
    
    Admin->>JSP: 대시보드 페이지 접속
    
    par 통계 데이터 병렬 로딩
        JSP->>StatsHandler: /dashboardstats.wi 요청
        StatsHandler->>DAO: dashLoad()
        DAO->>DB: SELECT 전체 통계
        DB-->>DAO: 사용자, 예약 통계
        DAO-->>StatsHandler: DashBoardStatsDTO
        StatsHandler-->>JSP: 기본 통계 JSON
    and
        JSP->>RevenueHandler: /seatrevenue.wi 요청
        RevenueHandler->>DAO: seatRevenue()
        DAO->>DB: SELECT 좌석별 수익
        DB-->>DAO: 좌석 수익 데이터
        DAO-->>RevenueHandler: SeatRevenueDTO List
        RevenueHandler-->>JSP: 수익 통계 JSON
    and
        JSP->>MonthHandler: /monthlyreservations.wi 요청
        MonthHandler->>DAO: monthReservation(year)
        DAO->>DB: SELECT 월별 예약 수
        DB-->>DAO: 월별 데이터
        DAO-->>MonthHandler: MonthReservationDTO List
        MonthHandler-->>JSP: 월별 통계 JSON
    and
        JSP->>PopularHandler: /popularroutes.wi 요청
        PopularHandler->>DAO: popularroutes(year)
        DAO->>DB: SELECT 인기 노선
        DB-->>DAO: 노선별 예약 수
        DAO-->>PopularHandler: PopularDTO List
        PopularHandler-->>JSP: 인기 노선 JSON
    end
    
    JSP-->>Admin: 대시보드 차트 및 통계 표시
```

## 9. 액티비티 다이어그램 (Activity Diagram)

```mermaid
flowchart TD
    START([관리자 항공편 관리 시작])
    LOGIN{로그인 확인}
    MENU[항공편 관리 메뉴 선택]
    
    subgraph "실시간 데이터 동기화"
        SYNC[동기화 버튼 클릭]
        API_CALL[외부 API 호출]
        DATA_PARSE[XML 데이터 파싱]
        VALIDATE[데이터 유효성 검사]
        SAVE_DB[데이터베이스 저장]
        SYNC_SUCCESS[동기화 완료]
    end
    
    subgraph "좌석 관리"
        SELECT_FLIGHT[항공편 선택]
        LOAD_SEATS[기존 좌석 배치 로드]
        MODIFY_SEATS[좌석 배치 수정]
        SET_PRICES[좌석 가격 설정]
        VALIDATE_SEATS[좌석 중복 검사]
        SAVE_SEATS[좌석 정보 저장]
        SEAT_SUCCESS[좌석 설정 완료]
    end
    
    subgraph "스케줄 조회"
        DATE_SELECT[날짜 선택]
        FILTER_TYPE[필터 유형 선택]
        LOAD_SCHEDULE[스케줄 조회]
        DISPLAY_RESULT[결과 표시]
    end
    
    ERROR[오류 처리]
    END([프로세스 종료])
    
    START --> LOGIN
    LOGIN -->|인증 실패| ERROR
    LOGIN -->|인증 성공| MENU
    
    MENU --> SYNC
    MENU --> SELECT_FLIGHT
    MENU --> DATE_SELECT
    
    SYNC --> API_CALL
    API_CALL --> DATA_PARSE
    DATA_PARSE --> VALIDATE
    VALIDATE -->|유효하지 않음| ERROR
    VALIDATE -->|유효함| SAVE_DB
    SAVE_DB --> SYNC_SUCCESS
    
    SELECT_FLIGHT --> LOAD_SEATS
    LOAD_SEATS --> MODIFY_SEATS
    MODIFY_SEATS --> SET_PRICES
    SET_PRICES --> VALIDATE_SEATS
    VALIDATE_SEATS -->|중복 발견| ERROR
    VALIDATE_SEATS -->|유효함| SAVE_SEATS
    SAVE_SEATS --> SEAT_SUCCESS
    
    DATE_SELECT --> FILTER_TYPE
    FILTER_TYPE --> LOAD_SCHEDULE
    LOAD_SCHEDULE --> DISPLAY_RESULT
    
    SYNC_SUCCESS --> END
    SEAT_SUCCESS --> END
    DISPLAY_RESULT --> END
    ERROR --> END
```

## 10. 배치 다이어그램 (Deployment Diagram)

```mermaid
graph TB
    subgraph "Client Tier"
        CLIENT["Web Browser<br/>(Chrome, Safari, Edge)<br/>- Admin Interface<br/>- JavaScript/CSS"]
    end
    
    subgraph "Web Tier - Tomcat Server"
        TOMCAT["Apache Tomcat 9.x<br/>- JSP Engine<br/>- Servlet Container"]
        
        subgraph "Web Applications"
            WEBAPP["KoreanAir Admin App<br/>- JSP Pages<br/>- Static Resources<br/>- WEB-INF"]
        end
        
        subgraph "Java Components"
            SERVLET["Servlets<br/>- DispatcherServlet<br/>- BookingLookupServlet"]
            FILTER["Filters<br/>- AuthenticationFilter<br/>- EncodingFilter"]
            HANDLERS["Command Handlers<br/>- Admin Handlers<br/>- User Handlers"]
        end
    end
    
    subgraph "Application Tier - Business Logic"
        SERVICES["Service Layer<br/>- FlightScheduleService<br/>- ReservationService<br/>- PaymentService"]
        DAO["Data Access Layer<br/>- ProjectDAO<br/>- FlightDAO<br/>- UserDAO"]
    end
    
    subgraph "Database Tier"
        ORACLE["Oracle Database<br/>- User Tables<br/>- Flight Tables<br/>- Booking Tables<br/>- Payment Tables"]
        POOL["Connection Pool<br/>(JNDI DataSource)"]
    end
    
    subgraph "External Services"
        FLIGHT_API["Flight Schedule API<br/>(Real-time Data)"]
        KAKAO_PAY["Kakao Pay API<br/>(Payment Gateway)"]
        KAKAO_LOGIN["Kakao Login API<br/>(OAuth 2.0)"]
    end
    
    %% Connections
    CLIENT -.->|HTTPS/HTTP| TOMCAT
    TOMCAT --> WEBAPP
    WEBAPP --> SERVLET
    WEBAPP --> FILTER
    WEBAPP --> HANDLERS
    
    SERVLET --> SERVICES
    FILTER --> SERVICES
    HANDLERS --> SERVICES
    
    SERVICES --> DAO
    DAO --> POOL
    POOL --> ORACLE
    
    SERVICES -.->|HTTPS/REST| FLIGHT_API
    SERVICES -.->|HTTPS/REST| KAKAO_PAY
    SERVICES -.->|HTTPS/OAuth| KAKAO_LOGIN
    
    %% Deployment annotations
    TOMCAT -.->|"Deploy WAR"| WEBAPP
    ORACLE -.->|"JDBC Driver"| POOL
```

---

## 🌐 외부에서 보는 방법들

### 1. **Mermaid Live Editor** (추천)
- 웹사이트: https://mermaid.live/
- 위 코드를 복사해서 붙여넣으면 바로 렌더링됨
- PNG, SVG로 다운로드 가능

### 2. **GitHub/GitLab**
- 이 `.md` 파일을 GitHub/GitLab에 업로드하면 자동으로 렌더링됨

### 3. **Visual Studio Code**
- Mermaid Extension 설치 후 미리보기 가능

### 4. **Draw.io (diagrams.net)**
- Mermaid 코드를 import하여 편집 가능

### 5. **Notion, Obsidian 등**
- Mermaid 지원하는 노트 앱에서 사용 가능 