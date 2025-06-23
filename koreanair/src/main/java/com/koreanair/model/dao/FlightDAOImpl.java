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
    public List<FlightSeatDTO> getSeatMapByFlightIds(String flightId) {
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
	public List<SeatAvailabilityDTO> getReservedSeats(String flightId, int passengers) {

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
		        + " HAVING available_seat_count >= ?"
		        + " ORDER BY sc.class_id;";

        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        List<SeatAvailabilityDTO>  availabilitySeats = new ArrayList<SeatAvailabilityDTO>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, flightId);
            pstmt.setInt(2, passengers);

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
	
	
	public void releaseExpiredPendingSeats() {
	    String sql = "UPDATE flight_seat " +
	                 "SET status = 'AVAILABLE', pending_at = NULL " +
	                 "WHERE status = 'PENDING' AND TIMESTAMPDIFF(MINUTE, pending_at, NOW()) >= 5";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        int updatedRows = pstmt.executeUpdate();
	        System.out.println("Expired pending seats released: " + updatedRows);
	    } catch (SQLException e) {
	        e.printStackTrace();
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

