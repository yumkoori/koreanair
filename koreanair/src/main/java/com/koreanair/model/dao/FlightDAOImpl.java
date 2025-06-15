package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koreanair.model.dto.FareDTO;
import com.koreanair.model.dto.FlightDTO;
import com.koreanair.model.dto.SearchFlightDTO;
import com.koreanair.model.dto.SearchFlightResultDTO;
import com.koreanair.model.dto.SeatAvailabilityDTO;
import com.koreanair.model.dto.SeatMapDTO;
import com.koreanair.model.dto.User;
import com.koreanair.util.DBConnection;

public class FlightDAOImpl implements FlightDAO{

	@Override
	public void insertFlight(FlightDTO flight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFlight(FlightDTO flight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFlight(String flightId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FlightDTO getFlightById(String flightId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<SearchFlightResultDTO> getSearchFlight(SearchFlightDTO searchFlightDTO) {
        String sql = "SELECT"
        		+ "    f.flight_id,"
        		+ "    ac.airline AS airline_name,"
        		+ "    f.departure_time,"
        		+ "    f.arrival_time,"
        		+ "    TIMESTAMPDIFF(MINUTE, f.departure_time, f.arrival_time) AS duration_minutes,"
        		+ "    COUNT(fs.seat_id) AS available_seat_count"
        		+ " FROM"
        		+ "    flight f"
        		+ " JOIN aircraft ac ON f.aircraft_id = ac.aircraft_id"
        		+ " JOIN flight_seat fs ON f.flight_id = fs.flight_id"
        		+ " WHERE"
        		+ "    f.departure_airport_id = 'ICN'  "
        		+ "    AND f.arrival_airport_id = 'PUS'  "
        		+ "    AND DATE(f.departure_time) = '2025-06-10' "
        		+ "    AND fs.class_id = 'ECON' "
        		+ "    AND fs.status = 'AVAILABLE'  "
        		+ " GROUP BY"
        		+ "    f.flight_id, ac.airline, f.departure_time, f.arrival_time"
        		+ " HAVING"
        		+ "    available_seat_count >= 1 "
        		+ " ORDER BY"
        		+ "    f.departure_time ASC;"
        		+ "";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SearchFlightResultDTO> flights = new ArrayList<SearchFlightResultDTO>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                SearchFlightResultDTO sftd = SearchFlightResultDTO.builder()
                .flightId(rs.getString("flight_id"))
                .airlineName("airline_name")
                .departureTime(null)
                .arrivalTime(null)
                .durationMinutes(0)
                .availableSeatCount(rs.getInt("available_seat_count"))
                .build();
            	
               flights.add(sftd);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
            System.out.println(sql);
        }
        
        return flights;
	}

	@Override
	public SeatMapDTO getSeatMapByFlightId(String flightId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SeatAvailabilityDTO> getReservedSeats(String flightId) {
        String sql = "SELECT "
        		+ "    sc.class_id,"
        		+ "    sc.class_name,"
        		+ "    sc.detail_class_name,"
        		+ "    COUNT(fs.seat_id) AS available_seat_count"
        		+ " FROM flight_seat fs"
        		+ " JOIN seat_class sc ON fs.class_id = sc.class_id"
        		+ " WHERE fs.flight_id = 'FL001'"
        		+ "  AND fs.status = 'AVAILABLE'"
        		+ " GROUP BY sc.class_id, sc.class_name, sc.detail_class_name"
        		+ " ORDER BY sc.class_id;";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        List<SeatAvailabilityDTO>  availabilitySeats = new ArrayList<SeatAvailabilityDTO>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                SeatAvailabilityDTO dto = SeatAvailabilityDTO.builder()
                		.classId(rs.getString("class_id"))
                		.className(rs.getString("class_name"))
                		.detailClassName(rs.getString("detail_class_name"))
                		.availableSeatCount(rs.getInt("available_seat_count"))
                		.build();
            	
                availabilitySeats.add(dto);
            }
       
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return availabilitySeats;
		
	}

	@Override
	public void updateFare(String flightId, FareDTO fare) {
		// TODO Auto-generated method stub
		
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
