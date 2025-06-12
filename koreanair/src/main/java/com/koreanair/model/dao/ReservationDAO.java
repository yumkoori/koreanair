package com.koreanair.model.dao;

import com.koreanair.model.dto.PassengerDTO;
import com.koreanair.model.dto.ReservationDTO;

public interface ReservationDAO {

    // Service가 호출할 수 있도록 메소드 시그니처를 정의
    ReservationDTO findReservation(String bookingId, String departureDate, String lastName, String firstName);
    
    // [수정] 예약 ID와 사용자 ID 기반으로 예약 정보를 조회하는 메소드
    ReservationDTO findReservationById(String bookingId, String userId);
    
    // --- 나머지 메소드들 ---
    void insertReservation(ReservationDTO reservation);
    void insertPassenger(PassengerDTO passenger);
    void updateReservation(ReservationDTO reservation);
    void cancelReservation(String reservationId);
}