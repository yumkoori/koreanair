package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.koreanair.model.dto.FareDTO;
import com.koreanair.model.dto.FlightDTO;
import com.koreanair.model.dto.SearchFlightDTO;
import com.koreanair.model.dto.SearchFlightResultDTO;
import com.koreanair.model.dto.SeatAvailabilityDTO;
import com.koreanair.model.dto.SeatMapDTO;
import com.koreanair.model.dto.SeatPriceDTO;
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
		        + " LEFT JOIN flight_seat fs ON f.flight_id = fs.flight_id AND fs.status = 'AVAILABLE'"
		        + " LEFT JOIN seat_class sc ON sc.class_id = fs.class_id"
		        + " WHERE"
		        + "    f.departure_airport_id = ? "
		        + "    AND f.arrival_airport_id = ? "
		        + "    AND DATE(f.departure_time) = ? "
		        + " GROUP BY"
		        + "    f.flight_id, ac.airline, f.departure_time, f.arrival_time"
		        + " ORDER BY"
		        + "    f.departure_time ASC";


        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SearchFlightResultDTO> flights = new ArrayList<SearchFlightResultDTO>();
        
        
        System.out.println("실행되는 SQL:");
        System.out.println("Departure: " + searchFlightDTO.getDeparture());
        System.out.println("Arrival: " + searchFlightDTO.getArrival());
        System.out.println("Date: " + searchFlightDTO.getDepartureDate());
        System.out.print(sql);
       
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchFlightDTO.getDeparture());
            pstmt.setString(2, searchFlightDTO.getArrival());
            pstmt.setString(3, searchFlightDTO.getDepartureDate());


            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SearchFlightResultDTO sftd = SearchFlightResultDTO.builder()
                .flightId(rs.getString("flight_id"))
                .airlineName(rs.getString("airline_name"))
                .departureTime(rs.getTimestamp("departure_time").toLocalDateTime())
                .arrivalTime(rs.getTimestamp("arrival_time").toLocalDateTime())
                .durationMinutes(rs.getInt("duration_minutes"))
                .availableSeatCount(rs.getInt("available_seat_count"))
                .build();
            	
               flights.add(sftd);
               
               System.out.println(flights);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
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
        String sql = "SELECT"
        		+ "    sc.class_id,"
        		+ "    sc.class_name,"
        		+ "    sc.detail_class_name,"
        		+ "    IFNULL(COUNT(fs.seat_id), 0) AS available_seat_count,"
        		+ "    sp.price"
        		+ " FROM seat_price sp"
        		+ " JOIN seat_class sc ON sp.class_id = sc.class_id"
        		+ " LEFT JOIN flight_seat fs "
        		+ "    ON fs.flight_id = sp.flight_id "
        		+ "    AND fs.class_id = sp.class_id "
        		+ "    AND fs.status = 'AVAILABLE'"
        		+ " WHERE sp.flight_id = ?"
        		+ " GROUP BY sc.class_id, sc.class_name, sc.detail_class_name, sp.price"
        		+ " ORDER BY sc.class_id;";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        List<SeatAvailabilityDTO>  availabilitySeats = new ArrayList<SeatAvailabilityDTO>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, flightId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SeatAvailabilityDTO dto = SeatAvailabilityDTO.builder()
                	.classId(rs.getString("class_id"))
                	.className(rs.getString("class_name"))
                	.detailClassName(rs.getString("detail_class_name"))
                	.availableSeatCount(rs.getInt("available_seat_count"))
                	.price(rs.getInt("price"))
                	.build();
            	
                availabilitySeats.add(dto);
            }
            System.out.println(availabilitySeats); // 루프 밖으로 옮기세요

       
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	System.out.println(sql);
            closeResources(conn, pstmt, rs);
        }
        
        return availabilitySeats;
		
	}

	@Override
	public void updateFare(String flightId, FareDTO fare) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Map<String, Integer> getSeatsPrice(String flightId) {
		String sql = "select * from seat_price where flight_id = ?;";
		
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
		
	    Map<String, Integer> map = new HashMap<String, Integer>();
	    
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, flightId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
            	map.put(rs.getString("class_id"), rs.getInt("price"));
            }
       
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	System.out.println(sql);
            closeResources(conn, pstmt, rs);
        }
		
		return map;
	}
	
	@Override
	public Map<String, Integer> getWeekLowPrices(SearchFlightDTO searchdto) {

		String sql = "WITH RECURSIVE date_range AS ("
				+ "    SELECT DATE(?) - INTERVAL 3 DAY AS target_date"
				+ "    UNION ALL"
				+ "    SELECT target_date + INTERVAL 1 DAY"
				+ "    FROM date_range"
				+ "    WHERE target_date + INTERVAL 1 DAY <= DATE(?) + INTERVAL 3 DAY"
				+ "),"
				+ " ranked_prices AS ("
				+ "    SELECT "
				+ "        DATE(f.departure_time) AS flight_date,"
				+ "        sp.price,"
				+ "        ROW_NUMBER() OVER (PARTITION BY DATE(f.departure_time) ORDER BY sp.price ASC) AS rn"
				+ "    FROM flight f"
				+ "    JOIN flight_seat fs ON f.flight_id = fs.flight_id"
				+ "    JOIN seat_price sp ON fs.flight_id = sp.flight_id AND fs.class_id = sp.class_id"
				+ "    WHERE "
				+ "        f.departure_airport_id = ?"
				+ "        AND f.arrival_airport_id = ?"
				+ "        AND fs.status = 'AVAILABLE'"
				+ " )"
				+ " SELECT "
				+ "    d.target_date,"
				+ "    rp.price"
				+ " FROM date_range d"
				+ " LEFT JOIN ranked_prices rp"
				+ "    ON d.target_date = rp.flight_date AND rp.rn = 1"
				+ " ORDER BY d.target_date;"
				+ "";
		
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
		
	    Map<String, Integer> weekMap = new LinkedHashMap<String, Integer>();
	    
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
 
            pstmt.setDate(1, java.sql.Date.valueOf(searchdto.getDepartureDate()));
            pstmt.setDate(2, java.sql.Date.valueOf(searchdto.getDepartureDate()));

            pstmt.setString(3, searchdto.getDeparture());
            pstmt.setString(4, searchdto.getArrival());


            rs = pstmt.executeQuery();
            
            while (rs.next()) {
            	weekMap.put(rs.getString("target_date"), rs.getInt("price"));
            }
       
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	System.out.println(sql);
            closeResources(conn, pstmt, rs);
        }
		
        
        System.out.println("최저가 호출됨");
		return weekMap;
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
