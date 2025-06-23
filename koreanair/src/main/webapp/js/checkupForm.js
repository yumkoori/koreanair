document.addEventListener('DOMContentLoaded', function() {
    // 1. 예약 조회 폼과 필수 DOM 요소를 선택합니다.
    const checkupForm = document.getElementById('checkupForm');
    
    // 폼이 페이지에 존재하지 않으면, 아무 작업도 수행하지 않고 종료합니다.
    if (!checkupForm) {
        console.error('ID가 "checkupForm"인 폼을 찾을 수 없습니다.');
        return;
    }

    const errorMessageContainer = document.getElementById('errorMessageContainer');
    // 오류 메시지 컨테이너가 없으면 콘솔에만 오류를 기록하고, 폼 제출 기능은 유지합니다.
    if (!errorMessageContainer) {
        console.error('ID가 "errorMessageContainer"인 오류 메시지 표시 영역을 찾을 수 없습니다.');
    }

    // 2. 폼의 'data-context-path' 속성에서 contextPath 값을 가져옵니다.
    const contextPath = checkupForm.dataset.contextPath;
    
    // contextPath가 정의되지 않은 경우, 치명적인 오류이므로 사용자에게 알리고 작업을 중단합니다.
    if (typeof contextPath === 'undefined') {
        console.error('폼의 "data-context-path" 속성에 컨텍스트 경로가 정의되지 않았습니다.');
        if (errorMessageContainer) {
            errorMessageContainer.innerHTML = '<div class="error-message">페이지에 설정 오류가 발생했습니다. 관리자에게 문의하세요.</div>';
        }
        return;
    }

    // 3. 폼 제출(submit) 이벤트에 대한 리스너를 추가합니다.
    checkupForm.addEventListener('submit', function(event) {
        // 폼의 기본 제출 동작(페이지 새로고침)을 막습니다.
        event.preventDefault();

        // 새로운 요청을 보내기 전에, 이전에 표시되었을 수 있는 오류 메시지를 즉시 지웁니다.
        if (errorMessageContainer) {
            errorMessageContainer.innerHTML = '';
        }

        // 폼에 입력된 데이터를 가져옵니다.
        const formData = new FormData(checkupForm);
        const fetchUrl = `${contextPath}/checkup`;

        // 4. fetch API를 사용하여 서버에 비동기(AJAX) 요청을 보냅니다.
        fetch(fetchUrl, {
            method: 'POST',
            body: new URLSearchParams(formData)
        })
        .then(response => {
            // 서버 응답이 정상적이지 않은 경우(예: 404, 500 오류) 예외를 발생시킵니다.
            if (!response.ok) {
                throw new Error(`서버 응답에 오류가 있습니다. 상태 코드: ${response.status}`);
            }
            // 응답 본문을 JSON 형태로 파싱하여 반환합니다.
            return response.json();
        })
        .then(data => {
            // 5. 서버로부터 받은 JSON 데이터를 기반으로 화면을 처리합니다.
            if (data.success) {
                // 성공 시: 서버가 제공한 URL로 페이지를 이동시킵니다.
                const finalUrl = `${contextPath}/${data.redirectUrl}`;
                console.log('페이지 이동:', finalUrl);
                window.location.href = finalUrl;
            } else {
                // 실패 시: 서버가 제공한 오류 메시지를 화면에 표시합니다.
                if (errorMessageContainer) {
                    const errorMessage = data.error || '입력 정보를 확인했으나, 예약 내용을 찾을 수 없습니다.';
                    errorMessageContainer.innerHTML = `<div class="error-message">${errorMessage}</div>`;
                }
            }
        })
        .catch(error => {
            // 6. fetch 요청 중 발생한 오류(네트워크 문제, JSON 파싱 실패 등)를 처리합니다.
            console.error('Fetch API 오류:', error);
            if (errorMessageContainer) {
                errorMessageContainer.innerHTML = `<div class="error-message">오류가 발생했습니다. 잠시 후 다시 시도해 주세요.</div>`;
            }
        });
    });

    function populateCheckinDateSelector() {
        const dateSelect = document.getElementById('checkinDepartureDate');
        if (!dateSelect) return;

        const today = new Date();
        dateSelect.innerHTML = ''; // 기존 옵션 제거
        for (let i = -1; i <= 2; i++) {
            const targetDate = new Date();
            targetDate.setDate(today.getDate() + i);
            const option = document.createElement('option');
            const year = targetDate.getFullYear();
            const month = String(targetDate.getMonth() + 1).padStart(2, '0');
            const day = String(targetDate.getDate()).padStart(2, '0');
            
            option.value = `${year}-${month}-${day}`;
            option.textContent = `${year}년 ${month}월 ${day}일`;

            if (i === 0) { // 오늘 날짜를 기본으로 선택
                option.selected = true;
            }
            dateSelect.appendChild(option);
        }
    }
    populateCheckinDateSelector();
});