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

// 회원가입 폼 검증
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
    const checkResult = document.getElementById('userIdCheckResult');
    
    // 필수 필드 체크
    if (!userId || !password || !confirmPassword || !koreanName || !englishName || 
        !birthDate || !gender || !email || !phone) {
        alert('모든 필수 항목을 입력해주세요.');
        return false;
    }
    
    // 아이디 길이 체크
    if (userId.length < 3) {
        alert('아이디는 3자 이상이어야 합니다.');
        return false;
    }
    
    // 아이디 중복 체크 결과 확인
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
    
    // 비밀번호 길이 체크
    if (password.length < 4) {
        alert('비밀번호는 4자 이상이어야 합니다.');
        return false;
    }
    
    // 비밀번호 확인
    if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return false;
    }
    
    // 한글 이름 검증 (한글만 허용)
    const koreanNameRegex = /^[가-힣\s]+$/;
    if (!koreanNameRegex.test(koreanName)) {
        alert('한글 이름은 한글만 입력 가능합니다.');
        return false;
    }
    
    // 영문 이름 검증 (영문과 공백만 허용)
    const englishNameRegex = /^[a-zA-Z\s]+$/;
    if (!englishNameRegex.test(englishName)) {
        alert('영문 이름은 영문자만 입력 가능합니다.');
        return false;
    }
    
    // 생년월일 검증 (미래 날짜 불가)
    const today = new Date();
    const selectedDate = new Date(birthDate);
    if (selectedDate >= today) {
        alert('생년월일은 오늘 이전 날짜여야 합니다.');
        return false;
    }
    
    // 나이 검증 (만 14세 이상)
    const age = today.getFullYear() - selectedDate.getFullYear();
    const monthDiff = today.getMonth() - selectedDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < selectedDate.getDate())) {
        age--;
    }
    if (age < 14) {
        alert('만 14세 이상만 가입 가능합니다.');
        return false;
    }
    
    // 이메일 형식 체크
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert('올바른 이메일 형식을 입력해주세요.');
        return false;
    }
    
    // 휴대폰 번호 형식 체크
    const phoneRegex = /^01[0-9]-\d{3,4}-\d{4}$/;
    if (!phoneRegex.test(phone)) {
        alert('휴대폰 번호는 010-1234-5678 형식으로 입력해주세요.');
        return false;
    }
    
    return true;
}

// 로그인 폼 검증
function validateLoginForm() {
    const userId = document.getElementById('userId').value;
    const password = document.getElementById('password').value;
    
    if (!userId || !password) {
        alert('아이디와 비밀번호를 입력해주세요.');
        return false;
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

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
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