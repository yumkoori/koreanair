package com.koreanair.model.dao;

import com.koreanair.model.dto.PaymentDTO;
import com.koreanair.model.dto.PaymentErrorDTO;

public interface PaymentDAO {

	// 결제 정보 등록 (결제 성공 시 저장)
    void insertPayment(PaymentDTO payment);
    
    // 특정 예약번호에 대한 결제 정보 조회
    PaymentDTO getPaymentByReservationId(String reservationId);
    
    // 특정 결제번호로 결제 정보 조회
    PaymentDTO getPaymentByPaymentId(String paymentId);
    
    // 결제 상태 업데이트 (ex: 결제 완료 → 취소 요청 → 취소 완료 등) //Enum으로 바꾸는것 고려 
    void updatePaymentStatus(String paymentId, String newStatus);
    
    // 결제 오류 내역 등록 (ex: 중복 결제, 승인 실패 등)
    void insertPaymentError(PaymentErrorDTO paymentError);
}
