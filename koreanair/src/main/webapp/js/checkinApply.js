document.addEventListener('DOMContentLoaded', function() {
    const applyForm = document.getElementById('applyForm');
    if (applyForm) {
        applyForm.addEventListener('submit', function(event) {
            const agreement1 = document.getElementById('agreement1');
            const agreement2 = document.getElementById('agreement2');

            // 필수 동의 항목이 하나라도 체크되지 않았다면
            if (!agreement1.checked || !agreement2.checked) {
                // 폼 제출(페이지 이동)을 막고 사용자에게 알림
                event.preventDefault(); 
                alert('필수 동의 항목을 모두 체크해주세요.');
            }
        });
    }
});

// 비회원 체크인에서 좌석 선택 시 로그인 필요 알림
function showLoginRequiredForSeat() {
    const message = '좌석 선택을 위해서는 로그인이 필요합니다.\n로그인 페이지로 이동하시겠습니까?';
    
    if (confirm(message)) {
        // 현재 예약 정보를 유지하기 위해 bookingId를 가져옴
        const bookingIdInput = document.querySelector('input[name="bookingId"]');
        const bookingId = bookingIdInput ? bookingIdInput.value : '';
        
        // 현재 페이지의 contextPath 가져오기
        const fullPath = window.location.pathname;
        const contextPath = fullPath.substring(0, fullPath.indexOf('/', 1)) || '';
        
        // 좌석 선택 페이지를 targetUrl로 설정
        const targetUrl = '/checkinSeat.do?bookingId=' + bookingId;
        
        // 로그인 페이지로 리다이렉트
        window.location.href = contextPath + '/loginForm.do?targetUrl=' + encodeURIComponent(targetUrl);
    }
}