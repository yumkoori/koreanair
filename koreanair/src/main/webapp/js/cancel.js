document.addEventListener('DOMContentLoaded', function() {
    const cancelForm = document.getElementById('cancelForm');

    if (cancelForm) {
        cancelForm.addEventListener('submit', function(event) {
            // 필수 동의 체크박스들을 가져옵니다.
            const agreement1 = document.getElementById('agreement1');
            const agreement2 = document.getElementById('agreement2');

            // 하나라도 체크가 안 되어 있으면
            if (!agreement1.checked || !agreement2.checked) {
                // 폼 제출을 막습니다.
                event.preventDefault(); 
                alert('안내 사항의 필수 동의 항목을 모두 체크해주세요.');
            }
        });
    }

    // '신청 취소' 버튼에 뒤로가기 기능 추가
    const cancelBtn = document.getElementById('cancelBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', function() {
            history.back();
        });
    }
});