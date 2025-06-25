let merchantUid = null;
// URL에서 받아올 예정
let paymentAmount = null;
let bookingId = null;

// URL 파라미터에서 값을 받아오는 함수
function getUrlParameters() {
    const urlParams = new URLSearchParams(window.location.search);
    
    // paymentAmount 파라미터 받아오기
    const amountParam = urlParams.get('totalAmount');
    //if문안에 amountParam
    if (true) {
		paymentAmount = parseInt(amountParam);
        console.log('URL에서 받아온 결제금액:', paymentAmount);
    }
    
    // bookingId 파라미터 받아오기
    const bookingParam = urlParams.get('bookingId');
    //if문안에 bookingParam
    if (true) {
		bookingId = bookingParam;
        console.log('URL에서 받아온 예약ID:', bookingId);
    }
}
    
// 페이지 로드 시 URL 파라미터 읽기
document.addEventListener('DOMContentLoaded', function() {
    getUrlParameters();
});

// 결제 수단 선택 처리
document.querySelectorAll('input[name="paymentMethod"]').forEach(radio => {
    radio.addEventListener('change', function() {
        // 모든 결제 수단 선택 해제
        document.querySelectorAll('.payment-method').forEach(method => {
            method.classList.remove('selected');
        });
        
        // 선택된 결제 수단 표시
        this.closest('.payment-method').classList.add('selected');
        
        // 유의사항 업데이트
        updateNoticeContent(this.value);
    });
});

// 유의사항 내용 업데이트 함수
function updateNoticeContent(paymentMethod) {
    // 모든 유의사항 숨기기
    document.querySelectorAll('.payment-notice').forEach(notice => {
        notice.style.display = 'none';
    });
    
    // 선택된 결제 수단에 해당하는 유의사항 표시
    switch(paymentMethod) {
        case 'creditcard':
            document.getElementById('creditCardNotice').style.display = 'block';
            break;
        case 'kakaopay':
            document.getElementById('kakaoPayNotice').style.display = 'block';
            break;
        case 'tosspay':
            document.getElementById('tossPayNotice').style.display = 'block';
            break;
        default:
            document.getElementById('creditCardNotice').style.display = 'block';
            break;
    }
}

// 클릭으로도 선택 가능하게
document.querySelectorAll('.payment-method').forEach(method => {
    method.addEventListener('click', function() {
        const radio = this.querySelector('input[type="radio"]');
        radio.checked = true;
        radio.dispatchEvent(new Event('change'));
    });
});

// 신용카드 결제 처리
function processcreditcardPayment() {
    if (!merchantUid) {
        alert('결제 준비 정보가 없습니다.');
        return;
    }
    
    IMP.init("imp87380624");
    
    IMP.request_pay({
        pg: "kicc.T5102001", // 신용카드 kicc PG설정
        pay_method: "card",
        merchant_uid: merchantUid,
        name: "대한항공 항공권 신용카드결제",
        amount: paymentAmount,
        
        buyer_email: "test@koreanair.com",
        buyer_name: "김도훈",
        buyer_tel: "010-1234-5678",
        buyer_addr: "서울특별시 강남구",
        buyer_postcode: "123-456"
    }, function (rsp) {
        if (rsp.success) {
            // 결제 성공
            console.log('결제 성공:', rsp);
            alert(`결제가 완료되었습니다.\n주문번호: ${rsp.merchant_uid}\n결제금액: ${paymentAmount.toLocaleString()}원`);
            
            // 서버로 결제 검증 요청
            submitPaymentVerification(rsp.imp_uid);
        } else {
            // 결제 실패
            console.log('결제 실패:', rsp);
            alert(`결제에 실패하였습니다.\n에러 내용: ${rsp.error_msg}`);
        }
    });
}

// 카카오페이 결제 처리
function processKakaoPayment() {
	
	const btn = document.getElementById("payBtn");
    btn.disabled = true;
	
    if (!merchantUid) {
        alert('결제 준비 정보가 없습니다.');
        return;
    }
    
    const IMP = window.IMP;
    IMP.init("imp87380624");
    
    IMP.request_pay({
        pg: "kakaopay.TC0ONETIME", // 카카오페이 PG설정
        pay_method: "kakaopay",
        merchant_uid: merchantUid,
        name: "대한항공 항공권 카카오페이 결제",
        amount: paymentAmount,
        buyer_email: "test@koreanair.com",
        buyer_name: "김도훈",
        buyer_tel: "010-1234-5678",
        buyer_addr: "서울특별시 강남구",
        buyer_postcode: "123-456"
    }, function (rsp) {
        if (rsp.success) {
            // 결제 성공
            console.log('카카오페이 결제 성공:', rsp);
            alert(`결제가 완료되었습니다.\n주문번호: ${rsp.merchant_uid}\n결제금액: ${paymentAmount.toLocaleString()}원`);
            
            // 서버로 결제 검증 요청
            submitPaymentVerification(rsp.imp_uid);
        } else {
            // 결제 실패
            console.log('결제 실패:', rsp);
            alert(`결제에 실패하였습니다.\n에러 내용: ${rsp}`);
        }
    });
}

// 토스페이 결제 처리
function processtossPayment() {
    if (!merchantUid) {
        alert('결제 준비 정보가 없습니다.');
        return;
    }
    
    IMP.init("imp87380624");
    
    IMP.request_pay({
        pg: "tosspay.tosstest", // 토스페이 PG설정
        pay_method: "tosspay",
        merchant_uid: merchantUid,
        name: "대한항공 항공권 토스페이 결제",
        amount: paymentAmount,
        
        buyer_email: "test@koreanair.com",
        buyer_name: "김도훈",
        buyer_tel: "010-1234-5678",
        buyer_addr: "서울특별시 강남구",
        buyer_postcode: "123-456"
    }, function (rsp) {
        if (rsp.success) {
            // 결제 성공
            console.log('토스페이 결제 성공:', rsp);
            alert(`결제가 완료되었습니다.\n주문번호: ${rsp.merchant_uid}\n결제금액: ${paymentAmount.toLocaleString()}원`);
            
            // 서버로 결제 검증 요청
            submitPaymentVerification(rsp.imp_uid);
        } else {
            // 결제 실패
            console.log('결제 실패:', rsp);
            alert(`결제에 실패하였습니다.\n에러 내용: ${rsp.error_msg}`);
        }
    });
}

// 결제 검증을 위한 서버 요청
function submitPaymentVerification(impUid) {
    const form = document.getElementById("reserv_form");
    
    // 결제 정보를 폼에 추가
    const impUidInput = document.createElement('input');
    impUidInput.type = 'hidden';
    impUidInput.name = 'imp_uid';
    impUidInput.value = impUid;
    form.appendChild(impUidInput);
    
    const merchantUidInput = document.createElement('input');
    merchantUidInput.type = 'hidden';
    merchantUidInput.name = 'merchant_uid';
    merchantUidInput.value = merchantUid;
    form.appendChild(merchantUidInput);
    
    // 서버로 전송
    form.method = "post";
    form.action = contextPath + "/verifyPayment.do";
    form.submit();
}

// 결제 준비 정보를 서버에 저장하는 함수 (개선된 버전)
function Transmission(paymentMethod, amount, booking_Id, callback) {
    // 입력값 검증
    if (!paymentMethod) {
        alert('결제 수단이 선택되지 않았습니다.');
        return;
    }
    
    if (!amount || amount <= 0) {
        alert('유효하지 않은 결제 금액입니다.');
        return;
    }
    
    if (!booking_Id) {
        alert('예약 ID가 없습니다.');
        return;
    }
    
    const now = new Date();
    const createdAt = now.getFullYear() + '-' + 
        String(now.getMonth() + 1).padStart(2, '0') + '-' + 
        String(now.getDate()).padStart(2, '0') + ' ' +
        String(now.getHours()).padStart(2, '0') + ':' +
        String(now.getMinutes()).padStart(2, '0') + ':' +
        String(now.getSeconds()).padStart(2, '0');
    
    // 고유한 상점 주문번호 생성
    merchantUid = 'ORD' + new Date().getTime();
    
    console.log('결제 준비 정보:', {
        merchantUid: merchantUid,
        bookingId: booking_Id,
        paymentMethod: paymentMethod,
        amount: amount,
        createdAt: createdAt
    });
    
    fetch(contextPath + "/savemerchantUid.do", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'merchantUid=' + encodeURIComponent(merchantUid) +
              '&bookingId=' + encodeURIComponent(booking_Id) +
              '&payment_method=' + encodeURIComponent(paymentMethod) +
              '&amount=' + encodeURIComponent(amount) +
              '&created_at=' + encodeURIComponent(createdAt)
    })
    .then(response => response.text())
    .then(result => {
        if (result === 'success') {
            console.log('결제 준비 정보 저장 성공');
            if (callback) {
                callback(merchantUid);
            }
        } else if (result.startsWith('invalid_input:')) {
            alert("입력값 오류: " + result.substring(14));
        } else if (result.startsWith('system_error:')) {
            alert("시스템 오류: " + result.substring(13));
        } else {
            alert("서버 오류 또는 저장 실패: " + result);
        }
    })
    .catch(error => {
        console.error("서버 통신 오류: ", error);
        alert("네트워크 오류가 발생했습니다.");
    });
}

// 결제 처리 메인 함수
function processPayment() {
    const selectedPayment = document.querySelector('input[name="paymentMethod"]:checked');
    
    if (!selectedPayment) {
        alert('결제 수단을 선택해주세요.');
        return;
    }
    
    // URL에서 받아온 bookingId가 없으면 에러
    if (!bookingId) {
        alert('예약 정보가 없습니다. 다시 시도해주세요.');
        return;
    }
    
    const paymentMethod = selectedPayment.value;
    
    // 결제 수단별 처리
    switch (paymentMethod) {
        case 'card':
            Transmission(paymentMethod, paymentAmount, bookingId, function() {
                processcreditcardPayment();
            });
            break;

        case 'kakaopay':
            Transmission(paymentMethod, paymentAmount, bookingId, function() {
                processKakaoPayment();
            });
            break;

        case 'tosspay':
            Transmission(paymentMethod, paymentAmount, bookingId, function() {
                processtossPayment();
            });
            break;
            
        default:
            alert('지원되지 않는 결제 수단입니다.');
            break;
    }
}