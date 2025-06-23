package com.koreanair.model.service;

import com.koreanair.model.dao.ReservationDAO;
import com.koreanair.model.dao.ReservationDAOImpl;
import com.koreanair.model.dao.RefundDAO;         // [추가] 신규 DAO import
import com.koreanair.model.dao.RefundDAOImpl;   // [추가] 신규 DAO import
import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.dto.RefundDetailDTO;

public class BookingService {

    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final RefundDAO refundDAO = new RefundDAOImpl(); // [추가] 신규 RefundDAO 필드

    /**
     * 비회원 예약 조회를 위한 서비스 메소드
     */
    public ReservationDTO searchBooking(String bookingId, String departureDate, String lastName, String firstName) {
        return reservationDAO.findReservation(bookingId, departureDate, lastName, firstName);
    }
    
    /**
     * 예약 상세 정보 '표시'를 위한 서비스 메소드
     */
    public ReservationDTO getBookingDetailsById(String bookingId, String userId) {
        return reservationDAO.findReservationById(bookingId, userId);
    }
    
    /**
     * [기존 메소드 - 하드코딩된 값 사용]
     * 예약 정보와 '환불 예정 금액'을 '계산'하여 함께 조회하는 서비스 메소드
     * CancelReservationHandler에서 사용합니다.
     */
    public RefundDetailDTO getBookingAndRefundDetails(String bookingId, String userId) {
        // DAO는 운임, 위약금 등이 포함된 ReservationDTO를 반환해야 합니다.
        ReservationDTO reservation = reservationDAO.findReservationById(bookingId, userId);
        
        if (reservation == null) {
            return null;
        }

        // --- 환불 금액 계산 로직 (실제 구현 시 DB 값을 사용해야 함) ---
        // (시연을 위한 임시 하드코딩된 값)
        double baseFare = 5000; // 5,000 마일
        double tax = 10500;     // 10,500 원
        int penalty = 500;      // 500 마일
        
        double totalRefundMileage = baseFare - penalty;
        double totalRefundAmount = tax; // 세금은 전액 환불로 가정

        // 계산된 정보를 RefundDetailDTO에 담아 반환
        RefundDetailDTO refundDetail = new RefundDetailDTO();
        refundDetail.setReservation(reservation);
        refundDetail.setBaseFare(baseFare);
        refundDetail.setTax(tax);
        refundDetail.setPenaltyFee(penalty);
        refundDetail.setTotalRefundAmount(totalRefundAmount);
        refundDetail.setTotalRefundMileage(totalRefundMileage);
        
        return refundDetail;
    }

    /**
     * [추가된 신규 메소드 - DB 연동]
     * RefundDAO를 사용하여 DB에서 직접 환불 정보를 조회하고 계산합니다.
     */
    public RefundDetailDTO getBookingAndRefundDetailsV2(String bookingId, String userId) {
        // 1. RefundDAO를 통해 필요한 모든 정보가 담긴 DTO를 가져옵니다.
        RefundDetailDTO refundDetail = refundDAO.findRefundDetailsByBookingId(bookingId, userId);
        
        if (refundDetail == null) {
            return null; // 정보가 없으면 null 반환
        }

        // 2. Service에서 비즈니스 로직(최종 환불액 계산)을 수행합니다.
        double totalRefundMileage = refundDetail.getBaseFare() - refundDetail.getPenaltyFee();
        double totalRefundAmount = refundDetail.getTax();

        // 3. 계산된 값을 DTO에 마저 채워 넣습니다.
        refundDetail.setTotalRefundMileage(totalRefundMileage);
        refundDetail.setTotalRefundAmount(totalRefundAmount);
        
        return refundDetail; // 모든 정보가 채워진 DTO를 반환
    }

    /**
     * 예약을 '취소' 상태로 변경하는 서비스 메소드
     */
    public boolean cancelBooking(String bookingId) {
        try {
            // DB의 booking 테이블 status를 'CANCELLED'로 업데이트
            reservationDAO.cancelReservation(bookingId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}