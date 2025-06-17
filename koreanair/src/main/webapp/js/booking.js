// DOM이 로드된 후 실행
document.addEventListener('DOMContentLoaded', function() {
    console.log('Booking page loaded');
    
    // 전체 동의 체크박스 기능
    initializeAgreementCheckboxes();
    
    // 폼 유효성 검사
    initializeFormValidation();
    
    // 특별 서비스 관련 기능
    initializeSpecialServices();
    
    // 결제 버튼 클릭 이벤트
    initializePaymentButton();
});

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

// 폼 유효성 검사
function initializeFormValidation() {
    const form = document.getElementById('passengerForm');
    const inputs = form.querySelectorAll('input[required], select[required]');
    
    // 실시간 유효성 검사
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateField(this);
        });
        
        input.addEventListener('input', function() {
            if (this.classList.contains('error')) {
                validateField(this);
            }
        });
    });
    
    // 특별 유효성 검사 규칙
    const emailInput = document.getElementById('email');
    if (emailInput) {
        emailInput.addEventListener('blur', function() {
            validateEmail(this);
        });
    }
    
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('blur', function() {
            validatePhone(this);
        });
        
        phoneInput.addEventListener('input', function() {
            formatPhoneNumber(this);
        });
    }
    
    // 영문명 입력 필드 (영어만 입력 가능)
    const englishNameInputs = document.querySelectorAll('#lastName1, #firstName1');
    englishNameInputs.forEach(input => {
        input.addEventListener('input', function() {
            this.value = this.value.replace(/[^a-zA-Z\s]/g, '').toUpperCase();
        });
    });
    
    // 한글명 입력 필드 (한글만 입력 가능)
    const koreanNameInput = document.getElementById('koreanName1');
    if (koreanNameInput) {
        koreanNameInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^가-힣\s]/g, '');
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