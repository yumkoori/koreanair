package com.koreanair.model.service;

import java.sql.SQLException;

import com.koreanair.model.dto.PaymentPrepareDTO;
import com.koreanair.model.dao.PaymentPrepareDAO;
import com.koreanair.model.dao.PaymentPrepareDAOImpl;

public class PaymentPrepareService {

    private final PaymentPrepareDAO paymentPrepareDAO;

    public PaymentPrepareService() {
        this.paymentPrepareDAO = new PaymentPrepareDAOImpl();
    }

    /**
     * 결제 준비 처리 (유효성 검증 + 로직 판단 + DAO 호출)
     * @param dto 결제 준비 정보
     * @return 처리 성공 여부
     * @throws IllegalArgumentException 입력값이 유효하지 않은 경우
     * @throws Exception 처리 중 발생하는 예외
     */
    public boolean processPaymentPrepare(PaymentPrepareDTO dto) throws Exception {
        // 1. 파라미터 유효성 검증
        validateParameters(dto);
        
        // 2. 결제 금액 유효성 검증
        validateAmount(dto.getAmount());
        
        // 3. 비즈니스 로직 검증
        validateBusinessLogic(dto);
        
        // 4. DAO 호출하여 데이터 저장
        return savePaymentInfo(dto);
    }

    /**
     * 결제 정보를 저장합니다.
     * @param dto 결제 준비 정보
     * @return 저장 성공 여부
     * @throws Exception 저장 중 발생하는 예외
     */
    private boolean savePaymentInfo(PaymentPrepareDTO dto) throws Exception {
        try {
            // DAO를 통해 결제 정보 저장
            boolean result = paymentPrepareDAO.insertMerchantUid(dto);
            
            if (result) {
                System.out.println("[SUCCESS] 결제 정보 저장 성공 - MerchantUid: " + dto.getMerchantUid());
            } else {
                System.err.println("[WARNING] 결제 정보 저장 실패 - MerchantUid: " + dto.getMerchantUid());
            }
            
            return result;
            
        } catch (SQLException e) {
            // 데이터베이스 관련 예외 처리
            System.err.println("[DB ERROR] 데이터베이스 오류 발생 - MerchantUid: " + dto.getMerchantUid() + ", Error: " + e.getMessage());
            throw new Exception("데이터베이스 연결 또는 쿼리 실행 중 오류가 발생했습니다.", e);
            
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("[SYSTEM ERROR] 시스템 오류 발생 - MerchantUid: " + dto.getMerchantUid() + ", Error: " + e.getMessage());
            throw new Exception("결제 정보 저장 중 예상치 못한 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 파라미터 유효성 검증
     * @param dto 검증할 DTO 객체
     * @throws IllegalArgumentException 유효하지 않은 입력값인 경우
     */
    private void validateParameters(PaymentPrepareDTO dto) throws IllegalArgumentException {
        if (dto == null) {
            throw new IllegalArgumentException("결제 정보가 누락되었습니다.");
        }
        
        // 주문번호 검증
        String merchantUid = dto.getMerchantUid();
        if (merchantUid == null || merchantUid.trim().isEmpty()) {
            throw new IllegalArgumentException("주문번호가 누락되었습니다.");
        }
        
        // 예약번호 검증
        String bookingId = dto.getBookingId();
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("예약번호가 누락되었습니다.");
        }
        
        // 결제방법 검증
        String paymentMethod = dto.getPayment_method();
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("결제방법이 누락되었습니다.");
        }
        
        // 결제금액 검증
        String amount = dto.getAmount();
        if (amount == null || amount.trim().isEmpty()) {
            throw new IllegalArgumentException("결제금액이 누락되었습니다.");
        }
        
        // 생성시간 검증
        String createdAt = dto.getCreated_at();
        if (createdAt == null || createdAt.trim().isEmpty()) {
            throw new IllegalArgumentException("생성시간이 누락되었습니다.");
        }
        
        System.out.println("[VALIDATION] 파라미터 검증 완료 - MerchantUid: " + merchantUid);
    }
    
    /**
     * 결제 금액 유효성 검증
     * @param amount 결제 금액 문자열
     * @throws IllegalArgumentException 금액이 유효하지 않은 경우
     */
    private void validateAmount(String amount) throws IllegalArgumentException {
        try {
            double amountValue = Double.parseDouble(amount);
            if (amountValue <= 0) {
                throw new IllegalArgumentException("결제금액은 0보다 커야 합니다.");
            }
            if (amountValue > 10000000) { // 1천만원 한도
                throw new IllegalArgumentException("결제금액이 한도를 초과했습니다. (최대 10,000,000원)");
            }
            System.out.println("[VALIDATION] 결제금액 검증 완료 - Amount: " + amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("결제금액 형식이 올바르지 않습니다.");
        }
    }
    
    /**
     * 비즈니스 로직 검증
     * @param dto 검증할 DTO 객체
     * @throws IllegalArgumentException 비즈니스 규칙에 위배되는 경우
     */
    private void validateBusinessLogic(PaymentPrepareDTO dto) throws IllegalArgumentException {
        // 결제방법 유효성 검증
        String paymentMethod = dto.getPayment_method();
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new IllegalArgumentException("지원하지 않는 결제방법입니다: " + paymentMethod);
        }
    }
    
    /**
     * 지원하는 결제방법인지 검증
     * @param paymentMethod 결제방법
     * @return 유효한 결제방법 여부
     */
    private boolean isValidPaymentMethod(String paymentMethod) {
        // 지원하는 결제방법 목록
        String[] validMethods = {"card", "kakaopay", "tosspay"};
        
        for (String method : validMethods) {
            if (method.equalsIgnoreCase(paymentMethod)) {
                return true;
            }
        }
        return false;
    }

    /**
     * PaymentPrepareDTO 유효성 검증 (기존 메서드 - 하위 호환성을 위해 유지)
     * @param dto 검증할 DTO 객체
     * @throws IllegalArgumentException 유효하지 않은 입력값인 경우
     */
    private void validatePaymentPrepareDTO(PaymentPrepareDTO dto) throws IllegalArgumentException {
        if (dto == null) {
            throw new IllegalArgumentException("결제 정보가 누락되었습니다.");
        }
        
        String merchantUid = dto.getMerchantUid();
        if (merchantUid == null || merchantUid.trim().isEmpty()) {
            throw new IllegalArgumentException("주문 번호(MerchantUid)는 필수값입니다.");
        }
        
        System.out.println("[VALIDATION PASS] 입력값 검증 완료 - MerchantUid: " + merchantUid);
    }
}

