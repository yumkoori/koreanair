package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.koreanair.model.dto.PassengerDTO;
import com.koreanair.util.DBConnection;

public class PassengerDAO {
	public void savePassenger(PassengerDTO dto) {
		String sql = "INSERT INTO passenger ("
				+ "  passenger_id,"
				+ "  user_no,"
				+ "  booking_id,"
				+ "  last_name,"
				+ "  first_name,"
				+ "  birth_date,"
				+ "  gender,"
				+ "  passenger_type"
				+ ") VALUES ("
				+ "  ?,         "
				+ "  ?,           "
				+ "  ?, "
				+ "  ?,          "
				+ "  ?,         "
				+ "  ?,   "
				+ "  ?,            "
				+ "  ?        "
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
            
            Integer userNo = dto.getUserNo();
            if (userNo != null) {
                pstmt.setInt(2, userNo);
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(3, dto.getBookingId());
            pstmt.setString(4, dto.getLastName());
            pstmt.setString(5, dto.getFirstName());
            pstmt.setString(6, dto.getBirthDate());
            pstmt.setString(7, dto.getGender());
            pstmt.setString(8, dto.getType());

            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.print("탑승객 정보 저장 완");
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
