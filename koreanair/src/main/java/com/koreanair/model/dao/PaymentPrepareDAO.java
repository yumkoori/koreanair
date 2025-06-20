package com.koreanair.model.dao;

import com.koreanair.model.dto.PaymentPrepareDTO;

public interface PaymentPrepareDAO {
    /**
     * 결제 정보를 데이터베이스에 저장합니다.
     * @param merchantUid 주문 고유 번호
     * @return 저장 성공 여부
     * @throws Exception 저장 중 발생하는 예외
     */
    boolean insertMerchantUid(PaymentPrepareDTO dto) throws Exception;
} 