package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import com.koreanair.model.dto.BookingDTO;
import com.koreanair.model.dto.PassengerDTO;
import com.koreanair.model.dto.SearchFlightResultDTO;
import com.koreanair.util.DBConnection;

public class BookingDAO {
	
	public String saveBooking(BookingDTO dto) {
		String sql = "INSERT INTO booking ("
				+ "    booking_id, "
				+ "    outbound_flight_id, "
				+ "    return_flight_id,   "
				+ "    user_no, "
				+ "    promotion_id, "
				+ "    booking_pw,"
				+ "	   expire_time"
				+ " ) VALUES ("
				+ "    ?,       "
				+ "    ?,              "
				+ "    ?,                  "
				+ "    ?,           "
				+ "    ?,           "
				+ "    ?,            "
				+ "    ?     "
				+ ");"
				+ "";
		
        UUID uuid = UUID.randomUUID();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, dto.getOutboundFlightId());
            pstmt.setString(3, dto.getReturnFlightId());
            pstmt.setString(4, dto.getUserNo());
            pstmt.setString(5, dto.getPromotionId());
            pstmt.setString(6, dto.getBookingPw());
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now().plusMinutes(15)));

            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.print("예약중 입니다");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
		
		return uuid.toString();
	}
	
	
	public void updateSeatToPending(String flightId, String seatClass, int totalPassengers) throws SQLException {
	    String sql = "UPDATE flight_seat"
	    		+ " SET status = 'PENDING', pending_at = NOW()"
	    		+ " WHERE flight_id = ?"
	    		+ " AND class_id = ?"
	    		+ " AND status = 'AVAILABLE'"
	    		+ " LIMIT ?;";
	    		
	    		
	            Connection conn = null;
	            PreparedStatement pstmt = null;
	            ResultSet rs = null;
	            
	            
	            System.out.println("UPDATE 시작!!!!!!");
	            System.out.println(flightId);
	            System.out.println(seatClass);
	            System.out.println(totalPassengers);

	            
	            
	    try {
	    	
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
	        pstmt.setString(1, flightId);
	        pstmt.setString(2, seatClass);
	        pstmt.setInt(3, totalPassengers);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
	}

	
	
	
    // 리소스 정리
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
