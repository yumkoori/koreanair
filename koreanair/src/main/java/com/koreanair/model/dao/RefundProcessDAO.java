package com.koreanair.model.dao;

import com.koreanair.model.dto.RefundProcessDTO;

public interface RefundProcessDAO {
    
    /**
     * 위변조 검사를 수행하고 merchant_uid를 반환
     * @param dto 환불 검증 정보 (bookingId, userNo)
     * @return merchant_uid (검증 성공시), null (검증 실패시)
     * @throws Exception 검사 중 발생하는 예외
     */
    String validateAndGetMerchantUid(RefundProcessDTO dto) throws Exception;
    
    /**
     * 환불 상태를 업데이트
     * @param merchantUid 주문 고유번호
     * @param status 새로운 상태 (CANCELLED, FAILED 등)
     * @return 업데이트 성공 여부
     * @throws Exception 업데이트 중 발생하는 예외
     */
    boolean updateRefundStatus(String merchantUid, String status) throws Exception;
    
    /**
     * merchant_uid로 결제 금액 조회
     * @param merchantUid 주문 고유번호
     * @return 결제 금액 (조회 성공시), null (조회 실패시)
     * @throws Exception 조회 중 발생하는 예외
     */
    String getPaymentAmount(String merchantUid) throws Exception;
} 