package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.koreanair.model.dto.AirportDTO;
import com.koreanair.util.DBConnection;

public class ApiDAO {

    public void saveAirport(List<AirportDTO> list) {
//    	String sql = "INSERT INTO airport (airport_id, airport_name, airport_eng_name, city_kor, city_eng) VALUES (?, ?, ?, ?, ?) "
//    	           + "ON DUPLICATE KEY UPDATE airport_name = VALUES(airport_name)";

    	
    	String sql = "INSERT INTO airport (airport_id, airport_name, airport_eng_name, city_kor, city_eng)"
    			+ "VALUES (?, ?, ?, ?, ?)"
    			+ "ON DUPLICATE KEY UPDATE"
    			+ "    airport_name = VALUES(airport_name),"
    			+ "    airport_eng_name = VALUES(airport_eng_name),"
    			+ "    city_kor = VALUES(city_kor),"
    			+ "    city_eng = VALUES(city_eng);"
    			+ "";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            for (AirportDTO airport : list) {
                if (airport.getAirportId() == null || airport.getAirportId().isEmpty()) continue; // 유효성 검사

                pstmt.setString(1, airport.getAirportId());
                pstmt.setString(2, airport.getAirportName());
                pstmt.setString(3, airport.getAirportEngName());
                pstmt.setString(4, airport.getCityKor());
                pstmt.setString(5, airport.getCityEng());

                pstmt.addBatch(); // 일괄처리
            }

            pstmt.executeBatch(); // 배치 실행
            System.out.println("공항 데이터 삽입 완료");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn);
        }
    }
    
    
}
