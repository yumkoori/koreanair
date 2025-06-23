document.addEventListener('DOMContentLoaded', function() {
    // 탭 기능 구현
    const tabBtns = document.querySelectorAll('.tab-btn');
    const bookingContents = document.querySelectorAll('.booking-content');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const targetTab = btn.getAttribute('data-tab');
            
            // 모든 탭 버튼에서 active 클래스 제거
            tabBtns.forEach(b => b.classList.remove('active'));
            // 클릭된 탭 버튼에 active 클래스 추가
            btn.classList.add('active');
            
            // 모든 콘텐츠에서 active 클래스 제거
            bookingContents.forEach(content => content.classList.remove('active'));
            // 해당 콘텐츠에 active 클래스 추가
            document.getElementById(targetTab).classList.add('active');
        });
    });

    // 슬라이더 기능 구현
    const slides = document.querySelectorAll('.slide');
    const dots = document.querySelectorAll('.dot');
    const prevBtn = document.querySelector('.prev');
    const nextBtn = document.querySelector('.next');
    
    let currentSlide = 0;
    const slideCount = slides.length;
    
    // 슬라이드 이동 함수
    function moveToSlide(index) {
        // 현재 활성화된 슬라이드와 닷 비활성화
        slides[currentSlide].classList.remove('active');
        dots[currentSlide].classList.remove('active');
        
        // 새로운 슬라이드와 닷 활성화
        currentSlide = index;
        
        // 슬라이드 인덱스 조정 (순환)
        if (currentSlide < 0) {
            currentSlide = slideCount - 1;
        } else if (currentSlide >= slideCount) {
            currentSlide = 0;
        }
        
        slides[currentSlide].classList.add('active');
        dots[currentSlide].classList.add('active');
    }
    
    // 다음 슬라이드로 이동
    function nextSlide() {
        moveToSlide(currentSlide + 1);
    }
    
    // 이전 슬라이드로 이동
    function prevSlide() {
        moveToSlide(currentSlide - 1);
    }
    
    // 이벤트 리스너 추가
    prevBtn.addEventListener('click', prevSlide);
    nextBtn.addEventListener('click', nextSlide);
    
    // 닷 클릭 이벤트
    dots.forEach((dot, index) => {
        dot.addEventListener('click', () => {
            moveToSlide(index);
        });
    });
    
    // 자동 슬라이드 기능
    let slideInterval = setInterval(nextSlide, 5000);
    
    // 슬라이드에 마우스를 올리면 자동 슬라이드 멈춤
    const bannerSlider = document.querySelector('.banner-slider');
    
    bannerSlider.addEventListener('mouseenter', () => {
        clearInterval(slideInterval);
    });
    
    // 슬라이드에서 마우스가 나가면 자동 슬라이드 재시작
    bannerSlider.addEventListener('mouseleave', () => {
        slideInterval = setInterval(nextSlide, 5000);
    });

    // 출발지/도착지 교환 버튼 기능
    const swapBtns = document.querySelectorAll('.swap-route-btn');
    swapBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const routeInputs = btn.parentElement.querySelectorAll('.airport-input');
            if (routeInputs.length === 2) {
                const departure = routeInputs[0];
                const arrival = routeInputs[1];
                
                // 코드와 이름 교환
                const tempCode = departure.querySelector('.airport-code').textContent;
                const tempName = departure.querySelector('.airport-name').textContent;
                
                departure.querySelector('.airport-code').textContent = arrival.querySelector('.airport-code').textContent;
                departure.querySelector('.airport-name').textContent = arrival.querySelector('.airport-name').textContent;
                
                arrival.querySelector('.airport-code').textContent = tempCode;
                arrival.querySelector('.airport-name').textContent = tempName;
            }
        });
    });

    // 스크롤 이벤트 - 헤더 애니메이션
    const header = document.querySelector('header');
    const headerTop = document.querySelector('.header-top');
    const dropdownMenus = document.querySelectorAll('.dropdown-menu');
    let lastScrollTop = 0;
    let scrollDirection = 'none';
    
    // 드롭다운 메뉴의 위치를 헤더 높이에 맞게 조정하는 함수
    function adjustDropdownPositions() {
        const headerHeight = header.offsetHeight;
        const viewportWidth = window.innerWidth;
        
        // 스크롤 위치에 따라 드롭다운 메뉴 위치 조정
        dropdownMenus.forEach(menu => {
            // 기본 스타일 초기화
            menu.style.left = '';
            menu.style.right = '';
            menu.style.transform = '';
            menu.style.width = '';
            menu.style.maxWidth = '';
            menu.style.minWidth = '';
            
            if (viewportWidth <= 840) {
                // 태블릿 및 작은 화면에서 (400px 조건 제거하고 840px로 통합)
                menu.style.top = (headerHeight - 10) + 'px';
                menu.style.left = '50%';
                menu.style.transform = 'translateX(-50%) translateY(5px)';
                menu.style.width = '95vw';
                menu.style.maxWidth = '95vw';
                menu.style.minWidth = '320px';
            } else if (viewportWidth <= 900) {
                // 태블릿 화면에서
                menu.style.top = (headerHeight - 6) + 'px';
                menu.style.left = '50%';
                menu.style.transform = 'translateX(-50%) translateY(5px)';
                menu.style.width = '90vw';
                menu.style.maxWidth = '90vw';
                menu.style.minWidth = '320px';
            } else {
                // 데스크톱 화면에서
                menu.style.top = (headerHeight - 6) + 'px';
                menu.style.left = '50%';
                menu.style.transform = 'translateX(-50%) translateY(5px)';
                menu.style.width = 'clamp(320px, 90vw, 1100px)';
                menu.style.maxWidth = 'min(1100px, 95vw)';
                menu.style.minWidth = '320px';
            }
        });
        
        // 브릿지 영역도 함께 조정
        updateBridgePosition(headerHeight);
    }
    
    // 브릿지 위치 업데이트 함수 분리
    function updateBridgePosition(headerHeight) {
        const viewportWidth = window.innerWidth;
        let bridgeTop;
        
        if (viewportWidth <= 840) {
            bridgeTop = headerHeight - 10;
        } else if (viewportWidth <= 900) {
            bridgeTop = headerHeight - 6;
        } else {
            bridgeTop = headerHeight - 16;
        }
        
        const style = document.createElement('style');
        style.textContent = `
            .nav-item::after {
                top: ${bridgeTop}px !important;
                width: 100vw;
            }
        `;
        
        // 기존 스타일 제거 후 새로운 스타일 추가
        const existingStyle = document.querySelector('#dynamic-bridge-style');
        if (existingStyle) {
            existingStyle.remove();
        }
        style.id = 'dynamic-bridge-style';
        document.head.appendChild(style);
    }
    
    // 드롭다운 메뉴가 화면 밖으로 나가지 않도록 실시간 체크하는 함수
    function checkDropdownBounds() {
        const viewportWidth = window.innerWidth;
        
        dropdownMenus.forEach(menu => {
            if (menu.style.visibility === 'visible' && menu.style.opacity === '1') {
                // 메뉴가 보이는 상태일 때만 체크
                setTimeout(() => {
                    const menuRect = menu.getBoundingClientRect();
                    const margin = 10;
                    
                    if (menuRect.left < margin) {
                        // 왼쪽으로 잘릴 때
                        menu.style.left = margin + 'px';
                        menu.style.transform = 'translateY(5px)';
                    } else if (menuRect.right > viewportWidth - margin) {
                        // 오른쪽으로 잘릴 때
                        menu.style.left = 'auto';
                        menu.style.right = margin + 'px';
                        menu.style.transform = 'translateY(5px)';
                    } else if (menu.style.left !== '50%') {
                        // 정상 범위에 있으면 중앙 정렬로 복원
                        menu.style.left = '50%';
                        menu.style.right = 'auto';
                        menu.style.transform = 'translateX(-50%) translateY(5px)';
                    }
                }, 10); // 약간의 지연을 두어 렌더링 완료 후 체크
            }
        });
    }
    
    // 초기 위치 설정
    adjustDropdownPositions();
    
    // 창 크기 변경 시 위치 조정 - 디바운스 적용
    let resizeTimeout;
    window.addEventListener('resize', () => {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(() => {
            adjustDropdownPositions();
            // 현재 열려있는 드롭다운이 있다면 위치 재조정
            checkDropdownBounds();
        }, 100); // 100ms 디바운스
    });
    
    // 추가로 실시간 체크를 위한 이벤트
    window.addEventListener('resize', () => {
        // 즉시 실행 (디바운스와 별도)
        checkDropdownBounds();
    });
    
    window.addEventListener('scroll', function() {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        
        // 스크롤 방향 감지
        if (scrollTop > lastScrollTop) {
            scrollDirection = 'down';
        } else {
            scrollDirection = 'up';
        }
        
        // 스크롤 위치에 따른 헤더 스타일 변경
        if (scrollTop > 100) {
            header.classList.add('scrolled');
            
            // 스크롤 다운 시 상단 메뉴 숨기기
            if (scrollDirection === 'down' && scrollTop > 200) {
                headerTop.style.transform = 'translateY(-100%)';
                headerTop.style.opacity = '0';
                header.style.transform = 'translateY(-' + headerTop.offsetHeight + 'px)';
            } 
            // 스크롤 업 시 상단 메뉴 다시 보이기
            else if (scrollDirection === 'up') {
                headerTop.style.transform = 'translateY(0)';
                headerTop.style.opacity = '1';
                header.style.transform = 'translateY(0)';
            }
        } else {
            header.classList.remove('scrolled');
            headerTop.style.transform = 'translateY(0)';
            headerTop.style.opacity = '1';
            header.style.transform = 'translateY(0)';
        }
        
        // 헤더 상태 변화에 따라 드롭다운 메뉴 위치 조정
        adjustDropdownPositions();
        
        lastScrollTop = scrollTop <= 0 ? 0 : scrollTop; // iOS 바운스 효과 방지
    });

    // 폼 유효성 검사
    const bookingForms = document.querySelectorAll('.booking-form');
    
    bookingForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            let isValid = true;
            const inputs = this.querySelectorAll('input[type="text"], input[type="date"]');
            
            inputs.forEach(input => {
                if (input.value.trim() === '') {
                    isValid = false;
                    input.classList.add('error');
                } else {
                    input.classList.remove('error');
                }
            });
            
            if (isValid) {
                // 실제 제출 코드 (실제 구현 시 서버로 전송)
                alert('항공권 검색을 시작합니다.');
                // form.submit();
            } else {
                alert('모든 필수 항목을 입력해주세요.');
            }
        });
    });

    // 여행 타입 버튼 기능
    const tripTypeBtns = document.querySelectorAll('.trip-type-btn');
    tripTypeBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            tripTypeBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
        });
    });
    
    // 상태 버튼 기능
    const statusBtns = document.querySelectorAll('.status-btn');
    statusBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            statusBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
        });
    });

    // 드롭다운 메뉴 기능
    const navItems = document.querySelectorAll('.nav-item.dropdown');
    
    navItems.forEach(navItem => {
        const navLink = navItem.querySelector('.nav-link');
        const dropdownMenu = navItem.querySelector('.dropdown-menu');
        let hoverTimeout;
        let isMouseOverDropdown = false;
        let isMouseOverNavItem = false;
        
        // 마우스 호버 이벤트 - 네비게이션 아이템
        navItem.addEventListener('mouseenter', () => {
            clearTimeout(hoverTimeout);
            isMouseOverNavItem = true;
            dropdownMenu.style.opacity = '1';
            dropdownMenu.style.visibility = 'visible';
            dropdownMenu.style.transform = 'translateX(-50%) translateY(0)';
            
            // 메뉴가 표시된 후 위치 체크
            checkDropdownBounds();
        });
        
        navItem.addEventListener('mouseleave', () => {
            isMouseOverNavItem = false;
            // 드롭다운 메뉴에 마우스가 없을 때만 숨김 처리
            if (!isMouseOverDropdown) {
                hoverTimeout = setTimeout(() => {
                    if (!isMouseOverNavItem && !isMouseOverDropdown) {
                        dropdownMenu.style.opacity = '0';
                        dropdownMenu.style.visibility = 'hidden';
                        dropdownMenu.style.transform = 'translateX(-50%) translateY(-10px)';
                        
                        // 메뉴가 사라질 때 위치 초기화
                        setTimeout(() => {
                            if (dropdownMenu.style.visibility === 'hidden') {
                                dropdownMenu.style.left = '50%';
                                dropdownMenu.style.right = 'auto';
                                dropdownMenu.style.transform = 'translateX(-50%) translateY(5px)';
                            }
                        }, 150); // 애니메이션 완료 후 초기화
                    }
                }, 200); // 지연 시간을 200ms로 증가
            }
        });
        
        // 드롭다운 메뉴 자체에 대한 마우스 이벤트
        dropdownMenu.addEventListener('mouseenter', () => {
            clearTimeout(hoverTimeout);
            isMouseOverDropdown = true;
        });
        
        dropdownMenu.addEventListener('mouseleave', () => {
            isMouseOverDropdown = false;
            // 네비게이션 아이템에 마우스가 없을 때만 숨김 처리
            if (!isMouseOverNavItem) {
                hoverTimeout = setTimeout(() => {
                    if (!isMouseOverNavItem && !isMouseOverDropdown) {
                        dropdownMenu.style.opacity = '0';
                        dropdownMenu.style.visibility = 'hidden';
                        dropdownMenu.style.transform = 'translateX(-50%) translateY(-10px)';
                        
                        // 메뉴가 사라질 때 위치 초기화
                        setTimeout(() => {
                            if (dropdownMenu.style.visibility === 'hidden') {
                                dropdownMenu.style.left = '50%';
                                dropdownMenu.style.right = 'auto';
                                dropdownMenu.style.transform = 'translateX(-50%) translateY(5px)';
                            }
                        }, 150); // 애니메이션 완료 후 초기화
                    }
                }, 100);
            }
        });
        
        // 키보드 접근성
        navLink.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                const isVisible = dropdownMenu.style.visibility === 'visible';
                
                if (isVisible) {
                    dropdownMenu.style.opacity = '0';
                    dropdownMenu.style.visibility = 'hidden';
                } else {
                    // 다른 드롭다운 메뉴 닫기
                    navItems.forEach(item => {
                        if (item !== navItem) {
                            const otherMenu = item.querySelector('.dropdown-menu');
                            otherMenu.style.opacity = '0';
                            otherMenu.style.visibility = 'hidden';
                        }
                    });
                    
                    dropdownMenu.style.opacity = '1';
                    dropdownMenu.style.visibility = 'visible';
                    dropdownMenu.style.transform = 'translateX(-50%) translateY(0)';
                    
                    // 첫 번째 링크에 포커스
                    const firstLink = dropdownMenu.querySelector('a');
                    if (firstLink) {
                        firstLink.focus();
                    }
                }
            }
        });
        
        // ESC 키로 드롭다운 닫기
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                dropdownMenu.style.opacity = '0';
                dropdownMenu.style.visibility = 'hidden';
                navLink.focus();
            }
        });
        
        // 드롭다운 외부 클릭 시 닫기
        document.addEventListener('click', (e) => {
            if (!navItem.contains(e.target)) {
                dropdownMenu.style.opacity = '0';
                dropdownMenu.style.visibility = 'hidden';
            }
        });
    });
    
    // 모바일에서 터치 이벤트 처리
    if ('ontouchstart' in window) {
        navItems.forEach(navItem => {
            const navLink = navItem.querySelector('.nav-link');
            const dropdownMenu = navItem.querySelector('.dropdown-menu');
            
            navLink.addEventListener('touchstart', (e) => {
                e.preventDefault();
                
                // 다른 드롭다운 메뉴 닫기
                navItems.forEach(item => {
                    if (item !== navItem) {
                        const otherMenu = item.querySelector('.dropdown-menu');
                        otherMenu.style.opacity = '0';
                        otherMenu.style.visibility = 'hidden';
                    }
                });
                
                // 현재 드롭다운 토글
                const isVisible = dropdownMenu.style.visibility === 'visible';
                if (isVisible) {
                    dropdownMenu.style.opacity = '0';
                    dropdownMenu.style.visibility = 'hidden';
                } else {
                    dropdownMenu.style.opacity = '1';
                    dropdownMenu.style.visibility = 'visible';
                    dropdownMenu.style.transform = 'translateX(-50%) translateY(0)';
                }
            });
        });
    }

    // 출발지/도착지 검색 기능
    const departureDiv = document.querySelector('.airport-input.departure');
    const arrivalDiv = document.querySelector('.airport-input.arrival');
    const departureDropdown = document.getElementById('departure-dropdown');
    const arrivalDropdown = document.getElementById('arrival-dropdown');
    const departureSearch = document.getElementById('departure-search');
    const arrivalSearch = document.getElementById('arrival-search');
    const departureResults = document.getElementById('departure-results');
    const arrivalResults = document.getElementById('arrival-results');
    const departureClose = document.getElementById('departure-close');
    const arrivalClose = document.getElementById('arrival-close');
    const departureAllRegions = document.getElementById('departure-all-regions');
    const arrivalAllRegions = document.getElementById('arrival-all-regions');



    // 출발지 검색 이벤트
    if (departureSearch) {

        // 검색창에 포커스될 때 테두리 색상 변경
        departureSearch.addEventListener('focus', function() {
            this.style.borderColor = '#0066cc';
        });
        departureSearch.addEventListener('blur', function() {
            this.style.borderColor = '#e0e0e0';
        });
    }

    // 도착지 검색 이벤트
    if (arrivalSearch) {
        arrivalSearch.addEventListener('input', function() {
            searchAirports(this.value, arrivalResults);
        });

        // 검색창에 포커스될 때 테두리 색상 변경
        arrivalSearch.addEventListener('focus', function() {
            this.style.borderColor = '#0066cc';
        });
        arrivalSearch.addEventListener('blur', function() {
            this.style.borderColor = '#e0e0e0';
        });
    }

    // X 버튼 클릭 이벤트
    if (departureClose) {
        departureClose.addEventListener('click', function(e) {
            e.stopPropagation();
            departureDropdown.style.display = 'none';
        });
    }
    
    if (arrivalClose) {
        arrivalClose.addEventListener('click', function(e) {
            e.stopPropagation();
            arrivalDropdown.style.display = 'none';
        });
    }



    // 드롭다운 위치 설정 함수
    function positionDropdown(triggerElement, dropdown) {
        const rect = triggerElement.getBoundingClientRect();
        const dropdownWidth = 350;
        const dropdownHeight = 500; // 최대 높이
        
        let top = rect.bottom + 5;
        let left = rect.left;
        
        // 화면 오른쪽 경계 체크
        if (left + dropdownWidth > window.innerWidth) {
            left = window.innerWidth - dropdownWidth - 10;
        }
        
        // 화면 왼쪽 경계 체크
        if (left < 10) {
            left = 10;
        }
        
        // 화면 하단 경계 체크 - 위쪽으로 표시
        if (top + dropdownHeight > window.innerHeight) {
            top = rect.top - dropdownHeight - 5;
            // 위쪽에도 공간이 부족하면 화면 중앙에 표시
            if (top < 10) {
                top = Math.max(10, (window.innerHeight - dropdownHeight) / 2);
            }
        }
        
        dropdown.style.top = top + 'px';
        dropdown.style.left = left + 'px';
        
        // 강제로 z-index 설정
        dropdown.style.zIndex = '2147483647';
        dropdown.style.position = 'fixed';
    }

    // 출발지 클릭 이벤트
    if (departureDiv) {
        departureDiv.addEventListener('click', function(e) {
            if (arrivalDropdown) arrivalDropdown.style.display = 'none';
            if (departureDropdown.style.display === 'none' || departureDropdown.style.display === '') {
                positionDropdown(departureDiv, departureDropdown);
                departureDropdown.style.display = 'block';
                if (departureSearch) {
                    setTimeout(() => departureSearch.focus(), 100);
                }
            } else {
                departureDropdown.style.display = 'none';
            }
            e.stopPropagation();
        });
    }

    // 도착지 클릭 이벤트
    if (arrivalDiv) {
        arrivalDiv.addEventListener('click', function(e) {
            if (departureDropdown) departureDropdown.style.display = 'none';
            if (arrivalDropdown.style.display === 'none' || arrivalDropdown.style.display === '') {
                positionDropdown(arrivalDiv, arrivalDropdown);
                arrivalDropdown.style.display = 'block';
                if (arrivalSearch) {
                    setTimeout(() => arrivalSearch.focus(), 100);
                }
            } else {
                arrivalDropdown.style.display = 'none';
            }
            e.stopPropagation();
        });
    }

    // 드롭다운 내부 클릭 시 이벤트 전파 방지
    if (departureDropdown) {
        departureDropdown.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    }
    
    if (arrivalDropdown) {
        arrivalDropdown.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    }

    // 바깥 클릭 시 모든 드롭다운 닫기
    document.addEventListener('click', function(e) {
        if (departureDiv && arrivalDiv && 
            !departureDiv.contains(e.target) && 
            !arrivalDiv.contains(e.target) &&
            !departureDropdown.contains(e.target) &&
            !arrivalDropdown.contains(e.target)) {
            if (departureDropdown) departureDropdown.style.display = 'none';
            if (arrivalDropdown) arrivalDropdown.style.display = 'none';
        }
    });

    // 달력 관련 변수들
    let currentDate = new Date();
    let selectedStartDate = null;
    let selectedEndDate = null;
    let isSelectingRange = false;
    let currentTripType = 'round'; // 기본값: 왕복

    // 달력 요소들
    const calendarPopup = document.getElementById('calendar-popup');
    const calendarOverlay = document.getElementById('calendar-overlay');
    const datePickerTrigger = document.getElementById('date-picker-trigger');
    const dateDisplay = document.getElementById('date-display');
    const dateLabel = document.getElementById('date-label');
    const calendarMonthYear = document.getElementById('calendar-month-year');
    const calendarDays = document.getElementById('calendar-days');
    const prevMonthBtn = document.getElementById('prev-month');
    const nextMonthBtn = document.getElementById('next-month');
    const calendarClearBtn = document.querySelector('.calendar-clear');
    const calendarApplyBtn = document.querySelector('.calendar-apply');

    // 날짜 포맷 함수
    function formatDate(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    // 날짜 표시 업데이트
    function updateDateDisplay() {
        if (currentTripType === 'oneway') {
            if (selectedStartDate) {
                dateDisplay.value = formatDate(selectedStartDate);
            } else {
                dateDisplay.value = '출발일 선택';
            }
        } else {
            if (selectedStartDate && selectedEndDate) {
                dateDisplay.value = `${formatDate(selectedStartDate)} ~ ${formatDate(selectedEndDate)}`;
            } else if (selectedStartDate) {
                dateDisplay.value = `${formatDate(selectedStartDate)} ~ 도착일을 선택하세요`;
            } else {
                dateDisplay.value = '출발일 ~ 도착일 선택';
            }
        }
    }

    // 달력 생성
    function generateCalendar(year, month) {
        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);
        const startDate = new Date(firstDay);
        startDate.setDate(startDate.getDate() - firstDay.getDay());
        
        const today = new Date();
        calendarDays.innerHTML = '';
        
        for (let i = 0; i < 42; i++) {
            const date = new Date(startDate);
            date.setDate(startDate.getDate() + i);
            
            const dayElement = document.createElement('div');
            dayElement.className = 'calendar-day';
            dayElement.textContent = date.getDate();
            dayElement.dataset.date = formatDate(date);
            
            // 다른 달의 날짜
            if (date.getMonth() !== month) {
                dayElement.classList.add('other-month');
            }
            
            // 오늘 날짜
            if (date.toDateString() === today.toDateString()) {
                dayElement.classList.add('today');
            }
            
            // 과거 날짜 비활성화
            if (date < today.setHours(0, 0, 0, 0)) {
                dayElement.style.color = '#ccc';
                dayElement.style.cursor = 'not-allowed';
            } else {
                // 선택된 날짜 스타일링
                if (selectedStartDate && date.toDateString() === selectedStartDate.toDateString()) {
                    dayElement.classList.add('start-date');
                }
                if (selectedEndDate && date.toDateString() === selectedEndDate.toDateString()) {
                    dayElement.classList.add('end-date');
                }
                
                // 범위 내 날짜 (왕복인 경우)
                if (currentTripType === 'round' && selectedStartDate && selectedEndDate) {
                    if (date > selectedStartDate && date < selectedEndDate) {
                        dayElement.classList.add('in-range');
                    }
                }
                
                // 클릭 이벤트
                dayElement.addEventListener('click', function() {
                    selectDate(new Date(date));
                });
            }
            
            calendarDays.appendChild(dayElement);
        }
    }

    // 날짜 선택 처리
    function selectDate(date) {
        if (currentTripType === 'oneway') {
            selectedStartDate = date;
            selectedEndDate = null;
            // 편도인 경우 날짜 선택 후 달력 자동 닫기
            setTimeout(() => {
                if (calendarPopup.classList.contains('show')) {
                    toggleCalendar();
                }
            }, 300);
        } else {
            if (!selectedStartDate || (selectedStartDate && selectedEndDate)) {
                // 첫 번째 날짜 선택 또는 재선택
                selectedStartDate = date;
                selectedEndDate = null;
                isSelectingRange = true;
                // 왕복인 경우 첫 번째 날짜 선택 후에는 달력을 열어둠
            } else if (selectedStartDate && !selectedEndDate) {
                // 두 번째 날짜 선택
                if (date >= selectedStartDate) {
                    selectedEndDate = date;
                } else {
                    selectedEndDate = selectedStartDate;
                    selectedStartDate = date;
                }
                isSelectingRange = false;
                // 왕복인 경우 두 번째 날짜 선택 후 달력 자동 닫기
                setTimeout(() => {
                    if (calendarPopup.classList.contains('show')) {
                        toggleCalendar();
                    }
                }, 300);
            }
        }
        
        updateDateDisplay();
        generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
    }

    // 달력 위치 계산
    function positionCalendar() {
        if (!datePickerTrigger || !calendarPopup) return;
        
        const triggerRect = datePickerTrigger.getBoundingClientRect();
        const calendarHeight = 400; // 대략적인 달력 높이
        const viewportHeight = window.innerHeight;
        const viewportWidth = window.innerWidth;
        
        // 기본적으로 트리거 아래에 표시
        let top = triggerRect.bottom + 5;
        let left = triggerRect.left;
        let width = Math.max(320, triggerRect.width);
        
        // 화면 아래로 넘어가는 경우 위에 표시
        if (top + calendarHeight > viewportHeight) {
            top = triggerRect.top - calendarHeight - 5;
        }
        
        // 화면 오른쪽으로 넘어가는 경우 조정
        if (left + width > viewportWidth) {
            left = viewportWidth - width - 10;
        }
        
        // 화면 왼쪽으로 넘어가는 경우 조정
        if (left < 10) {
            left = 10;
        }
        
        calendarPopup.style.top = top + 'px';
        calendarPopup.style.left = left + 'px';
        calendarPopup.style.width = width + 'px';
        calendarPopup.style.right = 'auto';
    }

    // 달력 표시/숨김
    function toggleCalendar() {
        const isShowing = calendarPopup.classList.contains('show');
        
        if (!isShowing) {
            // 달력 열기
            // 오버레이와 달력을 body에 직접 추가
            document.body.appendChild(calendarOverlay);
            document.body.appendChild(calendarPopup);
            
            // 오버레이 표시
            calendarOverlay.classList.add('show');
            calendarPopup.classList.add('show');
            
            // 강제로 최상단에 표시
            calendarOverlay.style.zIndex = '999999998';
            calendarOverlay.style.position = 'fixed';
            
            calendarPopup.style.zIndex = '999999999';
            calendarPopup.style.position = 'fixed';
            calendarPopup.style.transform = 'translateZ(0)';
            calendarPopup.style.willChange = 'transform';
            calendarPopup.style.isolation = 'isolate';
            calendarPopup.style.backfaceVisibility = 'hidden';
            calendarPopup.style.background = 'white';
            calendarPopup.style.opacity = '1';
            calendarPopup.style.visibility = 'visible';
            calendarPopup.style.pointerEvents = 'auto';
            
            positionCalendar();
            generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
            updateCalendarHeader();
        } else {
            // 달력 닫기
            calendarOverlay.classList.remove('show');
            calendarPopup.classList.remove('show');
            
            // 원래 위치로 되돌림
            const dateInput = document.querySelector('.date-input');
            if (dateInput) {
                if (calendarOverlay.parentNode === document.body) {
                    dateInput.appendChild(calendarOverlay);
                }
                if (calendarPopup.parentNode === document.body) {
                    dateInput.appendChild(calendarPopup);
                }
            }
        }
    }

    // 달력 헤더 업데이트
    function updateCalendarHeader() {
        const months = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];
        calendarMonthYear.textContent = `${currentDate.getFullYear()}년 ${months[currentDate.getMonth()]}`;
    }

    // 왕복/편도 버튼 클릭 이벤트
    const tripTypeButtons = document.querySelectorAll('.trip-type-btn');
    
    if (tripTypeButtons.length > 0) {
        tripTypeButtons.forEach(button => {
            button.addEventListener('click', function() {
                // 모든 버튼에서 active 클래스 제거
                tripTypeButtons.forEach(btn => btn.classList.remove('active'));
                
                // 클릭된 버튼에 active 클래스 추가
                this.classList.add('active');
                
                currentTripType = this.getAttribute('data-type');
                
                // 라벨 업데이트
                if (currentTripType === 'oneway') {
                    dateLabel.textContent = '출발일';
                    selectedEndDate = null; // 편도로 변경 시 도착일 초기화
                } else if (currentTripType === 'round') {
                    dateLabel.textContent = '출발일 ~ 도착일';
                } else if (currentTripType === 'multi') {
                    dateLabel.textContent = '출발일 ~ 도착일';
                }
                
                updateDateDisplay();
                if (calendarPopup.classList.contains('show')) {
                    generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
                }
            });
        });
    }

    // 이벤트 리스너들
    if (datePickerTrigger) {
        datePickerTrigger.addEventListener('click', toggleCalendar);
    }

    if (prevMonthBtn) {
        prevMonthBtn.addEventListener('click', function() {
            currentDate.setMonth(currentDate.getMonth() - 1);
            generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
            updateCalendarHeader();
        });
    }

    if (nextMonthBtn) {
        nextMonthBtn.addEventListener('click', function() {
            currentDate.setMonth(currentDate.getMonth() + 1);
            generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
            updateCalendarHeader();
        });
    }

    if (calendarClearBtn) {
        calendarClearBtn.addEventListener('click', function() {
            selectedStartDate = null;
            selectedEndDate = null;
            updateDateDisplay();
            generateCalendar(currentDate.getFullYear(), currentDate.getMonth());
        });
    }

    if (calendarApplyBtn) {
        calendarApplyBtn.addEventListener('click', function() {
            if (calendarPopup.classList.contains('show')) {
                toggleCalendar();
            }
        });
    }

    // 달력 외부 클릭 시 닫기
    document.addEventListener('click', function(e) {
        if (!datePickerTrigger.contains(e.target) && !calendarPopup.contains(e.target)) {
            if (calendarPopup.classList.contains('show')) {
                // 왕복 모드에서는 두 날짜가 모두 선택되었을 때만 외부 클릭으로 닫기
                if (currentTripType === 'oneway' || (currentTripType === 'round' && selectedStartDate && selectedEndDate)) {
                    toggleCalendar();
                }
                // 편도 모드이거나 왕복 모드에서 두 날짜가 모두 선택된 경우에만 닫기
            }
        }
    });

    // 오버레이 클릭 시 달력 닫기
    if (calendarOverlay) {
        calendarOverlay.addEventListener('click', function() {
            if (calendarPopup.classList.contains('show')) {
                // 왕복 모드에서는 두 날짜가 모두 선택되었을 때만 오버레이 클릭으로 닫기
                if (currentTripType === 'oneway' || (currentTripType === 'round' && selectedStartDate && selectedEndDate)) {
                    toggleCalendar();
                }
            }
        });
    }

    // 스크롤 및 리사이즈 시 달력 위치 재조정
    window.addEventListener('scroll', function() {
        if (calendarPopup.classList.contains('show')) {
            positionCalendar();
        }
    });

    window.addEventListener('resize', function() {
        if (calendarPopup.classList.contains('show')) {
            positionCalendar();
        }
    });

    // 초기 날짜 설정
    selectedStartDate = new Date(2025, 6, 15); // 2025-07-15
    selectedEndDate = new Date(2025, 6, 16);   // 2025-07-16
    
    // 페이지 로드 시 초기 상태 설정
    const activeButton = document.querySelector('.trip-type-btn.active');
    if (activeButton) {
        const tripType = activeButton.getAttribute('data-type');
        currentTripType = tripType;
        if (tripType === 'oneway') {
            selectedEndDate = null; // 편도인 경우 도착일 제거
            if (dateLabel) {
                dateLabel.textContent = '출발일';
            }
        }
    }
    
    updateDateDisplay();

    // 항공편 검색 폼 submit 이벤트
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
                        try {
                // 기본값 설정
                const departure = 'CJU';
                const arrival = 'GMP'; 
                const departureDate = '2025-07-15';
                const returnDate = '2025-07-16';
                const passengers = '성인 1명';
                const seatClass = '일반석';
                const tripType = 'round';
                
                // URL 직접 생성하여 이동
                const params = new URLSearchParams({
                    departure: departure,
                    arrival: arrival,
                    departureDate: departureDate,
                    returnDate: returnDate,
                    passengers: passengers,
                    seatClass: seatClass,
                    tripType: tripType
                });
                
                const url = 'flightSearch.do?' + params.toString();
                window.location.href = url;
            } catch (error) {
                console.error('폼 제출 중 오류 발생:', error);
                alert('검색 중 오류가 발생했습니다. 페이지를 새로고침 후 다시 시도해주세요.');
            }
        });
    }

    // 스크롤 시 드롭다운 위치 재조정
    window.addEventListener('scroll', function() {
        if (departureDropdown && departureDropdown.style.display === 'block') {
            positionDropdown(departureDiv, departureDropdown);
        }
        if (arrivalDropdown && arrivalDropdown.style.display === 'block') {
            positionDropdown(arrivalDiv, arrivalDropdown);
        }
    });

    // 윈도우 리사이즈 시 드롭다운 위치 재조정
    window.addEventListener('resize', function() {
        if (departureDropdown && departureDropdown.style.display === 'block') {
            positionDropdown(departureDiv, departureDropdown);
        }
        if (arrivalDropdown && arrivalDropdown.style.display === 'block') {
            positionDropdown(arrivalDiv, arrivalDropdown);
        }
    });
}); 


document.querySelector('#departure-search').addEventListener('input', function () {
    const keyword = this.value.trim();
    if (keyword.length === 0) return;

    fetch(`${window.contextPath}/autocomplete.do?keyword=${encodeURIComponent(keyword)}`)
        .then(response => response.json())
        .then(data => {
            const resultsContainer = document.getElementById('departure-results');
            resultsContainer.innerHTML = '';
            data.forEach(item => {
                const div = document.createElement('div');
                div.className = 'dropdown-item';
                div.textContent = `${item.airportId} - ${item.airportName}`;
                div.addEventListener('click', () => {
                    document.querySelector('.departure .airport-code').textContent = item.airportId;
                    document.querySelector('.departure .airport-name').textContent = item.airportName;
                    resultsContainer.innerHTML = '';
                });
                resultsContainer.appendChild(div);
            });
        })
        .catch(console.error);
});


document.querySelector('#arrival-search').addEventListener('input', function () {
    const keyword = this.value.trim();
    if (keyword.length === 0) return;

    fetch(`${window.contextPath}/autocomplete.do?keyword=${encodeURIComponent(keyword)}`)
        .then(response => response.json())
        .then(data => {
            const resultsContainer = document.getElementById('arrival-results');
            resultsContainer.innerHTML = '';
            data.forEach(item => {
                const div = document.createElement('div');
                div.className = 'dropdown-item';
                div.textContent = `${item.airportId} - ${item.airportName}`;
                div.addEventListener('click', () => {
                    document.querySelector('.arrival .airport-code').textContent = item.airportId;
                    document.querySelector('.arrival .airport-name').textContent = item.airportName;
                    resultsContainer.innerHTML = '';
                });
                resultsContainer.appendChild(div);
            });
        })
        .catch(console.error);
});