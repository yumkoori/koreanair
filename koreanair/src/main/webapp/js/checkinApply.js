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