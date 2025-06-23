package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.koreanair.model.dto.FareDTO;
import com.koreanair.model.dto.FlightDTO;
import com.koreanair.model.dto.FlightSeatDTO;
import com.koreanair.model.dto.SeatClassDTO;
import com.koreanair.util.DBConnection;

public class FlightDAOImpl implements FlightDAO {

    /**
     * 특정 항공편의 모든 좌석 정보를 좌석 등급 정보와 함께 조회합니다. (수정하지 않음)
     */
    @Override
    public List<FlightSeatDTO> getSeatMapByFlightId(String flightId) {
        List<FlightSeatDTO> seatMap = new ArrayList<>();
        String sql = "SELECT fs.seat_id, fs.flight_id, fs.class_id, fs.status, sc.class_name, sc.base_price " +
                     "FROM flight_seat fs " +
                     "JOIN seat_class sc ON fs.class_id = sc.class_id " +
                     "WHERE fs.flight_id = ? ORDER BY fs.seat_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, flightId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                FlightSeatDTO flightSeat = new FlightSeatDTO();
                flightSeat.setSeatId(rs.getString("seat_id"));
                flightSeat.setFlightId(rs.getString("flight_id"));
                flightSeat.setClassId(rs.getString("class_id"));
                flightSeat.setStatus(rs.getString("status"));

                SeatClassDTO seatClass = new SeatClassDTO();
                seatClass.setClassId(rs.getString("class_id"));
                seatClass.setClassName(rs.getString("class_name"));
                seatClass.setBasePrice(rs.getBigDecimal("base_price"));

                flightSeat.setSeatClass(seatClass);
                seatMap.add(flightSeat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatMap;
    }
    
    /**
     * 특정 항공편에서 이미 예약된 좌석들의 ID 목록을 조회합니다. (수정하지 않음)
     */
    @Override
    public List<String> getReservedSeats(String flightId) {
        List<String> reservedSeats = new ArrayList<>();
        String sql = "SELECT bs.flight_seat_id FROM booking_seat bs " +
                     "JOIN booking b ON bs.booking_id = b.booking_id " +
                     "WHERE b.flight_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, flightId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reservedSeats.add(rs.getString("flight_seat_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservedSeats;
    }

    // ================================================================
    // ▼▼▼▼▼▼▼▼▼▼▼▼ 체크인/좌석 변경을 위한 신규 메소드들 ▼▼▼▼▼▼▼▼▼▼▼▼
    // ================================================================
    
    /**
     * [신규] 예약 ID로 이미 배정된 좌석이 있는지 확인합니다.
     */
    @Override
    public boolean isSeatAssigned(String bookingId) {
        String sql = "SELECT COUNT(*) FROM booking_seat WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * [신규] 기존에 배정된 좌석을 새로운 좌석으로 변경합니다. (UPDATE)
     */
    @Override
    public boolean updateSeatForBooking(String bookingId, String newFlightSeatId, String newBookingSeatId) {
        String sql = "UPDATE booking_seat SET flight_seat_id = ?, booking_seat_id = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newFlightSeatId);
            pstmt.setString(2, newBookingSeatId);
            pstmt.setString(3, bookingId);
            
            System.out.println("[DEBUG - FlightDAOImpl] updateSeatForBooking 실행");
            System.out.println("  > SQL: " + sql);
            System.out.println("  > 파라미터 1 (newFlightSeatId): " + newFlightSeatId);
            System.out.println("  > 파라미터 2 (newBookingSeatId): " + newBookingSeatId);
            System.out.println("  > 파라미터 3 (bookingId): " + bookingId);
            
            int result = pstmt.executeUpdate();
            System.out.println("[DEBUG - FlightDAOImpl] UPDATE 결과 (0보다 크면 성공): " + result);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * [신규] 최초로 좌석을 배정합니다. (INSERT)
     */
    @Override
    public boolean insertSeatForBooking(String bookingId, String flightSeatId, String bookingSeatId) {
        String sql = "INSERT INTO booking_seat (booking_seat_id, booking_id, flight_seat_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            System.out.println("[DEBUG - FlightDAOImpl] insertSeatForBooking 실행");
            System.out.println("  > SQL: " + sql);
            System.out.println("  > 파라미터 1 (bookingSeatId): " + bookingSeatId);
            System.out.println("  > 파라미터 2 (bookingId): " + bookingId);
            System.out.println("  > 파라미터 3 (flightSeatId): " + flightSeatId);

            pstmt.setString(1, bookingSeatId);
            pstmt.setString(2, bookingId);
            pstmt.setString(3, flightSeatId);

            int result = pstmt.executeUpdate();
            System.out.println("[DEBUG - FlightDAOImpl] INSERT 결과 (0보다 크면 성공): " + result);
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
	public boolean deleteSeatAssignment(String bookingId) {
		String sql = "DELETE FROM booking_seat WHERE booking_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, bookingId);

			System.out.println("[DEBUG - FlightDAOImpl] deleteSeatAssignment 실행");
			System.out.println("  > SQL: " + sql);
			System.out.println("  > 파라미터 1 (bookingId): " + bookingId);

			int result = pstmt.executeUpdate();
			// 1개 행이 삭제되거나, 혹은 이전에 삭제되어 0개 행이 영향을 받아도 성공으로 처리
			return result >= 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

    // 미구현 메소드 (수정하지 않음)
    @Override public void insertFlight(FlightDTO flight) {}
    @Override public void updateFlight(FlightDTO flight) {}
    @Override public void deleteFlight(String flightId) {}
    @Override public FlightDTO getFlightById(String flightId) { return null; }
    @Override public void updateFare(String flightId, FareDTO fare) {}
}