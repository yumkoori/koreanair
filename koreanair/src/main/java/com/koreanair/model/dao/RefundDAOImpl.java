package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.dto.RefundDetailDTO;
import com.koreanair.util.DBConnection;

public class RefundDAOImpl implements RefundDAO {

    @Override
    public RefundDetailDTO findRefundDetailsByBookingId(String bookingId, String userId) {
        // [최종 수정] 존재하지 않는 fare 테이블 대신, seat_class 테이블을 사용하도록 SQL을 수정했습니다.
        String sql = "SELECT "
                       + "    b.booking_id, f.flight_id, f.departure_time, f.arrival_time, "
                       + "    p.last_name, p.first_name, u.phone, u.email, u.user_id AS member_id, "
                       + "    sc.class_name AS cabin_class, "
                       + "    da.airport_id AS departure_airport_id, da.airport_name AS departure_airport_name, "
                       + "    aa.airport_id AS arrival_airport_id, aa.airport_name AS arrival_airport_name, "
                       + "    sc.base_price AS base_fare, "
                       + "    0 AS tax, "
                       + "    sc.cancellation_penalty AS penalty_fee "
                       + "FROM booking b "
                       + "JOIN flight f ON b.flight_id = f.flight_id "
                       + "JOIN passenger p ON b.booking_id = p.booking_id "
                       + "JOIN users u ON p.user_no = u.user_no "
                       + "JOIN airport da ON f.departure_airport_id = da.airport_id "
                       + "JOIN airport aa ON f.arrival_airport_id = aa.airport_id "
                       + "LEFT JOIN booking_seat bs ON b.booking_id = bs.booking_id "
                       + "LEFT JOIN flight_seat fs ON bs.flight_seat_id = fs.seat_id "
                       + "LEFT JOIN seat_class sc ON fs.class_id = sc.class_id "
                       + "WHERE b.booking_id = ? AND u.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookingId);
            pstmt.setString(2, userId);
            
            System.out.println("--- RefundDAOImpl Query-Debug ---");
            System.out.println("실행될 SQL: " + sql.replaceAll("\\s+", " ").trim());
            System.out.println("파라미터 1 (bookingId): " + bookingId);
            System.out.println("파라미터 2 (userId): " + userId);
            System.out.println("------------------------------------");

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 1. DTO 객체들 생성
                    ReservationDTO reservation = new ReservationDTO();
                    RefundDetailDTO refundDetail = new RefundDetailDTO();

                    // 2. ResultSet -> ReservationDTO 매핑
                    reservation.setBookingId(rs.getString("booking_id"));
                    reservation.setFlightId(rs.getString("flight_id"));
                    reservation.setDepartureTime(rs.getTimestamp("departure_time"));
                    reservation.setArrivalTime(rs.getTimestamp("arrival_time"));
                    reservation.setLastName(rs.getString("last_name"));
                    reservation.setFirstName(rs.getString("first_name"));
                    reservation.setDepartureAirportId(rs.getString("departure_airport_id"));
                    reservation.setDepartureAirportName(rs.getString("departure_airport_name"));
                    reservation.setArrivalAirportId(rs.getString("arrival_airport_id"));
                    reservation.setArrivalAirportName(rs.getString("arrival_airport_name"));

                    // 3. ResultSet -> RefundDetailDTO 의 환불 관련 필드 매핑
                    refundDetail.setBaseFare(rs.getDouble("base_fare"));
                    refundDetail.setTax(rs.getDouble("tax"));
                    refundDetail.setPenaltyFee(rs.getInt("penalty_fee"));

                    // 4. 완성된 reservation 객체를 refundDetail 객체에 설정
                    refundDetail.setReservation(reservation);

                    return refundDetail;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // 조회 실패 시 null 반환
    }
}