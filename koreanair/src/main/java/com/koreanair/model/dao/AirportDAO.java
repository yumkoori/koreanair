package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koreanair.model.dto.AirportDTO;
import com.koreanair.util.DBconn;

public class AirportDAO {

	
	   public List<AirportDTO> findAirportsByKeyword(String keyword) {
	        List<AirportDTO> list = new ArrayList<>();
	        String sql = "SELECT * FROM airport WHERE airport_name LIKE ? OR airport_id LIKE ?";

	        try (Connection conn = DBconn.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            String pattern = "%" + keyword + "%";
	            pstmt.setString(1, pattern);
	            pstmt.setString(2, pattern);

	            try (ResultSet rs = pstmt.executeQuery()) {
	                while (rs.next()) {
	                    AirportDTO airport = AirportDTO.builder()
	                    		.airportId(rs.getString("airport_id"))
	                    		.airportName(rs.getString("airport_name"))
	                    		.airportEngName(rs.getString("airport_eng_name"))
	                    		.cityKor(rs.getString("city_kor"))
	                    		.cityEng(rs.getString("city_eng"))
	                    		.build();
	                   
	                    list.add(airport);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return list;
	    }

}
