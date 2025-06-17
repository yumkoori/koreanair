package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.koreanair.model.dto.BookingDTO;
import com.koreanair.model.dto.SearchFlightResultDTO;
import com.koreanair.util.DBConnection;

public class BookingDAO {
	
	public void saveBooking(BookingDTO dto) {
		String sql = "INSERT INTO booking ("
				+ "    booking_id, "
				+ "    flight_id, "
				+ "    user_no, "
				+ "    promotion_id, "
				+ "    booking_pw"
				+ " ) VALUES ("
				+ "    ?,       "
				+ "    ?,              "
				+ "    ?,                  "
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
            pstmt.setString(2, dto.getFlightId());
            pstmt.setString(3, dto.getUserNo());
            pstmt.setString(4, dto.getPromotionId());
            pstmt.setString(5, dto.getBookingPw());

            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.print("예약중 입니다");
            }
            
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
