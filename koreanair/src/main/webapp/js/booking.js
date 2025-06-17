// DOM이 로드된 후 실행
document.addEventListener('DOMContentLoaded', function() {
    console.log('Booking page loaded');
    
    // 입력 필드 확인 및 활성화
    enableAllInputs();
    
    // 전체 동의 체크박스 기능
    initializeAgreementCheckboxes();
    
    // 간단한 폼 검증만 적용
    initializeBasicValidation();
    
    // 특별 서비스 관련 기능
    initializeSpecialServices();
    
    // 결제 버튼 클릭 이벤트
    initializePaymentButton();
});

// 모든 입력 필드 활성화 확인
function enableAllInputs() {
    console.log('입력 필드 활성화 확인 중...');
    
    // 모든 입력 필드 찾기
    const allInputs = document.querySelectorAll('input, select, textarea');
    console.log('전체 입력 필드 개수:', allInputs.length);
    
    allInputs.forEach((input, index) => {
        // 비활성화된 필드가 있는지 확인
        if (input.disabled || input.readOnly) {
            console.log(`비활성화된 필드 발견: ${input.id || input.name || index}`);
            input.disabled = false;
            input.readOnly = false;
        }
        
        // 스타일로 인한 입력 차단 해제
        input.style.pointerEvents = 'auto';
        input.style.userSelect = 'auto';
        
        // 입력 테스트 이벤트 추가
        if (input.id) {
            input.addEventListener('focus', function() {
                console.log(`포커스: ${this.id}`);
            });
            
            input.addEventListener('input', function() {
                console.log(`입력: ${this.id} = ${this.value}`);
            });
        }
    });
}

// 전체 동의 체크박스 기능
function initializeAgreementCheckboxes() {
    const agreeAllCheckbox = document.getElementById('agreeAll');
    const requiredCheckboxes = document.querySelectorAll('input[name="agreeTerms"], input[name="agreePrivacy"]');
    const optionalCheckboxes = document.querySelectorAll('input[name="agreeMarketing"]');
    const allCheckboxes = document.querySelectorAll('.terms-section input[type="checkbox"]:not(#agreeAll)');
    
    if (agreeAllCheckbox) {
        // 전체 동의 체크박스 클릭 시
        agreeAllCheckbox.addEventListener('change', function() {
            const isChecked = this.checked;
            allCheckboxes.forEach(checkbox => {
                checkbox.checked = isChecked;
            });
        });
        
        // 개별 체크박스 변경 시 전체 동의 상태 업데이트
        allCheckboxes.forEach(checkbox => {
            checkbox.addEventListener('change', function() {
                updateAgreeAllStatus();
            });
        });
    }
    
    function updateAgreeAllStatus() {
        const checkedCount = Array.from(allCheckboxes).filter(cb => cb.checked).length;
        const totalCount = allCheckboxes.length;
        
        if (checkedCount === totalCount) {
            agreeAllCheckbox.checked = true;
            agreeAllCheckbox.indeterminate = false;
        } else if (checkedCount > 0) {
            agreeAllCheckbox.checked = false;
            agreeAllCheckbox.indeterminate = true;
        } else {
            agreeAllCheckbox.checked = false;
            agreeAllCheckbox.indeterminate = false;
        }
    }
}

// 간단한 폼 검증
function initializeBasicValidation() {
    console.log('기본 폼 검증 초기화 중...');
    
    // 모든 필수 필드에 대해 간단한 검증만 적용
    const requiredInputs = document.querySelectorAll('input[required], select[required]');
    console.log('필수 입력 필드 개수:', requiredInputs.length);
    
    requiredInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (!this.value.trim()) {
                this.style.borderColor = '#e91e63';
            } else {
                this.style.borderColor = '#ddd';
            }
        });
    });
    
    // 전화번호 자동 포맷팅만 유지
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function() {
            let value = this.value.replace(/[^0-9]/g, '');
            if (value.length >= 3) {
                if (value.length <= 7) {
                    value = value.replace(/(\d{3})(\d{1,4})/, '$1-$2');
                } else {
                    value = value.replace(/(\d{3})(\d{4})(\d{1,4})/, '$1-$2-$3');
                }
            }
            this.value = value;
        });
    }
}

// 개별 필드 유효성 검사
function validateField(field) {
    removeError(field);
    
    if (field.hasAttribute('required') && !field.value.trim()) {
        showError(field, '필수 입력 항목입니다.');
        return false;
    }
    
    return true;
}

// 이메일 유효성 검사
function validateEmail(emailField) {
    removeError(emailField);
    
    const email = emailField.value.trim();
    if (!email) {
        showError(emailField, '이메일을 입력해주세요.');
        return false;
    }
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        showError(emailField, '올바른 이메일 형식을 입력해주세요.');
        return false;
    }
    
    return true;
}

// 전화번호 유효성 검사
function validatePhone(phoneField) {
    removeError(phoneField);
    
    const phone = phoneField.value.trim();
    if (!phone) {
        showError(phoneField, '연락처를 입력해주세요.');
        return false;
    }
    
    const phoneRegex = /^010-\d{4}-\d{4}$/;
    if (!phoneRegex.test(phone)) {
        showError(phoneField, '010-0000-0000 형식으로 입력해주세요.');
        return false;
    }
    
    return true;
}

// 전화번호 자동 포맷팅
function formatPhoneNumber(phoneField) {
    let value = phoneField.value.replace(/[^0-9]/g, '');
    
    if (value.length >= 3) {
        if (value.length <= 7) {
            value = value.replace(/(\d{3})(\d{1,4})/, '$1-$2');
        } else {
            value = value.replace(/(\d{3})(\d{4})(\d{1,4})/, '$1-$2-$3');
        }
    }
    
    phoneField.value = value;
}

// 오류 메시지 표시
function showError(field, message) {
    field.classList.add('error');
    
    let errorElement = field.parentElement.querySelector('.error-message');
    if (!errorElement) {
        errorElement = document.createElement('div');
        errorElement.className = 'error-message';
        field.parentElement.appendChild(errorElement);
    }
    
    errorElement.textContent = message;
    
    // 오류 스타일 추가
    if (!document.querySelector('#errorStyles')) {
        const style = document.createElement('style');
        style.id = 'errorStyles';
        style.textContent = `
            .form-group input.error,
            .form-group select.error {
                border-color: #e91e63;
                box-shadow: 0 0 0 2px rgba(233, 30, 99, 0.2);
            }
            .error-message {
                color: #e91e63;
                font-size: 12px;
                margin-top: 5px;
            }
        `;
        document.head.appendChild(style);
    }
}

// 오류 메시지 제거
function removeError(field) {
    field.classList.remove('error');
    const errorElement = field.parentElement.querySelector('.error-message');
    if (errorElement) {
        errorElement.remove();
    }
}

// 특별 서비스 관련 기능
function initializeSpecialServices() {
    const specialMealCheckbox = document.querySelector('input[name="passengers[0].specialMeal"]');
    
    if (specialMealCheckbox) {
        specialMealCheckbox.addEventListener('change', function() {
            if (this.checked) {
                showSpecialMealOptions();
            }
        });
    }
}

// 특별 기내식 옵션 표시
function showSpecialMealOptions() {
    alert('특별 기내식 옵션:\n- 채식 기내식\n- 할랄 기내식\n- 저염식\n- 당뇨식\n\n자세한 선택은 고객센터로 문의 부탁드립니다.');
}

// 결제 버튼 초기화
function initializePaymentButton() {
    const paymentBtn = document.querySelector('.payment-btn');
    
    if (paymentBtn) {
        paymentBtn.addEventListener('click', function(e) {
            e.preventDefault();
            processPayment();
        });
    }
}

// 결제 처리
function processPayment() {
    // 폼 유효성 검사
    if (!validateForm()) {
        return;
    }
    
    // 필수 약관 동의 확인
    if (!validateRequiredAgreements()) {
        return;
    }
    
    // 결제 확인
    if (confirmPayment()) {
        // 로딩 표시
        showLoadingSpinner();
        
        // 실제 결제 처리 (시뮬레이션)
        setTimeout(() => {
            hideLoadingSpinner();
            showPaymentSuccess();
        }, 2000);
    }
}

// 전체 폼 유효성 검사
function validateForm() {
    const form = document.getElementById('passengerForm');
    const requiredFields = form.querySelectorAll('input[required], select[required]');
    let isValid = true;
    
    requiredFields.forEach(field => {
        if (!validateField(field)) {
            isValid = false;
        }
    });
    
    // 이메일 특별 검사
    const emailField = document.getElementById('email');
    if (emailField && !validateEmail(emailField)) {
        isValid = false;
    }
    
    // 전화번호 특별 검사
    const phoneField = document.getElementById('phone');
    if (phoneField && !validatePhone(phoneField)) {
        isValid = false;
    }
    
    if (!isValid) {
        alert('입력 정보를 다시 확인해주세요.');
        // 첫 번째 오류 필드로 스크롤
        const firstError = form.querySelector('.error');
        if (firstError) {
            firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            firstError.focus();
        }
    }
    
    return isValid;
}

// 필수 약관 동의 확인
function validateRequiredAgreements() {
    const requiredTerms = document.querySelector('input[name="agreeTerms"]');
    const requiredPrivacy = document.querySelector('input[name="agreePrivacy"]');
    
    if (!requiredTerms.checked || !requiredPrivacy.checked) {
        alert('필수 약관에 동의해주세요.');
        document.querySelector('.terms-section').scrollIntoView({ 
            behavior: 'smooth', 
            block: 'center' 
        });
        return false;
    }
    
    return true;
}

// 결제 확인
function confirmPayment() {
    const amount = document.querySelector('.final-amount .amount').textContent;
    const tripType = document.getElementById('bookingTripType').value;
    const departure = document.getElementById('bookingDeparture').value;
    const arrival = document.getElementById('bookingArrival').value;
    
    let confirmMessage = `${amount}에 대해 결제를 진행하시겠습니까?\n\n`;
    
    if (tripType === 'round') {
        const outboundFlightId = document.getElementById('bookingOutboundFlightId').value;
        const returnFlightId = document.getElementById('bookingReturnFlightId').value;
        confirmMessage += `가는 편: ${departure} → ${arrival} (${outboundFlightId})\n`;
        confirmMessage += `오는 편: ${arrival} → ${departure} (${returnFlightId})\n\n`;
    } else {
        const flightId = document.getElementById('bookingFlightId').value;
        confirmMessage += `항공편: ${departure} → ${arrival} (${flightId})\n\n`;
    }
    
    confirmMessage += `결제 후에는 취소 수수료가 발생할 수 있습니다.`;
    
    return confirm(confirmMessage);
}

// 로딩 스피너 표시
function showLoadingSpinner() {
    const paymentBtn = document.querySelector('.payment-btn');
    paymentBtn.disabled = true;
    paymentBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 결제 처리 중...';
    paymentBtn.style.background = '#ccc';
}

// 로딩 스피너 숨기기
function hideLoadingSpinner() {
    const paymentBtn = document.querySelector('.payment-btn');
    paymentBtn.disabled = false;
    paymentBtn.innerHTML = '결제하기';
    paymentBtn.style.background = 'linear-gradient(135deg, #0064de 0%, #0078d4 100%)';
}

// 결제 성공 표시
function showPaymentSuccess() {
    alert('결제가 완료되었습니다!\n\n예약 확인서가 이메일로 발송됩니다.\n감사합니다.');
    
    // 결제 완료 페이지로 이동 (실제로는 서버로 데이터 전송)
    // window.location.href = 'paymentComplete.jsp';
}

// 상세 보기 버튼 기능
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('details-btn')) {
        showFlightDetails();
    }
});

// 항공편 상세 정보 표시
function showFlightDetails() {
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h3>항공편 상세 정보</h3>
                <button class="modal-close">&times;</button>
            </div>
            <div class="modal-body">
                <div class="flight-detail-item">
                    <strong>항공편:</strong> KE1142
                </div>
                <div class="flight-detail-item">
                    <strong>기종:</strong> Boeing 737-800
                </div>
                <div class="flight-detail-item">
                    <strong>좌석:</strong> 일반석 (Economy)
                </div>
                <div class="flight-detail-item">
                    <strong>수하물:</strong> 위탁수하물 20kg 포함
                </div>
                <div class="flight-detail-item">
                    <strong>기내식:</strong> 제공
                </div>
                <div class="flight-detail-item">
                    <strong>변경 수수료:</strong> 50,000원
                </div>
                <div class="flight-detail-item">
                    <strong>환불 수수료:</strong> 100,000원
                </div>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    
    // 모달 스타일 추가
    if (!document.querySelector('#modalStyles')) {
        const style = document.createElement('style');
        style.id = 'modalStyles';
        style.textContent = `
            .modal-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                display: flex;
                align-items: center;
                justify-content: center;
                z-index: 10000;
            }
            .modal-content {
                background: white;
                border-radius: 15px;
                max-width: 500px;
                width: 90%;
                max-height: 80%;
                overflow: auto;
            }
            .modal-header {
                padding: 20px;
                border-bottom: 1px solid #eee;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .modal-close {
                background: none;
                border: none;
                font-size: 24px;
                cursor: pointer;
                color: #666;
            }
            .modal-body {
                padding: 20px;
            }
            .flight-detail-item {
                padding: 10px 0;
                border-bottom: 1px solid #f0f0f0;
            }
        `;
        document.head.appendChild(style);
    }
    
    // 모달 닫기 기능
    modal.addEventListener('click', function(e) {
        if (e.target === modal || e.target.classList.contains('modal-close')) {
            modal.remove();
        }
    });
}

// 승객 정보 저장 함수
function savePassengerInfo() {
    console.log('승객 정보 저장 시작');
    
    // 승객 정보 폼 유효성 검사
    const form = document.getElementById('passengerInfoForm');
    const requiredFields = form.querySelectorAll('input[required], select[required]');
    let isValid = true;
    let errorMessages = [];
    
    // 필수 필드 검증 (승객 정보만)
    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            isValid = false;
            const label = field.parentElement.querySelector('label');
            const fieldName = label ? label.textContent.replace('*', '').trim() : field.name;
            errorMessages.push(`${fieldName}을(를) 입력해주세요.`);
            field.classList.add('error');
        } else {
            field.classList.remove('error');
        }
    });
    
    if (!isValid) {
        alert('입력 정보를 확인해주세요:\n\n' + errorMessages.join('\n'));
        // 첫 번째 오류 필드로 스크롤
        const firstError = form.querySelector('.error');
        if (firstError) {
            firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            firstError.focus();
        }
        return;
    }
    
    // 승객 정보만 수집 (연락처 및 약관 정보 제외)
    const passengerData = {
        title: document.getElementById('title1').value,
        lastName: document.getElementById('lastName1').value,
        firstName: document.getElementById('firstName1').value,
        koreanName: document.getElementById('koreanName1').value,
        birthDate: document.getElementById('birthDate1').value,
        gender: document.getElementById('gender1').value,
        nationality: document.getElementById('nationality1').value,
        passportNumber: document.getElementById('passportNumber1').value,
        passportExpiry: document.getElementById('passportExpiry1').value,
        // 특별 서비스
        wheelchairService: document.querySelector('input[name="passengers[0].wheelchairService"]').checked,
        specialMeal: document.querySelector('input[name="passengers[0].specialMeal"]').checked,
        infantService: document.querySelector('input[name="passengers[0].infantService"]').checked
    };
    
    // 저장 버튼 상태 변경
    const saveBtn = document.querySelector('.passenger-save-btn');
    const originalText = saveBtn.innerHTML;
    saveBtn.disabled = true;
    saveBtn.innerHTML = '저장 중...';
    saveBtn.style.background = '#ccc';
    
    // 로컬 스토리지에 임시 저장 (실제로는 서버로 전송)
    try {
        localStorage.setItem('passengerInfo', JSON.stringify(passengerData));
        
        // 저장 성공 시뮬레이션 (1초 후)
        setTimeout(() => {
            saveBtn.disabled = false;
            saveBtn.innerHTML = '저장 완료';
            saveBtn.style.background = '#28a745';
            
            // 성공 메시지
            alert('승객 정보가 성공적으로 저장되었습니다.');
            
            // 카드 접기 및 요약 표시
            collapsePassengerCardAfterSave();
            
            // 3초 후 원래 상태로 복원
            setTimeout(() => {
                saveBtn.innerHTML = originalText;
                saveBtn.style.background = '#0064de';
            }, 3000);
            
        }, 1000);
        
    } catch (error) {
        console.error('저장 중 오류 발생:', error);
        saveBtn.disabled = false;
        saveBtn.innerHTML = originalText;
        saveBtn.style.background = '#0064de';
        alert('저장 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
    
    console.log('승객 정보:', passengerData);
}

// 승객 카드 토글 기능
function togglePassengerCard(cardId) {
    const content = document.getElementById(cardId.replace('Card', 'Content'));
    const icon = document.getElementById(cardId.replace('Card', '').replace('passenger', 'toggleIcon'));
    
    if (content && icon) {
        if (content.classList.contains('collapsed')) {
            // 펼치기
            content.classList.remove('collapsed');
            icon.classList.remove('rotated');
        } else {
            // 접기
            content.classList.add('collapsed');
            icon.classList.add('rotated');
        }
    }
}

// 승객 정보 저장 후 카드 접기 및 요약 표시
function collapsePassengerCardAfterSave() {
    const cardId = 'passengerCard1';
    const content = document.getElementById('passengerContent1');
    const icon = document.getElementById('toggleIcon1');
    const summary = document.getElementById('passengerSummary1');
    
    // 입력된 정보로 요약 텍스트 생성
    const lastName = document.getElementById('lastName1').value;
    const firstName = document.getElementById('firstName1').value;
    
    if (lastName && firstName) {
        const summaryText = `${lastName} / ${firstName}`;
        summary.querySelector('.summary-text').textContent = summaryText;
        summary.style.display = 'block';
    }
    
    // 카드 접기
    if (content && icon) {
        content.classList.add('collapsed');
        icon.classList.add('rotated');
    }
}

// 생년월일 입력 포맷팅
function formatBirthDate(input) {
    let value = input.value.replace(/[^0-9]/g, '');
    
    if (value.length >= 4) {
        if (value.length >= 6) {
            value = value.replace(/(\d{4})(\d{2})(\d{2})/, '$1.$2.$3');
        } else {
            value = value.replace(/(\d{4})(\d{2})/, '$1.$2');
        }
    }
    
    input.value = value;
}

// 생년월일 입력 필드에 포맷팅 이벤트 추가
document.addEventListener('DOMContentLoaded', function() {
    const birthDateInput = document.getElementById('birthDate1');
    if (birthDateInput) {
        birthDateInput.addEventListener('input', function() {
            formatBirthDate(this);
        });
    }
}); 