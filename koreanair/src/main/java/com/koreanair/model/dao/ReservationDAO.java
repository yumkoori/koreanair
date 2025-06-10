package com.koreanair.model.dao;

import com.koreanair.model.dto.PassengerDTO;
import com.koreanair.model.dto.ReservationDTO;

public interface ReservationDAO {
	
    // 예약 등록
    void insertReservation(ReservationDTO reservation);
	
    // 탑승객 정보 등록 (예약 시 다중 탑승객 가능)
    void insertPassenger(PassengerDTO passenger);
    
    // 예약번호 기반 예약 조회 
    ReservationDTO getReservationByReservationNumber(String reservationNumber, String passengerName);
    
    // 예약 변경 처리 (좌석 변경, 여정 변경 시 관련 정보 업데이트)
    void updateReservation(ReservationDTO reservation);
    
    // 예약 취소 처리 (취소 상태 업데이트 및 관련 부가서비스, 결제 처리 트리거 가능)
    void cancelReservation(String reservationId);
    
}
