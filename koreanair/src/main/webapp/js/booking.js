// 승객 정보 관리 스크립트 - 업데이트: 2025-01-03 15:30:00
// 브라우저 캐시 무효화를 위한 타임스탬프

console.log('✈️ Korean Air 예약 시스템 로드됨');

// DOM이 로드된 후 실행
document.addEventListener('DOMContentLoaded', function() {
    // 승객 카드 구조 확인
    checkPassengerCards();
    
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

// 승객 카드 구조 확인 함수
function checkPassengerCards() {
    // 모든 승객 카드 찾기
    const passengerCards = document.querySelectorAll('.passenger-card');
    console.log(`✅ 승객 카드 ${passengerCards.length}개 로드됨`);
    
    // 전체 폼의 입력 필드 개수 확인
    const passengerForm = document.getElementById('passengerInfoForm');
    if (passengerForm) {
        const allInputs = passengerForm.querySelectorAll('input, select, textarea');
        console.log(`✅ 총 입력 필드 ${allInputs.length}개 활성화됨`);
    }
    
    // 초기 상태 설정: 첫 번째 카드만 열고 나머지는 접기
    initializePassengerCardsState(passengerCards.length);
}

// 승객 카드 초기 상태 설정
function initializePassengerCardsState(totalCards) {
    for (let i = 1; i <= totalCards; i++) {
        const content = document.getElementById('passengerContent' + i);
        const icon = document.getElementById('toggleIcon' + i);
        
        if (content && icon) {
            if (i === 1) {
                // 첫 번째 카드는 열어두기
                content.classList.remove('collapsed');
                icon.classList.remove('rotated');
                console.log(`📂 승객 ${i} 카드 열림 (활성)`);
            } else {
                // 나머지 카드들은 접어두기
                content.classList.add('collapsed');
                icon.classList.add('rotated');
                console.log(`📁 승객 ${i} 카드 접힘 (대기)`);
            }
        }
    }
    
    // 첫 번째 승객 카드의 첫 번째 입력 필드에 포커스
    setTimeout(() => {
        const firstCard = document.getElementById('passengerCard1');
        if (firstCard) {
            const firstInput = firstCard.querySelector('input, select');
            if (firstInput) {
                firstInput.focus();
                console.log('🎯 첫 번째 입력 필드에 포커스 설정');
            }
        }
    }, 500);
}

// 모든 입력 필드 활성화 확인
function enableAllInputs() {
    // 모든 입력 필드 찾기
    const allInputs = document.querySelectorAll('input, select, textarea');
    
    allInputs.forEach((input, index) => {
        // 비활성화된 필드가 있는지 확인
        if (input.disabled || input.readOnly) {
            input.disabled = false;
            input.readOnly = false;
        }
        
        // 스타일로 인한 입력 차단 해제
        input.style.pointerEvents = 'auto';
        input.style.userSelect = 'auto';
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
    // 모든 필수 필드에 대해 간단한 검증만 적용
    const requiredInputs = document.querySelectorAll('input[required], select[required]');
    
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
    console.log('💳 결제 처리 시작');
    
    try {
        // 기본 정보 수집
        const bookingId = window.bookingId || '';
        const totalPriceElement = document.getElementById('bookingTotalPrice');
        const totalPrice = (totalPriceElement && totalPriceElement.value) || '';
        
        if (!bookingId || !totalPrice) {
            alert('필수 결제 정보가 누락되었습니다.');
            return;
        }
        
        // 로딩 표시
        showLoadingSpinner();
        
        // 폼을 생성해서 POST 방식으로 전송 (페이지 이동)
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = window.contextPath + '/testPayment.do';
        
        // bookingId 파라미터 추가
        const bookingIdInput = document.createElement('input');
        bookingIdInput.type = 'hidden';
        bookingIdInput.name = 'bookingId';
        bookingIdInput.value = bookingId;
        form.appendChild(bookingIdInput);
        
        // totalAmount 파라미터 추가
        const totalAmountInput = document.createElement('input');
        totalAmountInput.type = 'hidden';
        totalAmountInput.name = 'totalAmount';
        totalAmountInput.value = totalPrice;
        form.appendChild(totalAmountInput);
        
        // 폼을 body에 추가하고 전송
        document.body.appendChild(form);
        form.submit();
        
    } catch (error) {
        console.error('❌ processPayment 함수 오류:', error);
        hideLoadingSpinner();
        alert('결제 처리 함수에서 오류가 발생했습니다: ' + error.message);
    }
}

// 결제 데이터 수집
function collectPaymentData() {
    console.log('📊 결제 데이터 수집 시작');
    
    // 예약 정보 수집
    const tripType = document.getElementById('bookingTripType').value;
    const totalPrice = document.getElementById('bookingTotalPrice').value;
    const departure = document.getElementById('bookingDeparture').value;
    const arrival = document.getElementById('bookingArrival').value;
    
    console.log('🔍 수집된 기본 정보:');
    console.log('- tripType:', tripType);
    console.log('- totalPrice:', totalPrice);
    console.log('- departure:', departure);
    console.log('- arrival:', arrival);
    console.log('- window.bookingId:', window.bookingId);
    
    // 승객 정보 수집
    const passengers = [];
    const passengerCards = document.querySelectorAll('.passenger-card');
    
    passengerCards.forEach((card, index) => {
        const passengerIndex = index + 1;
        const passenger = {
            passengerNumber: passengerIndex,
            nationality: (card.querySelector('#nationality' + passengerIndex) && card.querySelector('#nationality' + passengerIndex).value) || '',
            lastName: (card.querySelector('#lastName' + passengerIndex) && card.querySelector('#lastName' + passengerIndex).value) || '',
            firstName: (card.querySelector('#firstName' + passengerIndex) && card.querySelector('#firstName' + passengerIndex).value) || '',
            gender: (card.querySelector('#gender' + passengerIndex) && card.querySelector('#gender' + passengerIndex).value) || '',
            birthDate: (card.querySelector('#birthDate' + passengerIndex) && card.querySelector('#birthDate' + passengerIndex).value) || '',
            jobAirline: (card.querySelector('#jobAirline' + passengerIndex) && card.querySelector('#jobAirline' + passengerIndex).value) || '',
            memberNumber: (card.querySelector('#memberNumber' + passengerIndex) && card.querySelector('#memberNumber' + passengerIndex).value) || '',
            discountType: (card.querySelector('#discountType' + passengerIndex) && card.querySelector('#discountType' + passengerIndex).value) || '',
            returnDiscountType: (card.querySelector('#returnDiscountType' + passengerIndex) && card.querySelector('#returnDiscountType' + passengerIndex).value) || ''
        };
        passengers.push(passenger);
    });
    
    // 연락처 정보 수집
    const contact = {
        email: (document.getElementById('email') && document.getElementById('email').value) || '',
        countryCode: (document.querySelector('[name="countryCode"]') && document.querySelector('[name="countryCode"]').value) || '+82',
        phone: (document.getElementById('phone') && document.getElementById('phone').value) || ''
    };
    
    // 약관 동의 정보 수집
    const agreements = {
        terms: (document.querySelector('[name="agreeTerms"]') && document.querySelector('[name="agreeTerms"]').checked) || false,
        privacy: (document.querySelector('[name="agreePrivacy"]') && document.querySelector('[name="agreePrivacy"]').checked) || false,
        marketing: (document.querySelector('[name="agreeMarketing"]') && document.querySelector('[name="agreeMarketing"]').checked) || false
    };
    
    // 항공편 정보 수집
    const flightInfo = {
        tripType: tripType,
        departure: departure,
        arrival: arrival,
        flightId: (document.getElementById('bookingFlightId') && document.getElementById('bookingFlightId').value) || '',
        outboundFlightId: (document.getElementById('bookingOutboundFlightId') && document.getElementById('bookingOutboundFlightId').value) || '',
        returnFlightId: (document.getElementById('bookingReturnFlightId') && document.getElementById('bookingReturnFlightId').value) || '',
        totalPrice: parseInt(totalPrice) || 0
    };
    
    // bookingId 안전하게 가져오기
    let bookingId = '';
    if (typeof window !== 'undefined' && window.bookingId) {
        bookingId = window.bookingId;
    } else {
        console.warn('⚠️ window.bookingId가 정의되지 않음');
        bookingId = 'FALLBACK-' + Date.now();
    }
    
    const paymentData = {
        flightInfo: flightInfo,
        passengers: passengers,
        contact: contact,
        agreements: agreements,
        paymentAmount: parseInt(totalPrice) || 0,
        bookingId: bookingId,
        timestamp: new Date().toISOString()
    };
    
    console.log('📦 최종 결제 데이터:');
    console.log('- paymentAmount:', paymentData.paymentAmount);
    console.log('- bookingId:', paymentData.bookingId);
    console.log('- 전체 데이터:', paymentData);
    
    return paymentData;
}

// 전체 폼 유효성 검사
function validateForm() {
    let isValid = true;
    let firstErrorField = null;
    
    // 승객 정보 폼 검증
    const passengerForm = document.getElementById('passengerInfoForm');
    if (passengerForm) {
        const passengerRequiredFields = passengerForm.querySelectorAll('input[required], select[required]');
        passengerRequiredFields.forEach(field => {
            if (!validateField(field)) {
                isValid = false;
                if (!firstErrorField) firstErrorField = field;
            }
        });
    }
    
    // 연락처 정보 폼 검증
    const contactForm = document.getElementById('contactForm');
    if (contactForm) {
        const contactRequiredFields = contactForm.querySelectorAll('input[required], select[required]');
        contactRequiredFields.forEach(field => {
            if (!validateField(field)) {
                isValid = false;
                if (!firstErrorField) firstErrorField = field;
            }
        });
    }
    
    // 이메일 특별 검사
    const emailField = document.getElementById('email');
    if (emailField && !validateEmail(emailField)) {
        isValid = false;
        if (!firstErrorField) firstErrorField = emailField;
    }
    
    // 전화번호 특별 검사
    const phoneField = document.getElementById('phone');
    if (phoneField && !validatePhone(phoneField)) {
        isValid = false;
        if (!firstErrorField) firstErrorField = phoneField;
    }
    
    if (!isValid) {
        alert('입력 정보를 다시 확인해주세요.');
        // 첫 번째 오류 필드로 스크롤
        if (firstErrorField) {
            firstErrorField.scrollIntoView({ behavior: 'smooth', block: 'center' });
            firstErrorField.focus();
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
    
    let confirmMessage = amount + '에 대해 결제를 진행하시겠습니까?\n\n';
    
    if (tripType === 'round') {
        const outboundFlightId = document.getElementById('bookingOutboundFlightId').value;
        const returnFlightId = document.getElementById('bookingReturnFlightId').value;
        confirmMessage += '가는 편: ' + departure + ' → ' + arrival + ' (' + outboundFlightId + ')\n';
        confirmMessage += '오는 편: ' + arrival + ' → ' + departure + ' (' + returnFlightId + ')\n\n';
    } else {
        const flightId = document.getElementById('bookingFlightId').value;
        confirmMessage += '항공편: ' + departure + ' → ' + arrival + ' (' + flightId + ')\n\n';
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
function showPaymentSuccess(data = null) {
    let message = '결제가 완료되었습니다!\n\n';
    
    if (data && data.reservationNumber) {
        message += '예약 번호: ' + data.reservationNumber + '\n';
    }
    
    message += '예약 확인서가 이메일로 발송됩니다.\n감사합니다.';
    
    alert(message);
    
    // 결제 완료 페이지로 이동하거나 홈으로 이동
    if (data && data.redirectUrl) {
        window.location.href = data.redirectUrl;
    } else {
        // 기본적으로 홈페이지로 이동
        window.location.href = window.contextPath + '/';
    }
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
function savePassengerInfo(passengerIndex) {
    console.log('=== 승객 정보 저장 함수 시작 ===');
    console.log('승객 정보 저장 시작, 승객 인덱스:', passengerIndex);
    
    // 기본값 설정 (인덱스가 없으면 1번 승객)
    if (!passengerIndex) {
        passengerIndex = 1;
    }
    
    // 승객 정보 폼 유효성 검사
    const form = document.getElementById('passengerInfoForm');
    console.log('폼 요소:', form);
    if (!form) {
        alert('승객 정보 폼을 찾을 수 없습니다.');
        return;
    }
    
    console.log('폼 유효성 검사 시작');
    
    // 폼 내의 모든 input, select 요소 확인
    const allInputs = form.querySelectorAll('input, select');
    console.log('폼 내 전체 요소 개수:', allInputs.length);
    allInputs.forEach((element, index) => {
        console.log(`요소 ${index}: id=${element.id}, name=${element.name}, value=${element.value}`);
    });
    
    // 해당 승객 카드의 필수 필드만 검증
    const passengerCard = document.getElementById(`passengerCard${passengerIndex}`);
    if (!passengerCard) {
        alert('승객 카드를 찾을 수 없습니다.');
        return;
    }
    
    const requiredFields = passengerCard.querySelectorAll('input[required], select[required]');
    let isValid = true;
    let errorMessages = [];
    
    // 필수 필드 검증 (해당 승객의 정보만)
    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            isValid = false;
            const label = field.parentElement.querySelector('label');
            const fieldName = label ? label.textContent.replace('*', '').trim() : field.name;
            errorMessages.push(`${fieldName}을(를) 입력해주세요.`);
            field.classList.add('error');
            field.style.borderColor = '#dc3545';
        } else {
            field.classList.remove('error');
            field.style.borderColor = '';
        }
    });
    
    if (!isValid) {
        alert('승객 ' + passengerIndex + '의 입력 정보를 확인해주세요:\n\n' + errorMessages.join('\n'));
        // 첫 번째 오류 필드로 스크롤
        const firstError = passengerCard.querySelector('.error');
        if (firstError) {
            firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            firstError.focus();
        }
        return;
    }
    
    // 승객 정보만 수집 (연락처 및 약관 정보 제외)
    const passengerData = {
        lastName: (function() {
            const lastNameElement = document.getElementById('lastName' + passengerIndex);
            return lastNameElement ? lastNameElement.value : '';
        })(),
        firstName: (function() {
            const firstNameElement = document.getElementById('firstName' + passengerIndex);
            return firstNameElement ? firstNameElement.value : '';
        })(),
        birthDate: (function() {
            const birthDateElement = document.getElementById('birthDate' + passengerIndex);
            return birthDateElement ? birthDateElement.value : '';
        })(),
        gender: (function() {
            const genderElement = document.getElementById('gender' + passengerIndex);
            return genderElement ? genderElement.value : '';
        })(),
        nationality: (function() {
            const nationalityElement = document.getElementById('nationality' + passengerIndex);
            return nationalityElement ? nationalityElement.value : '';
        })(),
        jobAirline: (function() {
            const jobAirlineElement = document.getElementById('jobAirline' + passengerIndex);
            return jobAirlineElement ? jobAirlineElement.value : '';
        })(),
        memberNumber: (function() {
            const memberNumberElement = document.getElementById('memberNumber' + passengerIndex);
            return memberNumberElement ? memberNumberElement.value : '';
        })(),
        discountType: (function() {
            const discountTypeElement = document.getElementById('discountType' + passengerIndex);
            return discountTypeElement ? discountTypeElement.value : '';
        })(),
        returnDiscountType: (function() {
            const returnDiscountTypeElement = document.getElementById('returnDiscountType' + passengerIndex);
            return returnDiscountTypeElement ? returnDiscountTypeElement.value : '';
        })()
    };
    
    // 저장 버튼 상태 변경 (해당 승객 카드의 저장 버튼 찾기)
    const passengerCardForBtn = document.getElementById('passengerCard' + passengerIndex);
    const saveBtn = passengerCardForBtn ? passengerCardForBtn.querySelector('.passenger-save-btn') : null;
    if (!saveBtn) {
        alert('저장 버튼을 찾을 수 없습니다.');
        return;
    }
    
    const originalText = saveBtn.innerHTML;
    saveBtn.disabled = true;
    saveBtn.innerHTML = '저장 중...';
    saveBtn.style.background = '#ccc';
    
    // AJAX를 통한 서버 전송 (페이지 이동 없음)
    try {
        // 수동으로 FormData 생성 (서버에서 기대하는 형식으로)
        const formData = new FormData();
        
        // 승객 정보 추가 (안전한 방식)
        const nationalityEl = document.getElementById('nationality' + passengerIndex);
        const lastNameEl = document.getElementById('lastName' + passengerIndex);
        const firstNameEl = document.getElementById('firstName' + passengerIndex);
        const genderEl = document.getElementById('gender' + passengerIndex);
        const birthDateEl = document.getElementById('birthDate' + passengerIndex);
        const jobAirlineEl = document.getElementById('jobAirline' + passengerIndex);
        const memberNumberEl = document.getElementById('memberNumber' + passengerIndex);
        const discountTypeEl = document.getElementById('discountType' + passengerIndex);
        const returnDiscountTypeEl = document.getElementById('returnDiscountType' + passengerIndex);
        
        const nationality = nationalityEl ? nationalityEl.value.trim() : '';
        const lastName = lastNameEl ? lastNameEl.value.trim() : '';
        const firstName = firstNameEl ? firstNameEl.value.trim() : '';
        const gender = genderEl ? genderEl.value.trim() : '';
        const birthDate = birthDateEl ? birthDateEl.value.trim() : '';
        const jobAirline = jobAirlineEl ? jobAirlineEl.value.trim() : '';
        const memberNumber = memberNumberEl ? memberNumberEl.value.trim() : '';
        const discountType = discountTypeEl ? discountTypeEl.value.trim() : '';
        const returnDiscountType = returnDiscountTypeEl ? returnDiscountTypeEl.value.trim() : '';
        
        // 필수 필드 검증
        if (!lastName || !firstName || !gender || !birthDate) {
            alert('필수 정보를 모두 입력해주세요.\n- 성: ' + (lastName ? '✓' : '✗') + 
                  '\n- 이름: ' + (firstName ? '✓' : '✗') + 
                  '\n- 성별: ' + (gender ? '✓' : '✗') + 
                  '\n- 생년월일: ' + (birthDate ? '✓' : '✗'));
            
            // 저장 버튼 상태 복원
            saveBtn.disabled = false;
            saveBtn.innerHTML = originalText;
            saveBtn.style.background = '#0064de';
            return;
        }
        
        // 개별 승객 정보로 전송 (배열 형태가 아닌 단일 객체)
        formData.append('nationality', nationality);
        formData.append('lastName', lastName);
        formData.append('firstName', firstName);
        formData.append('gender', gender);
        formData.append('birthDate', birthDate);
        formData.append('jobAirline', jobAirline);
        formData.append('memberNumber', memberNumber);
        formData.append('discountType', discountType);
        formData.append('returnDiscountType', returnDiscountType);
        
        // 승객 인덱스 정보 추가
        formData.append('passengerIndex', passengerIndex);
        
        console.log('=== 승객 ' + passengerIndex + ' 정보 전송 ===');
        console.log('nationality:', nationality);
        console.log('lastName:', lastName);
        console.log('firstName:', firstName);
        console.log('gender:', gender);
        console.log('birthDate:', birthDate);
        console.log('passengerIndex:', passengerIndex);
        
        // bookingId 추가
        if (window.bookingId && window.bookingId !== 'null') {
            formData.append('bookingId', window.bookingId);
            console.log('bookingId 추가:', window.bookingId);
        }
        if (window.outBookingId && window.outBookingId !== 'null') {
            formData.append('outBookingId', window.outBookingId);
            console.log('outBookingId 추가:', window.outBookingId);
        }
        if (window.returnBookingId && window.returnBookingId !== 'null') {
            formData.append('returnBookingId', window.returnBookingId);
            console.log('returnBookingId 추가:', window.returnBookingId);
        }
        
        // 요청 URL 확인
        console.log('window.contextPath:', window.contextPath);
        const requestUrl = window.contextPath + '/passenger.do';
        console.log('요청 URL:', requestUrl);
        
        // FormData 내용 확인
        console.log('=== FormData 내용 확인 ===');
        for (let [key, value] of formData.entries()) {
            console.log(key + ': ' + value);
        }
        
        // URL 파라미터 방식으로 전송 (테스트용)
        const params = new URLSearchParams();
        
        // FormData의 모든 내용을 URLSearchParams로 복사
        for (let [key, value] of formData.entries()) {
            params.append(key, value);
            console.log('URLParams 추가: ' + key + ' = ' + value);
        }
        
        // GET 방식으로 테스트 (URL에 파라미터 포함)
        const testUrl = requestUrl + '?' + params.toString();
        console.log('테스트 URL:', testUrl);
        
        // AJAX 요청
        fetch(requestUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params.toString()
        })
        .then(response => {
            console.log('서버 응답 상태:', response.status);
            if (response.ok) {
                // 저장 완료 상태로 변경
                saveBtn.disabled = false;
                saveBtn.innerHTML = '저장 완료';
                saveBtn.style.background = '#28a745';
                
                // 성공 메시지
                console.log('✅ 승객 ' + passengerIndex + ' 정보 독립 저장 완료');
                
                // 간단한 성공 알림
                const notification = document.createElement('div');
                notification.style.cssText = `
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    background: #28a745;
                    color: white;
                    padding: 12px 20px;
                    border-radius: 4px;
                    z-index: 10000;
                    font-weight: bold;
                    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                `;
                notification.textContent = '승객 ' + passengerIndex + ' 정보가 저장되었습니다.';
                document.body.appendChild(notification);
                
                // 3초 후 알림 제거
                setTimeout(() => {
                    if (notification.parentNode) {
                        notification.parentNode.removeChild(notification);
                    }
                }, 3000);
                
                // 카드 접기 및 요약 표시
                collapsePassengerCardAfterSave(passengerIndex);
                
                // 다음 승객 카드 열기
                openNextPassengerCard(passengerIndex);
                
                // 로컬 스토리지에도 백업 저장
                localStorage.setItem('passengerInfo_' + passengerIndex, JSON.stringify(passengerData));
                
                // 3초 후 원래 상태로 복원
                setTimeout(() => {
                    saveBtn.innerHTML = originalText;
                    saveBtn.style.background = '#0064de';
                }, 3000);
                
            } else {
                // 서버에서 오류 메시지 읽기
                return response.text().then(errorText => {
                    throw new Error(errorText || ('서버 응답 오류: ' + response.status));
                });
            }
        })
        .catch(error => {
            console.error('승객 정보 저장 중 오류 발생:', error);
            
            // 오류 상태로 변경
            saveBtn.disabled = false;
            saveBtn.innerHTML = originalText;
            saveBtn.style.background = '#0064de';
            
            // 오류 메시지 표시
            alert('승객 정보 저장 중 오류가 발생했습니다. 다시 시도해주세요.\n\n오류: ' + error.message);
        });
        
    } catch (error) {
        console.error('승객 정보 저장 중 오류 발생:', error);
        
        // 오류 상태로 변경
        saveBtn.disabled = false;
        saveBtn.innerHTML = originalText;
        saveBtn.style.background = '#0064de';
        
        // 오류 메시지 표시
        alert('승객 정보 저장 중 오류가 발생했습니다. 다시 시도해주세요.\n\n오류: ' + error.message);
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
function collapsePassengerCardAfterSave(passengerIndex) {
    // 기본값 설정
    if (!passengerIndex) {
        passengerIndex = 1;
    }
    
    const cardId = 'passengerCard' + passengerIndex;
    const content = document.getElementById('passengerContent' + passengerIndex);
    const icon = document.getElementById('toggleIcon' + passengerIndex);
    const summary = document.getElementById('passengerSummary' + passengerIndex);
    
    // 입력된 정보로 요약 텍스트 생성
    const lastNameElement = document.getElementById('lastName' + passengerIndex);
    const firstNameElement = document.getElementById('firstName' + passengerIndex);
    const lastName = lastNameElement ? lastNameElement.value : '';
    const firstName = firstNameElement ? firstNameElement.value : '';
    
    if (lastName && firstName && summary) {
        const summaryTextElement = summary.querySelector('.summary-text');
        if (summaryTextElement) {
            const summaryText = lastName + ' / ' + firstName;
            summaryTextElement.textContent = summaryText;
            summary.style.display = 'block';
        }
    }
    
    // 카드 접기
    if (content && icon) {
        content.classList.add('collapsed');
        icon.classList.add('rotated');
    }
}

// 다음 승객 카드 열기
function openNextPassengerCard(currentPassengerIndex) {
    const nextPassengerIndex = currentPassengerIndex + 1;
    const nextCard = document.getElementById('passengerCard' + nextPassengerIndex);
    
    if (nextCard) {
        console.log('📂 다음 승객 카드 열기: 승객 ' + nextPassengerIndex);
        
        // 다음 승객 카드의 콘텐츠와 아이콘 찾기
        const nextContent = document.getElementById('passengerContent' + nextPassengerIndex);
        const nextIcon = document.getElementById('toggleIcon' + nextPassengerIndex);
        
        if (nextContent && nextIcon) {
            // 다음 카드가 접혀있으면 펼치기
            if (nextContent.classList.contains('collapsed')) {
                nextContent.classList.remove('collapsed');
                nextIcon.classList.remove('rotated');
                
                // 다음 카드로 부드럽게 스크롤
                setTimeout(() => {
                    nextCard.scrollIntoView({ 
                        behavior: 'smooth', 
                        block: 'start',
                        inline: 'nearest'
                    });
                    
                    // 첫 번째 입력 필드에 포커스
                    const firstInput = nextCard.querySelector('input, select');
                    if (firstInput) {
                        setTimeout(() => {
                            firstInput.focus();
                        }, 300);
                    }
                }, 200);
            }
        }
    } else {
        // 마지막 승객인 경우
        console.log('🎉 모든 승객 정보 입력 완료!');
        
        // 결제 섹션으로 스크롤 또는 다음 단계 안내
        const paymentSection = document.querySelector('.payment-section, .contact-info, .terms-section');
        if (paymentSection) {
            setTimeout(() => {
                paymentSection.scrollIntoView({ 
                    behavior: 'smooth', 
                    block: 'start' 
                });
            }, 500);
        }
        
        // 완료 메시지 표시
        setTimeout(() => {
            const notification = document.createElement('div');
            notification.innerHTML = `
                <div style="
                    position: fixed; 
                    top: 20px; 
                    right: 20px; 
                    background: #28a745; 
                    color: white; 
                    padding: 15px 20px; 
                    border-radius: 8px; 
                    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                    z-index: 1000;
                    font-weight: 500;
                ">
                    ✅ 모든 승객 정보가 저장되었습니다!
                </div>
            `;
            document.body.appendChild(notification);
            
            // 3초 후 알림 제거
            setTimeout(() => {
                document.body.removeChild(notification);
            }, 3000);
        }, 300);
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
    // 모든 생년월일 입력 필드에 포맷팅 적용
    const birthDateInputs = document.querySelectorAll('input[id^="birthDate"]');
    birthDateInputs.forEach(input => {
        input.addEventListener('input', function() {
            formatBirthDate(this);
        });
    });
}); 