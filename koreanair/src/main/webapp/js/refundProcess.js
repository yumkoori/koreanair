// refund-confirmation.js

function handleRefund() {
    // 예약번호 확인
    //const bookingId = document.getElementById('bookingId').value;
    const bookingId = 'BKDON002';
    if (!bookingId) {
        alert('예약번호가 없습니다. 다시 시도해주세요.');
        return;
    }
    
    // 로딩 화면 표시
    document.getElementById('loading').style.display = 'flex';
    
    // 컨텍스트 패스 가져오기
    const contextPath = document.getElementById('contextPath').value;
    
    // 서버에 환불 요청 전송
    const xhr = new XMLHttpRequest();
    xhr.open('POST', contextPath + "/RefundProcess.do", true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            // 로딩 화면 숨기기
            document.getElementById('loading').style.display = 'none';
            
            if (xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    showResult(response.success, response.message);
                } catch (e) {
                    console.error('응답 파싱 오류:', e);
                    showResult(false, '서버 응답을 처리하는 중 오류가 발생했습니다.');
                }
            } else {
                console.error('HTTP 오류:', xhr.status);
                showResult(false, '서버와의 통신 중 오류가 발생했습니다.');
            }
        }
    };
    
    // 요청 파라미터 구성
    const params = 'bookingId=' + encodeURIComponent(bookingId);
    
    xhr.send(params);
}

function handleCancel() {
    // 취소 시 이전 페이지나 메인 페이지로 이동
    if (confirm('환불을 취소하시겠습니까?')) {
        // window.history.back(); // 이전 페이지로
        // 또는
        window.location.href = document.getElementById('contextPath').value + '/index.jsp';
    }
}

function showResult(success, message) {
    // 확인 컨테이너 숨기기
    document.querySelector('.confirmation-container').style.display = 'none';
    
    // 결과 메시지 표시
    const resultMessage = document.getElementById('resultMessage');
    const resultIcon = document.getElementById('resultIcon');
    const resultText = document.getElementById('resultText');
    
    if (success) {
        resultIcon.innerHTML = '✅';
        resultIcon.style.color = '#28a745';
        resultText.innerHTML = message || '환불이 성공적으로 처리되었습니다.';
    } else {
        resultIcon.innerHTML = '❌';
        resultIcon.style.color = '#dc3545';
        resultText.innerHTML = message || '환불 처리 중 오류가 발생했습니다.';
    }
    
    resultMessage.style.display = 'flex';
}

function goToMain() {
    // 메인 페이지로 이동
    window.location.href = document.getElementById('contextPath').value + '/index.jsp';
}

// 키보드 이벤트 처리
document.addEventListener('keydown', function(event) {
    // 결과 메시지가 표시된 상태에서는 키보드 이벤트 무시
    if (document.getElementById('resultMessage').style.display === 'flex') {
        if (event.key === 'Enter' || event.key === 'Escape') {
            goToMain();
        }
        return;
    }
    
    // 로딩 중일 때는 키보드 이벤트 무시
    if (document.getElementById('loading').style.display === 'flex') {
        return;
    }
    
    if (event.key === 'Enter') {
        handleRefund();
    } else if (event.key === 'Escape') {
        handleCancel();
    }
});

// 페이지 로드 완료 후 실행
document.addEventListener('DOMContentLoaded', function() {
    console.log('환불 확인 페이지가 로드되었습니다.');
    
    // 예약번호 확인
    //const bookingId = document.getElementById('bookingId').value;
    const bookingId = 'BKDON002';
    if (!bookingId) {
        console.warn('예약번호가 없습니다.');
        alert('예약번호가 없습니다. 올바른 경로로 접근해주세요.');
        window.history.back();
    }
});