document.addEventListener('DOMContentLoaded', function() {
    // 여정 카드 헤더를 클릭했을 때 최소화/확대 기능을 추가합니다.
    const journeyCard = document.querySelector('.journey-card-outer');
    const journeyHeader = document.querySelector('.journey-card-header');

    if (journeyHeader && journeyCard) {
        journeyHeader.addEventListener('click', function() {
            // is-collapsed 라는 클래스를 추가하거나 제거하여 상태를 변경합니다.
            journeyCard.classList.toggle('is-collapsed');
        });
    }

    // 탑승객 정보, 여행 변경 섹션의 토글 기능도 추가합니다.
    const sectionHeaders = document.querySelectorAll('.section-card .section-title');
    sectionHeaders.forEach(header => {
        header.addEventListener('click', function(e) {
            // 제목 영역의 어느 곳을 클릭해도 동작하도록 합니다.
            if (e.target.closest('.section-title')) {
                const sectionCard = header.closest('.section-card');
                if (sectionCard) {
                    sectionCard.classList.toggle('is-collapsed');
                }
            }
        });
    });

    const modal = document.getElementById('servicesModal');
    const openBtn = document.getElementById('openServicesModalBtn');
    const closeBtn = document.getElementById('closeServicesModalBtn');
    const footerCloseBtn = document.getElementById('footerCloseServicesModalBtn');

    function openModal() {
        if (modal) {
            modal.style.display = 'flex';
        }
    }

    function closeModal() {
        if (modal) {
            modal.style.display = 'none';
        }
    }

    if (openBtn) {
        openBtn.addEventListener('click', function() {
            console.log('버튼 클릭됨');
            if (modal) {
                modal.style.display = 'flex';
                console.log('모달 display:', modal.style.display);
            } else {
                console.log('모달 요소 없음');
            }
        });
    }

    // 'X' 버튼 클릭 시 모달 닫기
    if (closeBtn) {
        closeBtn.addEventListener('click', closeModal);
    }

    // '닫기' 버튼 클릭 시 모달 닫기
    if (footerCloseBtn) {
        footerCloseBtn.addEventListener('click', closeModal);
    }

    // 모달의 반투명 배경 클릭 시 모달 닫기
    if (modal) {
        modal.addEventListener('click', function(event) {
            // 클릭된 요소가 모달 배경(modal-overlay) 자체일 때만 닫기
            if (event.target === modal) {
                closeModal();
            }
        });
    }
});

// 비회원 조회 시 로그인이 필요한 기능에 대한 알람을 표시하는 함수
function showLoginRequiredAlert(functionName) {
    const message = functionName + ' 기능을 이용하시려면 로그인이 필요합니다.\n로그인 페이지로 이동하시겠습니까?';
    
    if (confirm(message)) {
        // 현재 예약 정보를 유지하기 위해 bookingId를 URL 파라미터로 전달
        const urlParams = new URLSearchParams(window.location.search);
        const bookingId = urlParams.get('bookingId');
        
        // 현재 페이지의 contextPath 가져오기
        const fullPath = window.location.pathname;
        const contextPath = fullPath.substring(0, fullPath.indexOf('/', 1)) || '';
        
        // 로그인 페이지로 리다이렉트 (간단한 방법)
        console.log('Redirecting to login with contextPath:', contextPath);
        window.location.href = contextPath + '/loginForm.do';
    }
}