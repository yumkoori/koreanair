package com.koreanair.model.dao;

import java.sql.Connection;
import com.koreanair.model.dto.PaymentPrepareDTO;

public interface PaymentPrepareDAO {
    /**
     * 결제 정보를 데이터베이스에 저장합니다.
     * @param merchantUid 주문 고유 번호
     * @return 저장 성공 여부
     * @throws Exception 저장 중 발생하는 예외
     */
    boolean insertMerchantUid(PaymentPrepareDTO dto) throws Exception;
    
    /**
     * payment_request_log 테이블에 결제 요청 로그를 저장합니다.
     * @param paymentId 결제 ID
     * @param merchantUid 주문 고유 번호
     * @param payMethod 결제 방법
     * @param requestedAmount 요청 금액
     * @param requestedAt 요청 시간
     * @return 저장 성공 여부
     * @throws Exception 저장 중 발생하는 예외
     */
    boolean insertPaymentRequestLog(String paymentId, String merchantUid, String payMethod, 
                                  String requestedAmount, String requestedAt) throws Exception;
    
    /**
     * 트랜잭션을 사용하여 payment와 payment_request_log 테이블에 동시에 저장합니다.
     * @param dto 결제 준비 정보
     * @return 저장 성공 여부
     * @throws Exception 저장 중 발생하는 예외
     */
    boolean insertPaymentWithLog(PaymentPrepareDTO dto) throws Exception;
} 