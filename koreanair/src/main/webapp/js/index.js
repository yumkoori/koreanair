document.addEventListener('DOMContentLoaded', function() {
    // 탭 기능 구현
    var tabBtns = document.querySelectorAll('.tab-btn');
    var bookingContents = document.querySelectorAll('.booking-content');

    tabBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            var targetTab = btn.getAttribute('data-tab');

            // 모든 탭 버튼에서 active 클래스 제거
            tabBtns.forEach(function(b) { b.classList.remove('active'); });
            // 클릭된 탭 버튼에 active 클래스 추가
            btn.classList.add('active');

            // 모든 콘텐츠에서 active 클래스 제거
            bookingContents.forEach(function(content) { content.classList.remove('active'); });
            // 해당 콘텐츠에 active 클래스 추가
            var targetElement = document.getElementById(targetTab);
            if (targetElement) {
                targetElement.classList.add('active');
            }
        });
    });

    // 슬라이더 기능 구현
    var slides = document.querySelectorAll('.slide');
    var dots = document.querySelectorAll('.dot');
    var prevBtn = document.querySelector('.prev');
    var nextBtn = document.querySelector('.next');
    
    var currentSlide = 0;
    var slideCount = slides.length;
    
    // 슬라이드 이동 함수
    function moveToSlide(index) {
        if (slides.length === 0) return;
        if (slides[currentSlide] && dots[currentSlide]) {
            slides[currentSlide].classList.remove('active');
            dots[currentSlide].classList.remove('active');
        }
        currentSlide = (index + slideCount) % slideCount;
        if (slides[currentSlide] && dots[currentSlide]) {
            slides[currentSlide].classList.add('active');
            dots[currentSlide].classList.add('active');
        }
    }
    
    function nextSlide() {
        if (slides.length > 0) {
            moveToSlide(currentSlide + 1);
        }
    }
    
    function prevSlide() {
        if (slides.length > 0) {
            moveToSlide(currentSlide - 1);
        }
    }
    
    if (prevBtn && nextBtn) {
        prevBtn.addEventListener('click', prevSlide);
        nextBtn.addEventListener('click', nextSlide);
    }
    
    dots.forEach(function(dot, index) {
        dot.addEventListener('click', function() {
            moveToSlide(index);
        });
    });
    
    if (slides.length > 0) {
        var slideInterval = setInterval(nextSlide, 5000);
        var bannerSlider = document.querySelector('.banner-slider');
        if (bannerSlider) {
            bannerSlider.addEventListener('mouseenter', function() {
                clearInterval(slideInterval);
            });
            bannerSlider.addEventListener('mouseleave', function() {
                slideInterval = setInterval(nextSlide, 5000);
            });
        }
    }

    // 출발지/도착지 교환 버튼 기능
    var swapBtns = document.querySelectorAll('.swap-route-btn');
    swapBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            var routeInputs = btn.parentElement.querySelectorAll('.airport-input');
            if (routeInputs.length === 2) {
                var departure = routeInputs[0];
                var arrival = routeInputs[1];
                
                var departureCodeEl = departure.querySelector('.airport-code');
                var departureNameEl = departure.querySelector('.airport-name');
                var arrivalCodeEl = arrival.querySelector('.airport-code');
                var arrivalNameEl = arrival.querySelector('.airport-name');

                if (departureCodeEl && departureNameEl && arrivalCodeEl && arrivalNameEl) {
                    var tempCode = departureCodeEl.textContent;
                    var tempName = departureNameEl.textContent;
                    
                    departureCodeEl.textContent = arrivalCodeEl.textContent;
                    departureNameEl.textContent = arrivalNameEl.textContent;
                    
                    arrivalCodeEl.textContent = tempCode;
                    arrivalNameEl.textContent = tempName;
                }
            }
        });
    });

    // 스크롤 이벤트 - 헤더 애니메이션
    var header = document.querySelector('header');
    var headerTop = document.querySelector('.header-top');
    var dropdownMenus = document.querySelectorAll('.dropdown-menu');
    var lastScrollTop = 0;
    
    function adjustDropdownPositions() {
        if (!header) return;
        var headerHeight = header.offsetHeight;
        var viewportWidth = window.innerWidth;
        
        dropdownMenus.forEach(function(menu) {
            menu.style.left = '';
            menu.style.right = '';
            menu.style.transform = '';
            menu.style.width = '';
            menu.style.maxWidth = '';
            menu.style.minWidth = '';
            
            if (viewportWidth <= 840) {
                menu.style.top = (headerHeight - 10) + 'px';
                menu.style.left = '50%';
                menu.style.transform = 'translateX(-50%) translateY(5px)';
                menu.style.width = '95vw';
                menu.style.maxWidth = '95vw';
                menu.style.minWidth = '320px';
            } else {
                menu.style.top = (headerHeight - 6) + 'px';
                menu.style.left = '50%';
                menu.style.transform = 'translateX(-50%) translateY(5px)';
                menu.style.width = 'clamp(320px, 90vw, 1100px)';
                menu.style.maxWidth = 'min(1100px, 95vw)';
                menu.style.minWidth = '320px';
            }
        });
        updateBridgePosition(headerHeight);
    }
    
    function updateBridgePosition(headerHeight) {
        if (!header) return;
        var viewportWidth = window.innerWidth;
        var bridgeTop = (viewportWidth <= 840) ? headerHeight - 10 : headerHeight - 16;
        
        var styleId = 'dynamic-bridge-style';
        var style = document.getElementById(styleId);
        if (!style) {
            style = document.createElement('style');
            style.id = styleId;
            document.head.appendChild(style);
        }
        style.textContent = '.nav-item::after { top: ' + bridgeTop + 'px !important; width: 100vw; }';
    }
    
    function checkDropdownBounds() {
        var viewportWidth = window.innerWidth;
        dropdownMenus.forEach(function(menu) {
            if (menu.style.visibility === 'visible' && menu.style.opacity === '1') {
                setTimeout(function() {
                    var menuRect = menu.getBoundingClientRect();
                    var margin = 10;
                    if (menuRect.left < margin) {
                        menu.style.left = margin + 'px';
                        menu.style.transform = 'translateY(5px)';
                    } else if (menuRect.right > viewportWidth - margin) {
                        menu.style.left = 'auto';
                        menu.style.right = margin + 'px';
                        menu.style.transform = 'translateY(5px)';
                    } else if (menu.style.left !== '50%') {
                        menu.style.left = '50%';
                        menu.style.right = 'auto';
                        menu.style.transform = 'translateX(-50%) translateY(5px)';
                    }
                }, 10);
            }
        });
    }

    adjustDropdownPositions();
    
    var resizeTimeout;
    window.addEventListener('resize', function() {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(function() {
            adjustDropdownPositions();
            checkDropdownBounds();
        }, 100);
    });
    
    window.addEventListener('scroll', function() {
        if (!header) return;
        var scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        var scrollDirection = (scrollTop > lastScrollTop) ? 'down' : 'up';
        
        if (scrollTop > 100) {
            header.classList.add('scrolled');
            if (scrollDirection === 'down' && scrollTop > 200 && headerTop) {
                header.style.transform = 'translateY(-' + headerTop.offsetHeight + 'px)';
            } else {
                header.style.transform = 'translateY(0)';
            }
        } else {
            header.classList.remove('scrolled');
            header.style.transform = 'translateY(0)';
        }
        adjustDropdownPositions();
        lastScrollTop = scrollTop <= 0 ? 0 : scrollTop;
    });

    var lookupForm = document.querySelector('#checkin .checkin-form');
    if (lookupForm) {
        lookupForm.addEventListener('submit', function(event) {
            event.preventDefault(); 

            var agreeCheckbox = lookupForm.querySelector('input[name="agreeInfo"]');
            if (agreeCheckbox && !agreeCheckbox.checked) {
                alert('[필수] 항목에 동의해주셔야 조회가 가능합니다.');
                return; 
            }
            
            var errorBox = document.querySelector('#bookingErrorBox');
            var errorMessageElement = document.querySelector('#bookingErrorMessage');
            if (errorBox) errorBox.classList.add('hidden');

            var formData = new FormData(this);

            fetch('lookup', {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('서버 응답에 문제가 발생했습니다.');
                }
                return response.json();
            })
            .then(function(data) {
                if (data.success) {
                    var contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2)) || "";
                    window.location.href = contextPath + '/' + data.redirectUrl;
                } else {
                    if (errorBox && errorMessageElement) {
                        errorMessageElement.textContent = data.error || '알 수 없는 오류가 발생했습니다.';
                        errorBox.classList.remove('hidden');
                    }
                }
            })
            .catch(function(error) {
                console.error('조회 중 오류 발생:', error);
                if (errorBox && errorMessageElement) {
                    errorMessageElement.textContent = '조회 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.';
                    errorBox.classList.remove('hidden');
                }
            });
        });
    }

    var tripTypeBtns = document.querySelectorAll('.trip-type-btn');
    tripTypeBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            tripTypeBtns.forEach(function(b) { b.classList.remove('active'); });
            btn.classList.add('active');
        });
    });
    
    var statusBtns = document.querySelectorAll('.status-btn');
    statusBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            statusBtns.forEach(function(b) { b.classList.remove('active'); });
            btn.classList.add('active');
        });
    });

    var navItems = document.querySelectorAll('.nav-item.dropdown');
    navItems.forEach(function(navItem) {
        var dropdownMenu = navItem.querySelector('.dropdown-menu');
        if (!dropdownMenu) return;

        var hoverTimeout;
        var isMouseOverDropdown = false; // 요청하신 대로 변수 유지
        
        function showMenu() {
            clearTimeout(hoverTimeout);
            dropdownMenu.style.opacity = '1';
            dropdownMenu.style.visibility = 'visible';
            dropdownMenu.style.transform = 'translateX(-50%) translateY(0)';
            checkDropdownBounds();
        }

        function hideMenu() {
             hoverTimeout = setTimeout(function() {
                if (!navItem.matches(':hover') && !dropdownMenu.matches(':hover')) {
                    dropdownMenu.style.opacity = '0';
                    dropdownMenu.style.visibility = 'hidden';
                    dropdownMenu.style.transform = 'translateX(-50%) translateY(-10px)';
                }
            }, 200);
        }

        navItem.addEventListener('mouseenter', showMenu);
        navItem.addEventListener('mouseleave', hideMenu);
        dropdownMenu.addEventListener('mouseenter', function() { clearTimeout(hoverTimeout); });
        dropdownMenu.addEventListener('mouseleave', hideMenu);

        var navLink = navItem.querySelector('.nav-link');
        if(navLink) {
            navLink.addEventListener('keydown', function(e) {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    var isVisible = dropdownMenu.style.visibility === 'visible';
                    navItems.forEach(function(item) {
                        var menu = item.querySelector('.dropdown-menu');
                        if (menu) {
                            menu.setAttribute('style', 'opacity:0; visibility:hidden;');
                        }
                    });
                    if (!isVisible) {
                        showMenu();
                        var firstLink = dropdownMenu.querySelector('a');
                        if (firstLink) {
                            firstLink.focus();
                        }
                    }
                }
            });
        }
    });

    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            navItems.forEach(function(navItem) {
                 var dropdownMenu = navItem.querySelector('.dropdown-menu');
                 if(dropdownMenu) {
                    dropdownMenu.style.opacity = '0';
                    dropdownMenu.style.visibility = 'hidden';
                 }
            });
        }
    });
    
    document.addEventListener('click', function(e) {
        var isClickInside = false;
        navItems.forEach(function(item) {
            if (item.contains(e.target)) {
                isClickInside = true;
            }
        });
        if (!isClickInside) {
             navItems.forEach(function(navItem) {
                 var dropdownMenu = navItem.querySelector('.dropdown-menu');
                 if(dropdownMenu) {
                    dropdownMenu.style.opacity = '0';
                    dropdownMenu.style.visibility = 'hidden';
                 }
            });
        }
    });
    
    if ('ontouchstart' in window) {
        navItems.forEach(function(navItem) {
            var navLink = navItem.querySelector('.nav-link');
            var dropdownMenu = navItem.querySelector('.dropdown-menu');
            if (!navLink || !dropdownMenu) return;

            navLink.addEventListener('click', function(e) {
                e.preventDefault();
                var isVisible = dropdownMenu.style.visibility === 'visible';
                
                navItems.forEach(function(item) {
                    if (item !== navItem) {
                        var menu = item.querySelector('.dropdown-menu');
                        if (menu) {
                            menu.setAttribute('style', 'opacity:0; visibility:hidden;');
                        }
                    }
                });

                if (isVisible) {
                    dropdownMenu.style.opacity = '0';
                    dropdownMenu.style.visibility = 'hidden';
                } else {
                    dropdownMenu.style.opacity = '1';
                    dropdownMenu.style.visibility = 'visible';
                    dropdownMenu.style.transform = 'translateX(-50%) translateY(0)';
                }
            }, { passive: false });
        });
    }

    window.addEventListener('pageshow', function(event) {
        if (event.persisted) {
            if (lookupForm) lookupForm.reset();
            var errorBox = document.querySelector('#bookingErrorBox');
            if (errorBox) errorBox.classList.add('hidden');
        }
    });

    function populateCheckinDateSelector() {
        var dateSelect = document.getElementById('checkinDepartureDate');
        if (!dateSelect) return;

        var today = new Date();
        dateSelect.innerHTML = '';
        for (var i = -1; i <= 2; i++) {
            var targetDate = new Date();
            targetDate.setDate(today.getDate() + i);
            var option = document.createElement('option');
            var year = targetDate.getFullYear();
            var month = String(targetDate.getMonth() + 1).padStart(2, '0');
            var day = String(targetDate.getDate()).padStart(2, '0');
            
            option.value = year + '-' + month + '-' + day;
            option.textContent = year + '년 ' + month + '월 ' + day + '일';

            if (i === 0) {
                option.selected = true;
            }
            dateSelect.appendChild(option);
        }
    }
    populateCheckinDateSelector();
});