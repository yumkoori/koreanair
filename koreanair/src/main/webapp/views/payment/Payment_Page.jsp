<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제화면 - 대한항공</title>
    <script src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
    <link rel="stylesheet" href="../css/payment.css">
</head><body>
    <header class="header">
        <div class="header-content">
        </div>
    </header>

    <div class="container">
        <div class="payment-section">
            <!-- 결제 수단 선택 -->
            <div class="payment-methods-grid">
                <label class="payment-method">
                    <input type="radio" name="paymentMethod" value="card">
                    <div class="credit_card">
                        <div style="font-size: 1.3rem;">
                            <img src="../images/credit_card.svg" alt="카카오페이 로고" style="height: 55px; width: 55px; vertical-align: middle;">
                            <span style="font-weight: bold;"> 신용/체크카드</span>
                        </div>
                    </div>
                </label>

                <!-- KakaoPay -->
                <label class="payment-method">
                    <input type="radio" name="paymentMethod" value="kakaopay">
                    <div class="kakao_pay" style="font-size: 0.9rem;border-top-width: 0px;padding-bottom: 0px;padding-top: 0px;border-bottom-width: 0px;height: 65px;">
                        <img src="../images/kakaopay.png" alt="카카오페이 로고" style="height: 120px; width: 140px;">
                    </div>
                </label>

                <!-- TossPay -->
                <label class="payment-method">
                    <input type="radio" name="paymentMethod" value="tosspay">
                    <div class="toss_pay" style="font-size: 0.9rem;">
                        <img src="../images/tosspay.svg" alt="토스페이 로고">
                    </div>
                </label>
            </div>

            <!-- 자동 수수 결제수단으로 저장 체크박스 -->
            <div class="checkbox-section">
                <div class="checkbox-item">
                    <input type="checkbox" id="savePayment" name="savePayment">
                    <label for="savePayment">자동 수수 결제수단으로 저장</label>
                </div>
            </div>

            <!-- 유의사항 -->
            <div class="notice-section">
                <div class="notice-title">
                    유의사항
                </div>
                <div class="notice-content" id="noticeContent">
                    <!-- 신용/체크카드 유의사항 -->
                    <div id="creditCardNotice" class="payment-notice">
                        <div class="notice-item">구매 후 항공권의 결제 수단은 변경할 수 없습니다.</div>
                        <div class="notice-item">대한항공카드가 있다면 최초 등록 후 비밀번호로 간편하게 결제해 보세요.</div>
                        <div class="notice-item">대한항공카드 간편결제는 한국 출발 여정을 원화(KRW)로 결제하는 경우 이용할 수 있으며 한국어 사이트에서만 지원됩니다.</div>
                        <div class="notice-item">가족카드 소지자는 결제 수단을 [신용/체크카드] > '현대카드'로 선택해 결제해 주세요.</div>
                        <div class="notice-item">간편결제 등록/결제 오류 등 관련 문의는 대한항공카드 고객센터(현대카드) 1588-7300로 연락해 주시기 바랍니다.</div>
                    </div>
                    
                    <!-- 카카오페이 유의사항 -->
                    <div id="kakaoPayNotice" class="payment-notice" style="display: none;">
                        <div class="notice-item">구매 후 항공권의 결제 수단은 변경할 수 없습니다.</div>
                        <div class="notice-item">카카오페이는 카카오톡 내에서 비밀번호 또는 지문 인증을 통해, 신용카드 및 카카오페이머니로 결제할 수 있는 간편결제 서비스입니다.</div>
                        <div class="notice-item">본인명의 스마트폰 카카오톡 내에서 본인인증된 신용카드 또는 본인명의 계좌로 카카오페이머니 충전 후 사용 가능합니다.</div>
                        <div class="notice-item">카카오페이 결제 시 신용카드 무이자 할부 및 마일리지 예약은 카카오페이의 규정을 따릅니다.</div>
                        <div class="notice-item">카카오페이 결제 서비스 이용시간은 1월 첫째 200명한정이며, 온택 점검 시간(23:30~00:30) 동안 서비스 이용이 제한될 수 있습니다.</div>
                        <div class="notice-item">카카오페이 관련 궁금사항/FAQ는 카카오페이에 에페리터서(설에서 확인하시거, 기타 문의사 상담은 카카오페이 고객센터(1644-7405)로 문의하여 주시기 바랍니다.</div>
                    </div>
                    
                    <!-- 토스페이 유의사항 -->
                    <div id="tossPayNotice" class="payment-notice" style="display: none;">
                        <div class="notice-item">구매 후 항공권의 결제 수단은 변경할 수 없습니다.</div>
                        <div class="notice-item">토스페이의 간편함이 대한항공으로 이어집니다. 계좌 및 카드 등록 후 비밀번호 하나로 간편하게 결제하세요!</div>
                        <div class="notice-item">카드사별 무이자 할부, 청구 할인 혜택은 토스페이 내 혜택 안내를 통해 확인하실 수 있습니다.</div>
                        <div class="notice-item">토스페이 결제 문의, 토스페이 고객센터 : 1599-4905</div>
                    </div>
                </div>
            </div>
        </div>
        <br>

        <div>
            <button class="payment-button" onclick="processPayment()">
                결제하기
            </button>
            <form id="reserv_form">
               
            </form>
            <form id="failure_log_form">
                
            </form>
        </div>
    </div>

    <script>const contextPath = "${pageContext.request.contextPath}";</script>
	<script src="../js/payments.js"></script>
</body>
</html>