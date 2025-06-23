// 아이디 중복 체크 함수
function checkUserId() {
    const userId = document.getElementById('userId').value;
    const checkResult = document.getElementById('userIdCheckResult');
    
    if (userId.length < 3) {
        checkResult.innerHTML = '<span class="error-icon">✗</span> 아이디는 3자 이상이어야 합니다.';
        checkResult.setAttribute('data-status', 'invalid');
        return;
    }
    
    // AJAX 요청
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'checkUserId.do', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.exists) {
                checkResult.innerHTML = '<span class="error-icon">✗</span> 이미 사용 중인 아이디입니다.';
                checkResult.setAttribute('data-status', 'duplicate');
            } else {
                checkResult.innerHTML = '<span class="check-icon">✓</span> 사용 가능한 아이디입니다.';
                checkResult.setAttribute('data-status', 'available');
            }
        }
    };
    
    xhr.send('userId=' + encodeURIComponent(userId));
}

// 개별 검증 함수들 (재사용 가능)
function validateRequiredFields(fields) {
    for (let field of fields) {
        if (!field.value || field.value.trim() === '') {
            alert('모든 필수 항목을 입력해주세요.');
            field.focus();
            return false;
        }
    }
    return true;
}

function validateKoreanName(koreanName) {
    const koreanNameRegex = /^[가-힣\s]+$/;
    if (!koreanNameRegex.test(koreanName)) {
        alert('한글 이름은 한글만 입력 가능합니다.');
        return false;
    }
    return true;
}

function validateEnglishName(englishName) {
    const englishNameRegex = /^[a-zA-Z\s]+$/;
    if (!englishNameRegex.test(englishName)) {
        alert('영문 이름은 영문자만 입력 가능합니다.');
        return false;
    }
    return true;
}

function validateBirthDate(birthDate) {
    const today = new Date();
    const selectedDate = new Date(birthDate);
    
    // 미래 날짜 검증
    if (selectedDate >= today) {
        alert('생년월일은 오늘 이전 날짜여야 합니다.');
        return false;
    }
    
    // 나이 검증 (만 14세 이상)
    let age = today.getFullYear() - selectedDate.getFullYear();
    const monthDiff = today.getMonth() - selectedDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < selectedDate.getDate())) {
        age--;
    }
    if (age < 14) {
        alert('만 14세 이상만 가입 가능합니다.');
        return false;
    }
    
    return true;
}

function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert('올바른 이메일 형식을 입력해주세요.');
        return false;
    }
    return true;
}

function validatePhoneNumber(phone) {
    const phoneRegex = /^01[0-9]-\d{3,4}-\d{4}$/;
    if (!phoneRegex.test(phone)) {
        alert('휴대폰 번호는 010-1234-5678 형식으로 입력해주세요.');
        return false;
    }
    return true;
}

function validatePassword(password) {
    if (password.length < 4) {
        alert('비밀번호는 4자 이상이어야 합니다.');
        return false;
    }
    return true;
}

function validatePasswordConfirm(password, confirmPassword) {
    if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return false;
    }
    return true;
}

function validateUserId(userId) {
    if (userId.length < 3) {
        alert('아이디는 3자 이상이어야 합니다.');
        return false;
    }
    return true;
}

function validateUserIdDuplication() {
    const checkResult = document.getElementById('userIdCheckResult');
    if (!checkResult) return true; // 아이디 중복 체크가 없는 경우 (카카오 가입 등)
    
    const checkStatus = checkResult.getAttribute('data-status');
    if (checkStatus === 'duplicate') {
        alert('이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.');
        document.getElementById('userId').focus();
        return false;
    } else if (checkStatus !== 'available') {
        alert('아이디 중복 확인을 해주세요.');
        document.getElementById('userId').focus();
        return false;
    }
    return true;
}

// 카카오 회원가입 폼 검증 (공통 함수 활용)
function validateKakaoSignupForm() {
    const koreanName = document.getElementById('koreanName').value.trim();
    const englishName = document.getElementById('englishName').value.trim();
    const birthDate = document.getElementById('birthDate').value;
    const gender = document.getElementById('gender').value;
    const phone = document.getElementById('phone').value.trim();
    
    // 필수 필드 체크
    const requiredFields = [
        { value: koreanName, focus: () => document.getElementById('koreanName').focus() },
        { value: englishName, focus: () => document.getElementById('englishName').focus() },
        { value: birthDate, focus: () => document.getElementById('birthDate').focus() },
        { value: gender, focus: () => document.getElementById('gender').focus() },
        { value: phone, focus: () => document.getElementById('phone').focus() }
    ];
    
    for (let field of requiredFields) {
        if (!field.value || field.value.trim() === '') {
            alert('모든 필수 항목을 입력해주세요.');
            field.focus();
            return false;
        }
    }
    
    // 한글 이름 검증
    if (!validateKoreanName(koreanName)) {
        document.getElementById('koreanName').focus();
        return false;
    }
    
    // 영문 이름 검증
    if (!validateEnglishName(englishName)) {
        document.getElementById('englishName').focus();
        return false;
    }
    
    // 생년월일 검증
    if (!validateBirthDate(birthDate)) {
        document.getElementById('birthDate').focus();
        return false;
    }
    
    // 휴대폰 번호 검증
    if (!validatePhoneNumber(phone)) {
        document.getElementById('phone').focus();
        return false;
    }
    
    return true;
}

// 회원가입 폼 검증 (기존 함수 - 공통 함수 활용하도록 수정)
function validateRegisterForm() {
    const userId = document.getElementById('userId').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const koreanName = document.getElementById('koreanName').value;
    const englishName = document.getElementById('englishName').value;
    const birthDate = document.getElementById('birthDate').value;
    const gender = document.getElementById('gender').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    
    // 필수 필드 체크
    const requiredFields = [
        document.getElementById('userId'),
        document.getElementById('password'),
        document.getElementById('confirmPassword'),
        document.getElementById('koreanName'),
        document.getElementById('englishName'),
        document.getElementById('birthDate'),
        document.getElementById('gender'),
        document.getElementById('email'),
        document.getElementById('phone')
    ];
    
    if (!validateRequiredFields(requiredFields)) {
        return false;
    }
    
    // 아이디 길이 체크
    if (!validateUserId(userId)) {
        document.getElementById('userId').focus();
        return false;
    }
    
    // 아이디 중복 체크 결과 확인
    if (!validateUserIdDuplication()) {
        return false;
    }
    
    // 비밀번호 길이 체크
    if (!validatePassword(password)) {
        document.getElementById('password').focus();
        return false;
    }
    
    // 비밀번호 확인
    if (!validatePasswordConfirm(password, confirmPassword)) {
        document.getElementById('confirmPassword').focus();
        return false;
    }
    
    // 한글 이름 검증
    if (!validateKoreanName(koreanName)) {
        document.getElementById('koreanName').focus();
        return false;
    }
    
    // 영문 이름 검증
    if (!validateEnglishName(englishName)) {
        document.getElementById('englishName').focus();
        return false;
    }
    
    // 생년월일 검증
    if (!validateBirthDate(birthDate)) {
        document.getElementById('birthDate').focus();
        return false;
    }
    
    // 이메일 형식 체크
    if (!validateEmail(email)) {
        document.getElementById('email').focus();
        return false;
    }
    
    // 휴대폰 번호 형식 체크
    if (!validatePhoneNumber(phone)) {
        document.getElementById('phone').focus();
        return false;
    }
    
    return true;
}

// 쿠키 관련 함수들
function setCookie(name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function deleteCookie(name) {
    document.cookie = name + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
}

// 페이지 로드 시 저장된 아이디 불러오기
function loadSavedUserId() {
    const savedUserId = getCookie('savedUserId');
    const userIdInput = document.getElementById('userId');
    const rememberCheckbox = document.getElementById('remember');
    
    if (savedUserId && userIdInput) {
        userIdInput.value = savedUserId;
        if (rememberCheckbox) {
            rememberCheckbox.checked = true;
        }
    }
}

// 로그인 폼 검증
function validateLoginForm() {
    const userId = document.getElementById('userId').value;
    const password = document.getElementById('password').value;
    const rememberCheckbox = document.getElementById('remember');
    
    if (!userId || !password) {
        alert('아이디와 비밀번호를 입력해주세요.');
        return false;
    }
    
    // 아이디 저장 처리
    if (rememberCheckbox && rememberCheckbox.checked) {
        // 30일간 쿠키 저장
        setCookie('savedUserId', userId.trim(), 30);
    } else {
        // 체크박스가 해제되면 쿠키 삭제
        deleteCookie('savedUserId');
    }
    
    return true;
}

// 회원탈퇴 확인
function confirmDeleteAccount() {
    const password = document.getElementById('deletePassword').value;
    
    if (!password) {
        alert('비밀번호를 입력해주세요.');
        return false;
    }
    
    if (confirm('정말로 회원탈퇴를 하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
        return true;
    }
    
    return false;
}

// 카카오 사용자 회원탈퇴 확인
function confirmKakaoDeleteAccount() {
    if (confirm('정말로 회원탈퇴를 하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
        return true;
    }
    
    return false;
}

// 휴대폰 번호 자동 포맷팅
function formatPhoneNumber(input) {
    let value = input.value.replace(/[^0-9]/g, '');
    
    if (value.length >= 3 && value.length <= 7) {
        value = value.replace(/(\d{3})(\d{1,4})/, '$1-$2');
    } else if (value.length >= 8) {
        value = value.replace(/(\d{3})(\d{3,4})(\d{4})/, '$1-$2-$3');
    }
    
    input.value = value;
}

// 탭 기능
function initTabs() {
    const tabButtons = document.querySelectorAll('.tab-btn');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            // 모든 탭 버튼에서 active 클래스 제거
            tabButtons.forEach(btn => btn.classList.remove('active'));
            
            // 클릭된 탭 버튼에 active 클래스 추가
            this.classList.add('active');
            
            // 탭에 따른 라벨 텍스트 변경
            const userIdLabel = document.querySelector('label[for="userId"]');
            if (userIdLabel) {
                if (this.textContent.includes('스카이패스')) {
                    userIdLabel.innerHTML = '스카이패스 번호<span style="color: red;">*</span>';
                    document.getElementById('userId').placeholder = '';
                } else {
                    userIdLabel.innerHTML = '아이디<span style="color: red;">*</span>';
                    document.getElementById('userId').placeholder = '';
                }
            }
        });
    });
}

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
    // 탭 기능 초기화
    initTabs();
    
    // 저장된 아이디 불러오기
    loadSavedUserId();
    
    // 아이디 입력 필드에 이벤트 리스너 추가
    const userIdInput = document.getElementById('userId');
    if (userIdInput) {
        userIdInput.addEventListener('blur', checkUserId);
        
        // 아이디 입력 시 중복 체크 상태 초기화
        userIdInput.addEventListener('input', function() {
            const checkResult = document.getElementById('userIdCheckResult');
            if (checkResult) {
                checkResult.innerHTML = '';
                checkResult.removeAttribute('data-status');
            }
        });
    }
    
    // 비밀번호 확인 필드에 이벤트 리스너 추가
    const confirmPasswordInput = document.getElementById('confirmPassword');
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('blur', function() {
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            const result = document.getElementById('passwordCheckResult');
            
            if (confirmPassword && password !== confirmPassword) {
                result.innerHTML = '<span class="error-icon">✗</span> 비밀번호가 일치하지 않습니다.';
            } else if (confirmPassword && password === confirmPassword) {
                result.innerHTML = '<span class="check-icon">✓</span> 비밀번호가 일치합니다.';
            } else {
                result.innerHTML = '';
            }
        });
    }
    
    // 휴대폰 번호 자동 포맷팅
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function() {
            formatPhoneNumber(this);
        });
    }
    
    // 폼 애니메이션 효과
    const container = document.querySelector('.container');
    if (container) {
        container.style.opacity = '0';
        container.style.transform = 'translateY(20px)';
        
        setTimeout(function() {
            container.style.transition = 'all 0.5s ease';
            container.style.opacity = '1';
            container.style.transform = 'translateY(0)';
        }, 100);
    }
});

// 다음 우편번호 서비스 열기
function openPostcodeSearch() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
            
            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                addr += extraAddr;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById('roadAddress').value = addr;
            document.getElementById('jibunAddress').value = data.jibunAddress;

            // 상세주소 필드로 커서를 이동한다.
            document.getElementById('detailAddress').focus();
            
            // 전체 주소를 하나의 문자열로 합쳐서 hidden 필드에 저장
            updateFullAddress();
        }
    }).open();
}

// 전체 주소를 업데이트하는 함수
function updateFullAddress() {
    const postcode = document.getElementById('postcode').value;
    const roadAddress = document.getElementById('roadAddress').value;
    const detailAddress = document.getElementById('detailAddress').value;
    
    let fullAddress = '';
    
    if (postcode) {
        fullAddress += '(' + postcode + ') ';
    }
    if (roadAddress) {
        fullAddress += roadAddress;
    }
    if (detailAddress) {
        fullAddress += ' ' + detailAddress;
    }
    
    // 전체 주소를 hidden 필드에 저장
    document.getElementById('address').value = fullAddress.trim();
}

// 상세주소 입력 시 전체 주소 업데이트
document.addEventListener('DOMContentLoaded', function() {
    const detailAddressInput = document.getElementById('detailAddress');
    if (detailAddressInput) {
        detailAddressInput.addEventListener('input', updateFullAddress);
    }
}); 