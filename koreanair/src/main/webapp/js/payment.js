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
        case 'credit_card':
            document.getElementById('creditCardNotice').style.display = 'block';
            break;
        case 'kakao_pay':
            document.getElementById('kakaoPayNotice').style.display = 'block';
            break;
        case 'toss_pay':
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

function processKakaoPayment() {
    const merchantUid = 'ORD' + new Date().getTime(); // 주문번호 생성
    
    IMP.request_pay({
        pg: "kakaopay.TC0ONETIME", // 카카오페이 PG설정
        pay_method: "card",
        merchant_uid: merchantUid,
        name: "대한항공 항공권",
        amount: 100000, // 실제 금액으로 변경하세요 (테스트용 100000원)
        buyer_email: "test@koreanair.com",
        buyer_name: "김도훈",
        buyer_tel: "010-1234-5678",
        buyer_addr: "서울특별시 강남구",
        buyer_postcode: "123-456"
    }, function (rsp) {
        if (rsp.success) {
            // 결제 성공
            console.log('결제 성공:', rsp);
            alert(`결제가 완료되었습니다.\n주문번호: \n결제금액: 원`);
            
            // 서버로 결제 정보 전송
            const form = document.getElementById("reserv_form");
            
            // 결제 정보를 폼에 추가
            const impUidInput = document.createElement('input');
            impUidInput.type = 'hidden';
            impUidInput.name = 'imp_uid';
            impUidInput.value = rsp.imp_uid;
            form.appendChild(impUidInput);
            
            const merchantUidInput = document.createElement('input');
            merchantUidInput.type = 'hidden';
            merchantUidInput.name = 'merchant_uid';
            merchantUidInput.value = rsp.merchant_uid;
            form.appendChild(merchantUidInput);
            
            const amountInput = document.createElement('input');
            amountInput.type = 'hidden';
            amountInput.name = 'amount';
            amountInput.value = rsp.paid_amount;
            form.appendChild(amountInput);
            
            // 서버로 전송
            form.method = "post";
            form.action = "/reserv/reservInsert"; // 실제 서버 URL로 변경
            form.submit();
            
        } else {
            // 결제 실패
            console.log('결제 실패:', rsp);
            alert(`결제에 실패하였습니다.\n에러 내용: `);
        }
    });
}

// 결제 처리
function processPayment() {
    const selectedPayment = document.querySelector('input[name="paymentMethod"]:checked');
    
    if (!selectedPayment) {
        alert('결제 수단을 선택해주세요.');
        return;
    }
    
    const paymentMethod = selectedPayment.value;
    
    // 실제 결제 처리 로직
    switch(paymentMethod) {
        case 'kakao_pay':
            processKakaoPayment();
            break;
        case 'toss_pay':
            alert('토스페이 결제 페이지로 이동합니다.');
            // 실제로는 토스페이 API 호출
            // location.href = '/payment/toss';
            break;
            
        default:
            alert(paymentMethod + ' 결제 페이지로 이동합니다.');
            // 각 결제 수단별 처리
            break;
    }
} 