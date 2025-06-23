package com.koreanair.model.dao;

import com.koreanair.model.dto.PaymentCompareDTO;

/**
 * 결제 검증을 위한 DAO 인터페이스
 */
public interface PaymentVerifyDAO {
    
    /**
     * imp_uid로 DB에서 merchant_uid, amount, pay_method를 조회
     * @param impUid 아임포트 결제 고유번호
     * @return PaymentCompareDTO 검증용 결제 정보 (merchant_uid, amount, pay_method)
     * @throws Exception 조회 중 오류 발생 시
     */
    PaymentCompareDTO getPaymentCompareInfo(String impUid) throws Exception;
    
    /**
     * merchant_uid로 DB에서 merchant_uid와 amount를 조회 (아임포트 데이터와 비교용)
     * @param merchantUid 가맹점 주문번호
     * @return PaymentCompareDTO 검증용 결제 정보 (merchant_uid, amount)
     * @throws Exception 조회 중 오류 발생 시
     */
    PaymentCompareDTO getPaymentCompareInfoByMerchantUid(String merchantUid) throws Exception;
    
    /**
     * merchant_uid로 결제 상태를 PAID로 업데이트하고 paid_at 시간을 설정
     * @param merchantUid 가맹점 주문번호
     * @return 업데이트 성공 여부
     * @throws Exception 업데이트 중 오류 발생 시
     */
    boolean updatePaymentStatusToPaid(String merchantUid) throws Exception;
} 