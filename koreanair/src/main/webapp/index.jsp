<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koreanair.model.dto.User" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, user-scalable=yes">
    <!-- 웹페이지 최소 너비 576px 보장을 위한 추가 메타 태그 -->
    <meta name="format-detection" content="telephone=no">
    <title>항공사 웹사이트</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body>
    <!-- 모듈화된 헤더 포함 -->
    <jsp:include page="views/common/header.jsp" />

    <section class="booking-widget">
        <div class="container">
            <div class="booking-card">
                <div class="booking-tabs">
                    <button class="tab-btn active" data-tab="flight">항공권 예매</button>
                    <button class="tab-btn" data-tab="checkin">예약 조회</button>
                    <button class="tab-btn" data-tab="schedule">체크인</button>
                    <button class="tab-btn" data-tab="status">출도착/스케줄</button>
                </div>
                
                <div class="booking-content active" id="flight">
                    <div class="trip-type-section">
                        <div class="trip-type-buttons">
                            <button class="trip-type-btn active" data-type="round">왕복</button>
                            <button class="trip-type-btn" data-type="oneway">편도</button>
                            <button class="trip-type-btn" data-type="multi">다구간</button>
                        </div>
                        <div class="special-options">
                            <label class="checkbox-label">
                                <input type="checkbox" id="award-ticket">
                                <span class="checkmark"></span>
                                가까운 날짜 함께 조회
                            </label>
                        </div>
                    </div>
                    
                    <div class="route-section">
                        <div class="route-inputs">
                            <div class="airport-input departure">
                                <div class="airport-code">SEL</div>
                                <div class="airport-name">서울</div>
                            </div>
                            <button class="swap-route-btn">
                                <i class="fas fa-exchange-alt"></i>
                            </button>
                            <div class="airport-input arrival">
                                <div class="airport-code">To</div>
                                <div class="airport-name">도착지</div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="booking-details">
                        <div class="date-section">
                            <div class="date-input">
                                <label>출발일</label>
                                <input type="date" value="2025-05-28">
                            </div>
                        </div>
                        
                        <div class="passenger-section">
                            <div class="passenger-input">
                                <label>탑승객</label>
                                <select>
                                    <option>성인 1명</option>
                                    <option>성인 2명</option>
                                    <option>성인 3명</option>
                                    <option>성인 4명</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="class-section">
                            <div class="class-input">
                                <label>좌석 등급</label>
                                <select>
                                    <option>선택하세요</option>
                                    <option>일반석</option>
                                    <option>비즈니스석</option>
                                    <option>일등석</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="search-section">
                            <button class="search-flights-btn">항공편 검색</button>
                        </div>
                    </div>
                </div>
                
                <div class="booking-content" id="checkin">
                    <div class="checkin-form">
                        <div class="form-description">
                            <p>예약번호 또는 항공권번호</p>
                        </div>
                        <div class="checkin-inputs">
                            <div class="input-group">
                                <input type="text" placeholder="예) A1B2C3 또는 1801234567890">
                            </div>
                            <div class="input-group">
                                <label>출발일</label>
                                <input type="date">
                            </div>
                            <div class="input-group">
                                <label>성</label>
                                <input type="text" placeholder="">
                            </div>
                            <div class="input-group">
                                <label>이름</label>
                                <input type="text" placeholder="">
                            </div>
                            <div class="search-section">
                                <button class="search-flights-btn">조회</button>
                            </div>
                        </div>
                        <div class="form-notice">
                            <label class="checkbox-label">
                                <input type="checkbox">
                                <span class="checkmark"></span>
                                [필수] 본인의 예약 정보이거나 승객으로부터 조회를 위임 받은 예약 정보입니다.
                            </label>
                            <p class="notice-text">국내선 입금 예약인 경우, 입금 상태 여부를 입력하세요.</p>
                        </div>
                    </div>
                </div>
                
                <div class="booking-content" id="schedule">
                    <div class="schedule-form">
                        <div class="form-description">
                            <p>예약번호 또는 항공권번호</p>
                        </div>
                        <div class="schedule-inputs">
                            <div class="input-group">
                                <input type="text" placeholder="예) A1B2C3 또는 1801234567890">
                            </div>
                            <div class="input-group">
                                <label>출발일</label>
                                <select>
                                    <option>2025년 05월 28일</option>
                                </select>
                            </div>
                            <div class="input-group">
                                <label>성</label>
                                <input type="text" placeholder="">
                            </div>
                            <div class="input-group">
                                <label>이름</label>
                                <input type="text" placeholder="">
                            </div>
                            <div class="search-section">
                                <button class="search-flights-btn">조회</button>
                            </div>
                        </div>
                        <div class="form-notice">
                            <label class="checkbox-label">
                                <input type="checkbox">
                                <span class="checkmark"></span>
                                [필수] 본인의 예약 정보이거나 승객으로부터 조회를 위임 받은 예약 정보입니다.
                            </label>
                            <p class="notice-text">국내선 입금 예약인 경우, 입금 상태 여부를 입력하세요.</p>
                        </div>
                    </div>
                </div>
                
                <div class="booking-content" id="status">
                    <div class="status-form">
                        <div class="status-options">
                            <button class="status-btn active">출도착 조회</button>
                            <button class="status-btn">수간 스케줄</button>
                            <button class="status-btn">출/도착지</button>
                            <button class="status-btn">편명</button>
                        </div>
                        <div class="status-inputs">
                            <div class="route-inputs">
                                <div class="airport-input">
                                    <div class="airport-code">From</div>
                                    <div class="airport-name">출발지</div>
                                </div>
                                <button class="swap-route-btn">
                                    <i class="fas fa-exchange-alt"></i>
                                </button>
                                <div class="airport-input">
                                    <div class="airport-code">To</div>
                                    <div class="airport-name">도착지</div>
                                </div>
                            </div>
                            <div class="date-input">
                                <label>출발일</label>
                                <input type="date" value="2025-05-28">
                            </div>
                            <div class="search-section">
                                <button class="search-flights-btn">조회</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="banner">
        <div class="container">
            <div class="banner-slider">
                <div class="slide active" style="background-image: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('https://images.unsplash.com/photo-1436491865332-7a61a109cc05?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');">
                    <div class="slide-content">
                        <h2>유럽 노선 특가 이벤트</h2>
                        <p>꿈꾸던 유럽 여행, 지금 특별한 가격으로 만나보세요</p>
                        <a href="#" class="btn">자세히 보기</a>
                    </div>
                </div>
                <div class="slide" style="background-image: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('https://images.unsplash.com/photo-1540339832862-474599807836?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');">
                    <div class="slide-content">
                        <h2>프리미엄 신규 기내 서비스</h2>
                        <p>더욱 편안하고 특별한 여행을 위한 새로운 기내 서비스를 소개합니다</p>
                        <a href="#" class="btn">자세히 보기</a>
                    </div>
                </div>
                <div class="slide" style="background-image: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('https://images.unsplash.com/photo-1464038008305-ee8def75f234?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');">
                    <div class="slide-content">
                        <h2>마일리지 더블 적립 혜택</h2>
                        <p>이번 달 특별 프로모션, 모든 노선 마일리지 두 배 적립</p>
                        <a href="#" class="btn">자세히 보기</a>
                    </div>
                </div>
            </div>
            <div class="banner-nav">
                <button class="prev"><i class="fas fa-chevron-left"></i></button>
                <div class="dots">
                    <span class="dot active"></span>
                    <span class="dot"></span>
                    <span class="dot"></span>
                </div>
                <button class="next"><i class="fas fa-chevron-right"></i></button>
            </div>
        </div>
    </section>

    <section class="features">
        <div class="container">
            <div class="feature-box">
                <div class="feature-icon">
                    <i class="fas fa-map-marked-alt"></i>
                </div>
                <h3>취항지 안내</h3>
                <p>전 세계 40개국 120개 도시로 편리하게 여행하세요</p>
                <a href="#" class="feature-link">더 알아보기</a>
            </div>
            <div class="feature-box">
                <div class="feature-icon">
                    <i class="fas fa-plane"></i>
                </div>
                <h3>항공기 소개</h3>
                <p>최신 항공기 보유로 안전하고 쾌적한 여행을 약속합니다</p>
                <a href="#" class="feature-link">더 알아보기</a>
            </div>
            <div class="feature-box">
                <div class="feature-icon">
                    <i class="fas fa-gift"></i>
                </div>
                <h3>마일리지 혜택</h3>
                <p>다양한 적립과 사용으로 더 많은 혜택을 누리세요</p>
                <a href="#" class="feature-link">더 알아보기</a>
            </div>
            <div class="feature-box">
                <div class="feature-icon">
                    <i class="fas fa-headset"></i>
                </div>
                <h3>고객 지원</h3>
                <p>연중무휴 고객센터 운영으로 문의사항을 빠르게 해결해 드립니다</p>
                <a href="#" class="feature-link">더 알아보기</a>
            </div>
        </div>
    </section>

    <section class="destinations">
        <div class="container">
            <h2 class="section-title">인기 여행지</h2>
            <div class="destination-grid">
                <div class="destination-card">
                    <div class="destination-img" style="background-image: url('https://images.unsplash.com/photo-1499856871958-5b9627545d1a?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');"></div>
                    <div class="destination-info">
                        <h3>파리</h3>
                        <p class="price">편도 <span>650,000원~</span></p>
                        <a href="#" class="btn-outline">예매하기</a>
                    </div>
                </div>
                <div class="destination-card">
                    <div class="destination-img" style="background-image: url('https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');"></div>
                    <div class="destination-info">
                        <h3>뉴욕</h3>
                        <p class="price">편도 <span>780,000원~</span></p>
                        <a href="#" class="btn-outline">예매하기</a>
                    </div>
                </div>
                <div class="destination-card">
                    <div class="destination-img" style="background-image: url('https://images.unsplash.com/photo-1513407030348-c983a97b98d8?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');"></div>
                    <div class="destination-info">
                        <h3>도쿄</h3>
                        <p class="price">편도 <span>250,000원~</span></p>
                        <a href="#" class="btn-outline">예매하기</a>
                    </div>
                </div>
                <div class="destination-card">
                    <div class="destination-img" style="background-image: url('https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');"></div>
                    <div class="destination-info">
                        <h3>시드니</h3>
                        <p class="price">편도 <span>720,000원~</span></p>
                        <a href="#" class="btn-outline">예매하기</a>
                    </div>
                </div>
                <div class="destination-card">
                    <div class="destination-img" style="background-image: url('https://images.unsplash.com/photo-1528181304800-259b08848526?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');"></div>
                    <div class="destination-info">
                        <h3>방콕</h3>
                        <p class="price">편도 <span>280,000원~</span></p>
                        <a href="#" class="btn-outline">예매하기</a>
                    </div>
                </div>
                <div class="destination-card">
                    <div class="destination-img" style="background-image: url('https://images.unsplash.com/photo-1552832230-c0197dd311b5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');"></div>
                    <div class="destination-info">
                        <h3>로마</h3>
                        <p class="price">편도 <span>680,000원~</span></p>
                        <a href="#" class="btn-outline">예매하기</a>
                    </div>
                </div>
            </div>
            <div class="view-more">
                <a href="#" class="btn">더 많은 여행지 보기</a>
            </div>
        </div>
    </section>

    <section class="news">
        <div class="container">
            <h2 class="section-title">항공사 소식</h2>
            <div class="news-grid">
                <div class="news-card">
                    <div class="news-img" style="background-image: url('https://images.unsplash.com/photo-1436491865332-7a61a109cc05?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');"></div>
                    <div class="news-content">
                        <span class="news-date">2023.07.15</span>
                        <h3>신규 노선 오픈 안내</h3>
                        <p>2023년 9월부터 새롭게 선보이는 대양주 노선에 대한 안내입니다.</p>
                        <a href="#" class="read-more">자세히 보기</a>
                    </div>
                </div>
                <div class="news-card">
                    <div class="news-img" style="background-image: url('https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');"></div>
                    <div class="news-content">
                        <span class="news-date">2023.07.08</span>
                        <h3>프리미엄 기내식 리뉴얼</h3>
                        <p>최고의 셰프와 협업한 새로운 기내식 메뉴를 소개합니다.</p>
                        <a href="#" class="read-more">자세히 보기</a>
                    </div>
                </div>
                <div class="news-card">
                    <div class="news-img" style="background-image: url('https://images.unsplash.com/photo-1530521954074-e64f6810b32d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');"></div>
                    <div class="news-content">
                        <span class="news-date">2023.07.01</span>
                        <h3>여름 시즌 특별 프로모션</h3>
                        <p>여름 휴가철 맞이 특별 할인 프로모션을 확인하세요.</p>
                        <a href="#" class="read-more">자세히 보기</a>
                    </div>
                </div>
            </div>
            <div class="view-more">
                <a href="#" class="btn">모든 소식 보기</a>
            </div>
        </div>
    </section>

    <!-- 모듈화된 푸터 포함 -->
    <jsp:include page="views/common/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/index.js"></script>
</body>
</html> 