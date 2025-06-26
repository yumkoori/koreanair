package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.koreanair.model.dto.PassengerDTO;
import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.util.DBConnection; // ◀◀◀ 변경점 1: DBconn2를 DBConnection으로 수정

public class ReservationDAOImpl implements ReservationDAO {

    // findReservation: 스키마 기반 최종 SQL
    @Override
    public ReservationDTO findReservation(String reservationId, String departureDate, String lastName, String firstName) {
        ReservationDTO reservation = null;
        String sql = getFinalQuery() + " WHERE b.booking_id = ? AND p.last_name = ? AND p.first_name = ? AND DATE(f.departure_time) = ? GROUP BY b.booking_id";

        // ◀◀◀ 변경점 2: DBconn2.getConnection()을 DBConnection.getConnection()으로 수정
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reservationId);
            pstmt.setString(2, lastName);
            pstmt.setString(3, firstName);
            pstmt.setString(4, departureDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    reservation = mapResultSetToReservationDTO(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservation;
    }

    // [수정] findReservationById: 스키마 기반 최종 SQL
    @Override
    public ReservationDTO findReservationById(String bookingId, String userId) {
        ReservationDTO reservation = null;
        
        // [수정 시작] SQL을 동적으로 구성하도록 변경
        StringBuilder sqlBuilder = new StringBuilder(getFinalQuery());
        sqlBuilder.append(" WHERE b.booking_id = ?");

        // userId가 null이 아니고 비어있지 않은 경우에만 userId 조건을 추가
        if (userId != null && !userId.trim().isEmpty()) {
            sqlBuilder.append(" AND u.user_id = ?");
        }
        
        sqlBuilder.append(" GROUP BY b.booking_id");
        
        String sql = sqlBuilder.toString();
        // [수정 끝]

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // [수정 시작] 파라미터 인덱스를 동적으로 설정
            int paramIndex = 1;
            pstmt.setString(paramIndex++, bookingId);

            if (userId != null && !userId.trim().isEmpty()) {
                pstmt.setString(paramIndex++, userId);
            }
            // [수정 끝]

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    reservation = mapResultSetToReservationDTO(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservation;
    }
    
    // 중복 제거를 위한 최종 쿼리 생성 메소드
    private String getFinalQuery() {
        return "SELECT "
                + "    b.booking_id, "
                + "    f.flight_id, f.departure_time, f.arrival_time, "
                + "    p.last_name, p.first_name, "
                + "    u.phone, u.email, u.user_id AS member_id, "
                + "    sc.class_name AS cabin_class, "
                + "    da.airport_id AS departure_airport_id, da.airport_name AS departure_airport_name, "
                + "    aa.airport_id AS arrival_airport_id, aa.airport_name AS arrival_airport_name, "
                + "    fs.row AS seat_row, fs.seat AS seat_number " // <-- 좌석 번호 조회를 위해 수정
                + "FROM booking b "
                + "JOIN flight f ON b.outbound_flight_id = f.flight_id " 
                + "JOIN passenger p ON b.booking_id = p.booking_id "
                + "JOIN users u ON p.user_no = u.user_no "
                + "JOIN airport da ON f.departure_airport_id = da.airport_id "
                + "JOIN airport aa ON f.arrival_airport_id = aa.airport_id "
                + "LEFT JOIN booking_seat bs ON b.booking_id = bs.booking_id " // <-- 이 부분 추가
                + "LEFT JOIN flight_seat fs ON bs.flight_seat_id = fs.seat_id "
                + "LEFT JOIN seat_class sc ON fs.class_id = sc.class_id";
    }

    // 최종 DTO 매핑 로직
    private ReservationDTO mapResultSetToReservationDTO(ResultSet rs) throws Exception {
        ReservationDTO reservation = new ReservationDTO();
        
        // 스키마에 존재하는 필드 매핑
        reservation.setBookingId(rs.getString("booking_id"));
        reservation.setFlightId(rs.getString("flight_id"));
        reservation.setFlightName(rs.getString("flight_id")); // 편명은 flight_id로 대체
        reservation.setDepartureTime(rs.getTimestamp("departure_time"));
        reservation.setArrivalTime(rs.getTimestamp("arrival_time"));
        reservation.setLastName(rs.getString("last_name"));
        reservation.setFirstName(rs.getString("first_name"));
        reservation.setPhone(rs.getString("phone"));
        reservation.setEmail(rs.getString("email"));
        reservation.setMemberId(rs.getString("member_id"));
        reservation.setCabinClass(rs.getString("cabin_class"));
        reservation.setDepartureAirportId(rs.getString("departure_airport_id"));
        reservation.setDepartureAirportName(rs.getString("departure_airport_name"));
        reservation.setArrivalAirportId(rs.getString("arrival_airport_id"));
        reservation.setArrivalAirportName(rs.getString("arrival_airport_name"));
        
        // 스키마에 존재하지 않는 필드는 null로 유지됨 (기본값)
        // reservation.setAircraftType(null);
        // reservation.setTicketNumber(null);
        // 배정된 좌석 정보 처리
        String seatRow = rs.getString("seat_row");
        String seatNumber = rs.getString("seat_number");
        if (seatRow != null && seatNumber != null && !seatRow.isEmpty() && !seatNumber.isEmpty()) {
            // row와 seat를 조합해서 좌석 번호 생성 (예: "12" + "A" = "12A")
            reservation.setAssignedSeat(seatRow + seatNumber);
        }
        
        return reservation;
    }
    
    @Override
    public void cancelReservation(String reservationId) { 
        // 실제 구현 시에는 예약 상태를 'CANCELLED'로 변경하는 UPDATE 쿼리를 실행해야 합니다.
        String sql = "UPDATE booking SET status = 'CANCELLED' WHERE booking_id = ?";
        // 여기서는 로직을 단순화하여 출력으로 대체합니다.
        System.out.println("Booking " + reservationId + " has been cancelled.");
    }

    @Override
    public void insertReservation(ReservationDTO reservation) { /* 미구현 */ }
    @Override
    public void insertPassenger(PassengerDTO passenger) { /* 미구현 */ }
    @Override
    public void updateReservation(ReservationDTO reservation) { /* 미구현 */ }

}