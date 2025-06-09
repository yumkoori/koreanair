<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header>
    <div class="header-top">
        <div class="container">
            <div class="top-nav-left">
                <a href="#" class="top-link">
                    <i class="fas fa-gift"></i>
                    이벤트
                </a>
                <a href="#" class="top-link">
                    <i class="fas fa-question-circle"></i>
                    자주 묻는 질문
                </a>
            </div>
            <div class="top-nav-right">
                <div class="language-selector">
                    <img src="https://flagcdn.com/w20/kr.png" alt="한국" width="20">
                    <span>대한민국 - 한국어</span>
                    <i class="fas fa-chevron-down"></i>
                </div>
                <a href="#" class="top-link">
                    <i class="fas fa-user"></i>
                    회원가입
                </a>
            </div>
        </div>
    </div>
    <div class="header-main">
        <div class="container">
            <div class="logo">
                <svg width="180" height="45" viewBox="0 0 180 45">
                    <!-- 대한항공 로고 -->
                    <g>
                        <!-- 외부 원 테두리 -->
                        <circle cx="22.5" cy="22.5" r="18" fill="none" stroke="#003876" stroke-width="2.5"/>
                        
                        <!-- 태극 심볼 - 심플 버전 -->
                        <g transform="translate(22.5, 22.5) rotate(90) scale(-1, 1)">
                            <!-- 배경 원 (빨간색) -->
                            <circle cx="0" cy="0" r="15" fill="#CD212A"/>
                            
                            <!-- S자 모양의 파란색 영역 -->
                            <path d="M 0,-15 A 7.5,7.5 0 0,1 0,0 A 7.5,7.5 0 0,0 0,15 A 15,15 0 0,1 0,-15" fill="#003876"/>
                            
                            <!-- 작은 빨간색 원 (하단) -->
                            <circle cx="0" cy="7.5" r="7.5" fill="#CD212A"/>
                            
                            <!-- 작은 파란색 원 (상단) -->
                            <circle cx="0" cy="-7.5" r="7.5" fill="#003876"/>
                        </g>
                    </g>
                    
                    <!-- KOREAN AIR 텍스트 -->
                    <text x="55" y="28" font-family="'Nanum Gothic', Arial, sans-serif" font-size="18" font-weight="bold" fill="#003876" letter-spacing="1px">KOREAN AIR</text>
                </svg>
            </div>
            <nav class="main-nav">
                <ul>
                    <li class="nav-item dropdown">
                        <a href="#" class="nav-link">예약</a>
                        <div class="dropdown-menu">
                            <div class="dropdown-content">
                                <div class="dropdown-column">
                                    <h4>항공권 예약</h4>
                                    <ul>
                                        <li><a href="#">항공편 검색</a></li>
                                        <li><a href="#">다구간 예약</a></li>
                                        <li><a href="#">그룹 예약</a></li>
                                        <li><a href="#">특가 항공권</a></li>
                                    </ul>
                                </div>
                                <div class="dropdown-column">
                                    <h4>예약 관리</h4>
                                    <ul>
                                        <li><a href="#">예약 조회/변경</a></li>
                                        <li><a href="#">좌석 선택</a></li>
                                        <li><a href="#">기내식 선택</a></li>
                                        <li><a href="#">수하물 추가</a></li>
                                    </ul>
                                </div>
                                <div class="dropdown-column">
                                    <h4>부가 서비스</h4>
                                    <ul>
                                        <li><a href="#">호텔 예약</a></li>
                                        <li><a href="#">렌터카 예약</a></li>
                                        <li><a href="#">여행자 보험</a></li>
                                        <li><a href="#">공항 라운지</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="nav-item dropdown">
                        <a href="#" class="nav-link">여행 준비</a>
                        <div class="dropdown-menu">
                            <div class="dropdown-content">
                                <div class="dropdown-column">
                                    <h4>체크인</h4>
                                    <ul>
                                        <li><a href="#">온라인 체크인</a></li>
                                        <li><a href="#">모바일 체크인</a></li>
                                        <li><a href="#">탑승권 출력</a></li>
                                        <li><a href="#">좌석 변경</a></li>
                                    </ul>
                                </div>
                                <div class="dropdown-column">
                                    <h4>여행 정보</h4>
                                    <ul>
                                        <li><a href="#">공항 정보</a></li>
                                        <li><a href="#">비자/여권 정보</a></li>
                                        <li><a href="#">수하물 규정</a></li>
                                        <li><a href="#">기내 반입 금지품목</a></li>
                                    </ul>
                                </div>
                                <div class="dropdown-column">
                                    <h4>특별 서비스</h4>
                                    <ul>
                                        <li><a href="#">특별 도움 서비스</a></li>
                                        <li><a href="#">반려동물 동반</a></li>
                                        <li><a href="#">임산부 서비스</a></li>
                                        <li><a href="#">어린이 혼자 여행</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="nav-item dropdown">
                        <a href="#" class="nav-link">스카이패스</a>
                        <div class="dropdown-menu">
                            <div class="dropdown-content">
                                <div class="dropdown-column">
                                    <h4>마일리지</h4>
                                    <ul>
                                        <li><a href="#">마일리지 조회</a></li>
                                        <li><a href="#">마일리지 적립</a></li>
                                        <li><a href="#">마일리지 사용</a></li>
                                        <li><a href="#">마일리지 양도</a></li>
                                    </ul>
                                </div>
                                <div class="dropdown-column">
                                    <h4>등급 혜택</h4>
                                    <ul>
                                        <li><a href="#">회원 등급 안내</a></li>
                                        <li><a href="#">등급별 혜택</a></li>
                                        <li><a href="#">라운지 이용</a></li>
                                        <li><a href="#">우선 탑승</a></li>
                                    </ul>
                                </div>
                                <div class="dropdown-column">
                                    <h4>파트너 혜택</h4>
                                    <ul>
                                        <li><a href="#">제휴 카드</a></li>
                                        <li><a href="#">호텔 파트너</a></li>
                                        <li><a href="#">렌터카 파트너</a></li>
                                        <li><a href="#">쇼핑몰 파트너</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </nav>
            <div class="header-actions">
                <div class="search-box">
                    <input type="text" placeholder="궁금한 것을 검색해 보세요!">
                    <button class="search-btn">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
                <button class="login-btn">로그인</button>
            </div>
        </div>
    </div>
</header> 