<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koreanair.model.dto.User" %>
<header class="airline-header">
    <div class="airline-header-top">
        <div class="airline-container">
            <div class="airline-top-nav-left">
                <a href="#" class="airline-top-link">
                    <i class="fas fa-gift"></i>
                    이벤트
                </a>
                <a href="#" class="airline-top-link">
                    <i class="fas fa-question-circle"></i>
                    자주 묻는 질문
                </a>
            </div>
            <div class="airline-top-nav-right">
                <div class="airline-language-selector">
                    <img src="https://flagcdn.com/w20/kr.png" alt="한국" width="20">
                    <span>대한민국 - 한국어</span>
                    <i class="fas fa-chevron-down"></i>
                </div>
                <%
                    // 세션에서 사용자 정보 확인
                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                %>
                    <span class="airline-top-link airline-user-info">
                        <i class="fas fa-user"></i>
                        <%= user.getKoreanName() %>님
                    </span>
                    <a href="logout.do" class="airline-top-link">
                        <i class="fas fa-sign-out-alt"></i>
                        로그아웃
                    </a>
                <%
                    } else {
                %>
                    <a href="registerForm.do" class="airline-top-link">
                        <i class="fas fa-user-plus"></i>
                        회원가입
                    </a>
                <%
                    }
                %>
            </div>
        </div>
    </div>
    <div class="airline-header-main">
        <div class="airline-container">
            <div class="airline-logo">
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
            <nav class="airline-main-nav">
                <ul class="airline-list">
                    <li class="airline-nav-item dropdown">
                        <a href="#" class="airline-nav-link">예약</a>
                        <div class="airline-dropdown-menu">
                            <div class="airline-dropdown-content">
                                <div class="airline-dropdown-column">
                                    <h4>항공권 예약</h4>
                                    <ul class="airline-list">
                                        <li><a href="#" class="airline-link">항공편 검색</a></li>
                                        <li><a href="#" class="airline-link">다구간 예약</a></li>
                                        <li><a href="#" class="airline-link">그룹 예약</a></li>
                                        <li><a href="#" class="airline-link">특가 항공권</a></li>
                                    </ul>
                                </div>
                                <div class="airline-dropdown-column">
                                    <h4>예약 관리</h4>
                                    <ul class="airline-list">
                                        <li><a href="#" class="airline-link">예약 조회/변경</a></li>
                                        <li><a href="#" class="airline-link">좌석 선택</a></li>
                                        <li><a href="#" class="airline-link">기내식 선택</a></li>
                                        <li><a href="#" class="airline-link">수하물 추가</a></li>
                                    </ul>
                                </div>
                                <div class="airline-dropdown-column">
                                    <h4>부가 서비스</h4>
                                    <ul class="airline-list">
                                        <li><a href="#" class="airline-link">호텔 예약</a></li>
                                        <li><a href="#" class="airline-link">렌터카 예약</a></li>
                                        <li><a href="#" class="airline-link">여행자 보험</a></li>
                                        <li><a href="#" class="airline-link">공항 라운지</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="airline-nav-item dropdown">
                        <a href="#" class="airline-nav-link">여행 준비</a>
                        <div class="airline-dropdown-menu">
                            <div class="airline-dropdown-content">
                                <div class="airline-dropdown-column">
                                    <h4>체크인</h4>
                                    <ul class="airline-list">
                                        <li><a href="#" class="airline-link">온라인 체크인</a></li>
                                        <li><a href="#" class="airline-link">모바일 체크인</a></li>
                                        <li><a href="#" class="airline-link">탑승권 출력</a></li>
                                        <li><a href="#" class="airline-link">좌석 변경</a></li>
                                    </ul>
                                </div>
                                <div class="airline-dropdown-column">
                                    <h4>여행 정보</h4>
                                    <ul class="airline-list">
                                        <li><a href="#" class="airline-link">공항 정보</a></li>
                                        <li><a href="#" class="airline-link">비자/여권 정보</a></li>
                                        <li><a href="#" class="airline-link">수하물 규정</a></li>
                                        <li><a href="#" class="airline-link">기내 반입 금지품목</a></li>
                                    </ul>
                                </div>
                                <div class="airline-dropdown-column">
                                    <h4>특별 서비스</h4>
                                    <ul class="airline-list">
                                        <li><a href="#" class="airline-link">특별 도움 서비스</a></li>
                                        <li><a href="#" class="airline-link">반려동물 동반</a></li>
                                        <li><a href="#" class="airline-link">임산부 서비스</a></li>
                                        <li><a href="#" class="airline-link">어린이 혼자 여행</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="airline-nav-item dropdown">
                        <a href="#" class="airline-nav-link">스카이패스</a>
                        <div class="airline-dropdown-menu">
                            <div class="airline-dropdown-content">
                                <div class="airline-dropdown-column">
                                    <h4>마일리지</h4>
                                    <ul class="airline-list">
                                        <li><a href="#" class="airline-link">마일리지 조회</a></li>
                                        <li><a href="#" class="airline-link">마일리지 적립</a></li>
                                        <li><a href="#" class="airline-link">마일리지 사용</a></li>
                                        <li><a href="#" class="airline-link">마일리지 양도</a></li>
                                    </ul>
                                </div>
                                <div class="airline-dropdown-column">
                                    <h4>등급 혜택</h4>
                                    <ul class="airline-list">
                                        <li><a href="#" class="airline-link">회원 등급 안내</a></li>
                                        <li><a href="#" class="airline-link">등급별 혜택</a></li>
                                        <li><a href="#" class="airline-link">라운지 이용</a></li>
                                        <li><a href="#" class="airline-link">우선 탑승</a></li>
                                    </ul>
                                </div>
                                <div class="airline-dropdown-column">
                                    <h4>파트너 혜택</h4>
                                    <ul class="airline-list">
                                        <li><a href="#" class="airline-link">제휴 카드</a></li>
                                        <li><a href="#" class="airline-link">호텔 파트너</a></li>
                                        <li><a href="#" class="airline-link">렌터카 파트너</a></li>
                                        <li><a href="#" class="airline-link">쇼핑몰 파트너</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </nav>
            <div class="airline-header-actions">
                <div class="airline-search-box">
                    <input type="text" class="airline-search-input" placeholder="궁금한 것을 검색해 보세요!">
                    <button class="airline-search-btn">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
                <%
                    if (user != null) {
                %>
                    <a href="dashboard.do" class="airline-login-btn">마이페이지</a>
                <%
                    } else {
                %>
                    <a href="loginForm.do" class="airline-login-btn" >로그인</a>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</header> 