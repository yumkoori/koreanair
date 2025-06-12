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

    // 항공편 검색 버튼 클릭 이벤트
    const searchFlightBtn = document.querySelector('#flight .search-flights-btn');
    if (searchFlightBtn) {
        searchFlightBtn.addEventListener('click', function(e) {
            e.preventDefault();
            
            // 검색 조건 수집
            const departure = document.querySelector('#flight .departure .airport-name').textContent || '서울';
            const arrival = document.querySelector('#flight .arrival .airport-name').textContent || '도착지';
            const departureDate = document.querySelector('#flight .date-input input[type="date"]').value || '';
            const passengers = document.querySelector('#flight .passenger-input select').value || '성인 1명';
            const seatClass = document.querySelector('#flight .class-input select').value || '일반석';
            
            // URL 파라미터 생성
            const params = new URLSearchParams({
                departure: departure,
                arrival: arrival,
                departureDate: departureDate,
                passengers: passengers,
                seatClass: seatClass
            });
            
            // search.jsp로 이동
            window.location.href = `${window.contextPath || ''}/views/search/search.jsp?${params.toString()}`;
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



    // 출발지 검색 자동완성
    document.getElementById("departure-search").addEventListener("input", function() {
        console.log(window.contextPath);
        
        let keyword = this.value;
        if (keyword.length < 1) return;

        fetch(window.contextPath + "/airportSearch.do?keyword=" + encodeURIComponent(keyword))
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                let resultDiv = document.getElementById("departure-results");
                resultDiv.innerHTML = ""; // 초기화
                data.forEach(city => {
                    let div = document.createElement("div");
                    div.textContent = city;
                    div.classList.add("dropdown-result-item");

                    div.addEventListener("click", function() {
                        document.querySelector('.departure .airport-code').textContent = "";
                        document.querySelector('.departure .airport-name').textContent = city;
                        document.getElementById('departure-dropdown').style.display = 'none';
                    });

                    resultDiv.appendChild(div);
                });
            })
            .catch(error => {
                console.error("Fetch error:", error);
            });
    });

    // 도착지 검색 자동완성
    document.getElementById("arrival-search").addEventListener("input", function() {
        console.log(window.contextPath);
        
        let keyword = this.value;
        if (keyword.length < 1) return;

        fetch(window.contextPath + "/airportSearch.do?keyword=" + encodeURIComponent(keyword))
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                let resultDiv = document.getElementById("arrival-results");
                resultDiv.innerHTML = ""; // 초기화
                data.forEach(city => {
                    let div = document.createElement("div");
                    div.textContent = city;
                    div.classList.add("dropdown-result-item");

                    div.addEventListener("click", function() {
                        document.querySelector('.arrival .airport-code').textContent = "";
                        document.querySelector('.arrival .airport-name').textContent = city;
                        document.getElementById('arrival-dropdown').style.display = 'none';
                    });

                    resultDiv.appendChild(div);
                });
            })
            .catch(error => {
                console.error("Fetch error:", error);
            });
    });



