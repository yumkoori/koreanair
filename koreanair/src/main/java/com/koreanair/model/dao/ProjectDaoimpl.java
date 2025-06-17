package com.koreanair.model.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.koreanair.model.dto.AirCraftId;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.dto.SaveSchedulesDBDTO;
import com.koreanair.util.DBConn;

public class ProjectDaoimpl implements ProjectDao{
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;



	@Override
	public String fetchFlightData(String apiUrl) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader br = null;

		try {
			URL url = new URL(apiUrl);
			conn =(HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
			conn.setUseCaches(false);

			int responseCode = conn.getResponseCode();

			if( responseCode == 200 ) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
				StringBuilder responseDataXml = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					responseDataXml.append(line);
				}
				// 성공 시, 가공하지 않은 원본 XML 문자열 반환
				
				
				
				return responseDataXml.toString();
			}else {
				throw new Exception("API call failed with HTTP code: " + responseCode);
			}
		} finally {
			// 자원 해제
			if (br != null) 
				try { 
					br.close(); } 
			catch (Exception e) {
				e.printStackTrace();
			}
			if (conn != null) conn.disconnect();
			
			
		}
	}


	@Override
	public int seatsave(List<FlightSeatSaveDTO> seatList , String id) throws Exception {
		// 1. Connection과 PreparedStatement는 메서드 내의 지역 변수로 선언하는 것이 좋습니다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		int totalSavedCount = 0;

		String sql = "INSERT INTO flight_seat (seat_id, flight_id, class_id, status, price , row , seat) VALUES (?, ?, ?, ?, ? , ? , ?)";

		try {
			// 2. DBConn 유틸리티를 사용해 커넥션을 얻어옵니다.
			conn = DBConn.getConnection(); 

			// conn.setAutoCommit(false); // 트랜잭션 시작
			pstmt = conn.prepareStatement(sql);


			// 3. 파라미터로 받은 'seatList'를 반복문으로 처리합니다.
			//    'dto' 변수는 여기서 각 좌석 정보를 받아와 사용합니다.
			for (FlightSeatSaveDTO dto : seatList) { 
				// 4. 'dto' 객체의 값으로 PreparedStatement를 설정합니다.
				UUID uuid = UUID.randomUUID();
				pstmt.setString(1, uuid.toString());
				pstmt.setString(2, id);
				pstmt.setString(3, dto.getClassseat());
				pstmt.setString(4, "AVAILABLE");
				pstmt.setInt(5, dto.getPrice()); 
				pstmt.setInt(6, dto.getRow());
				pstmt.setString(7, dto.getSeat());

				pstmt.addBatch(); // 실행할 쿼리에 추가
			}

			int[] resultCounts = pstmt.executeBatch(); // 배치 쿼리 실행
			// conn.commit(); // 트랜잭션 성공 (커밋)

			totalSavedCount = resultCounts.length; // 성공한 개수

		} catch (Exception e) {
			if (conn != null) conn.rollback(); // 오류 시 롤백
			System.out.println("DAO seatsave 오류");
			e.printStackTrace();
			throw e; // 오류를 상위로 전달
		} finally {
			// 5. 자원 해제
			DBConn.close(conn, pstmt);  // DBConn 유틸리티에 close 메서드가 있다면 사용
		}

		return totalSavedCount;
	}


	@Override
	public List<FlightSeatSaveDTO> flightSeatload(String flight_id) throws Exception {
		System.out.println(flight_id + " 들고무사히 도착했습니다!!!");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM flight_seat WHERE flight_id = ? AND `row` BETWEEN 7 AND 52";
		List<FlightSeatSaveDTO> list = new ArrayList();

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, flight_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				FlightSeatSaveDTO dto = new FlightSeatSaveDTO();
				dto.setSeat(rs.getString("seat"));
				dto.setPrice(rs.getInt("price"));
				dto.setRow(rs.getInt("row"));
				list.add(dto);
			}

		} catch (Exception e) {
			if (conn != null) conn.rollback();
			System.out.println("DAO seatsave 오류");
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			DBConn.close(conn, pstmt);
		}

		return list;
	}


	@Override
	public int searchcarftid(String flight_id) throws Exception {
		// 1. Connection과 PreparedStatement는 메서드 내의 지역 변수로 선언하는 것이 좋습니다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		int checkid = 0;

		String sql = "SELECT * "
				+ " FROM flight "
				+ " WHERE flight_id = ? ";

		try {
			// 2. DBConn 유틸리티를 사용해 커넥션을 얻어옵니다.
			conn = DBConn.getConnection(); 

			// conn.setAutoCommit(false); // 트랜잭션 시작
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, flight_id);

			rs = pstmt.executeQuery();
			// conn.commit(); // 트랜잭션 성공 (커밋)

			if (rs.next()) {
				// 조회된 행이 있다면, checkid를 1로 변경
				checkid = 1;
			}

		} catch (Exception e) {
			if (conn != null) conn.rollback(); // 오류 시 롤백
			System.out.println("DAO seatsave 오류");
			e.printStackTrace();
			throw e; // 오류를 상위로 전달
		} finally {
			// 5. 자원 해제
			DBConn.close(conn, pstmt);  // DBConn 유틸리티에 close 메서드가 있다면 사용
		}
		return checkid;
	}


	@Override
	public boolean checkDuplicateSeat(List<FlightSeatSaveDTO> seatList) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(*) FROM flight_seat WHERE flight_id = ? AND row = ? AND seat = ?";

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);

			for (FlightSeatSaveDTO seat : seatList) {
				pstmt.setString(1, seat.getFlight_id());   
				pstmt.setInt(2, seat.getRow());            
				pstmt.setString(3, seat.getSeat());        

				rs = pstmt.executeQuery();

				if (rs.next()) {
					int count = rs.getInt(1);
					if (count > 0) {
						// 중복이 발견되면 true 반환 (중복 있음)
						System.out.println("중복 좌석 발견: Flight_id=" + seat.getFlight_id() + 
								", Row=" + seat.getRow() + ", Seat=" + seat.getSeat());
						return true;
					}
				}

				// ResultSet을 닫고 다음 쿼리를 위해 준비
				if (rs != null) {
					rs.close();
					rs = null;
				}
			}

		} catch (Exception e) {
			System.out.println("DAO checkDuplicateSeat 오류");
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
			if (conn != null) try { conn.close(); } catch (Exception e) {}
		}

		return false; // 중복 없음
	}


	@Override
	public int saveSchdulesDB(List<SaveSchedulesDBDTO> scheduleList) {
		// 1. Connection과 PreparedStatement는 메서드 내의 지역 변수로 선언하는 것이 좋습니다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		int totalSavedCount = 0;

		String sql = "INSERT INTO flight ( flight_id ,aircraft_id, departure_airport_id , arrival_airport_id, arrival_time, departure_time, terminal, status, flifhtNo) "
				+    " VALUES (?, ?, ?, ?, ? , ? , ? , ? , ?)";

		try {
			// 2. DBConn 유틸리티를 사용해 커넥션을 얻어옵니다.
			conn = DBConn.getConnection(); 

			// conn.setAutoCommit(false); // 트랜잭션 시작
			pstmt = conn.prepareStatement(sql);


			// 3. 파라미터로 받은 'seatList'를 반복문으로 처리합니다.
			//    'dto' 변수는 여기서 각 좌석 정보를 받아와 사용합니다.
			for (SaveSchedulesDBDTO dto : scheduleList) {
				String id = dto.getId(); // 예: "2025-06-16-all-FLZE821A_0"
				String dateStr = id.split("-all-")[0]; // "2025-06-16"
				LocalDate date = LocalDate.parse(dateStr); // LocalDate 객체로 변환
				String flid = id.split("-all-")[1];
				UUID uuid = UUID.randomUUID();
				pstmt.setString(1, uuid.toString());
				pstmt.setString(2, "B789");
				pstmt.setString(3, dto.getOrigin());
				pstmt.setString(4, dto.getDestination());

				// 5. departureTime 처리
				if ("N/A".equalsIgnoreCase(dto.getDepartureTime())) {
					pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
				} else {
					String departureDateTimeStr = date + " " + dto.getDepartureTime() + ":00"; // "2025-06-16 00:17:00"
					pstmt.setTimestamp(5, Timestamp.valueOf(departureDateTimeStr));
				}

				// 6. arrivalTime 처리
				if ("N/A".equalsIgnoreCase(dto.getArrivalTime())) {
					pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
				} else {
					String arrivalDateTimeStr = date + " " + dto.getArrivalTime() + ":00";
					pstmt.setTimestamp(6, Timestamp.valueOf(arrivalDateTimeStr));
				}

				pstmt.setString(7, "T1");
				pstmt.setString(8, dto.getStatus());
				pstmt.setString(9, dto.getFlightNo());

				pstmt.addBatch();
			}

			int[] resultCounts = pstmt.executeBatch(); // 배치 쿼리 실행
			// conn.commit(); // 트랜잭션 성공 (커밋)

			totalSavedCount = resultCounts.length; // 성공한 개수

		} catch (Exception e) {
			// if (conn != null) conn.rollback(); // 오류 시 롤백
			System.out.println("DAO seatsave 오류");
			e.printStackTrace();
			// throw e; // 오류를 상위로 전달
		} finally {
			// 5. 자원 해제
			DBConn.close(conn, pstmt);  // DBConn 유틸리티에 close 메서드가 있다면 사용
		}

		return totalSavedCount;
	}


	@Override
	public boolean refreshCheck(List<SaveSchedulesDBDTO> refresList) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(*) FROM flight WHERE flight_id = ? ";

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);

			for (SaveSchedulesDBDTO refresh : refresList) {
				String id = refresh.getId(); // 예: "2025-06-16-all-FLZE821A_0"
				String dateStr = id.split("-all-")[0]; // "2025-06-16"
				String flid = id.split("-all-")[1];
				pstmt.setString(1, flid);          
				rs = pstmt.executeQuery();

				if (rs.next()) {
					int count = rs.getInt(1);
					if (count > 0) {
						// 중복이 발견되면 true 반환 (중복 있음)
						System.out.println("중복 좌석 발견: Flight_id=" + flid);
						return true;
					}
				}

				// ResultSet을 닫고 다음 쿼리를 위해 준비
				if (rs != null) {
					rs.close();
					rs = null;
				}
			}

		} catch (Exception e) {
			System.out.println("DAO checkDuplicateSeat 오류");
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) {}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
			if (conn != null) try { conn.close(); } catch (Exception e) {}
		}

		return false; // 중복 없음
	}


	@Override
	public List<SaveSchedulesDBDTO> refreshSchdules(List<SaveSchedulesDBDTO> refresList) throws Exception {
	    List<SaveSchedulesDBDTO> insertedList = new ArrayList<>();
	    
	    Connection conn = null;
	    PreparedStatement pstmtCheck = null;
	    PreparedStatement pstmtInsert = null;
	    ResultSet rs = null;

	    String sqlCheck = "SELECT COUNT(*) FROM flight WHERE departure_airport_id = ? AND arrival_airport_id =? AND arrival_time = ? AND departure_time = ?";
	    String sqlInsert = "INSERT INTO flight (flight_id, aircraft_id, departure_airport_id, arrival_airport_id, arrival_time, departure_time, terminal, status, flifhtNo) "
	                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    try {
	        conn = DBConn.getConnection();
	        pstmtCheck = conn.prepareStatement(sqlCheck);
	        pstmtInsert = conn.prepareStatement(sqlInsert);
	        for (SaveSchedulesDBDTO refresh : refresList) {
	            // null 체크
	            if (refresh == null || refresh.getId() == null) {
	                System.out.println("refresh 객체 또는 ID가 null입니다. 건너뜁니다.");
	                continue;
	            }
	            
	            String id = refresh.getId();
	            if (!id.contains("-all-")) {
	                System.out.println("ID 형식이 올바르지 않습니다: " + id);
	                continue;
	            }
	            
	            String dateStr = id.split("-all-")[0];
	            String flid = id.split("-all-")[1];
	            LocalDate date = LocalDate.parse(dateStr); // LocalDate 객체로 변환
	            
	            // 시간 null 체크 및 기본값 설정
	            String departureTime = refresh.getDepartureTime();
	            String arrivalTime = refresh.getArrivalTime();
	            
	            if (departureTime == null || "N/A".equalsIgnoreCase(departureTime)) {
	                departureTime = "00:00";
	            }
	            if (arrivalTime == null || "N/A".equalsIgnoreCase(arrivalTime)) {
	                arrivalTime = "00:00";
	            }
	            
				String departureDateTimeStr = date + " " + departureTime + ":00"; // "2025-06-16 00:17:00"
				String arrivalDateTimeStr = date + " " + arrivalTime + ":00";
				
	            // 존재 여부 체크 - null 체크 추가
	            String origin = refresh.getOrigin() != null ? refresh.getOrigin() : "UNKNOWN";
	            String destination = refresh.getDestination() != null ? refresh.getDestination() : "UNKNOWN";
	            
	            pstmtCheck.setString(1, origin);
	            pstmtCheck.setString(2, destination);
	            pstmtCheck.setTimestamp(3, Timestamp.valueOf(arrivalDateTimeStr));    // arrival_time이 3번째
	            pstmtCheck.setTimestamp(4, Timestamp.valueOf(departureDateTimeStr));  // departure_time이 4번째
	            rs = pstmtCheck.executeQuery();

	            int count = 0;
	            if (rs.next()) {
	                count = rs.getInt(1);
	            }
	            rs.close();
	            rs = null;

	            // 없으면 insert
	            if (count == 0) {	               	                
	                UUID uuid = UUID.randomUUID();
	                
	                // pstmtInsert 사용 (pstmt가 아닌)
					pstmtInsert.setString(1, uuid.toString());
					pstmtInsert.setString(2, "B789");
					pstmtInsert.setString(3, origin);      // 이미 null 체크된 값 사용
					pstmtInsert.setString(4, destination); // 이미 null 체크된 값 사용

	                // departureTime
	                if ("N/A".equalsIgnoreCase(refresh.getDepartureTime())) {
	                    pstmtInsert.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
	                } else {
	                    pstmtInsert.setTimestamp(5, Timestamp.valueOf(departureDateTimeStr));
	                }

	                // arrivalTime
	                if ("N/A".equalsIgnoreCase(refresh.getArrivalTime())) {
	                    pstmtInsert.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
	                } else {
	                    pstmtInsert.setTimestamp(6, Timestamp.valueOf(arrivalDateTimeStr));
	                }

	                pstmtInsert.setString(7, "T160");
	                pstmtInsert.setString(8, refresh.getStatus() != null ? refresh.getStatus() : "UNKNOWN");
	                pstmtInsert.setString(9, refresh.getFlightNo() != null ? refresh.getFlightNo() : "UNKNOWN");

	                int inserted = pstmtInsert.executeUpdate();

	                if (inserted > 0) {
	                    insertedList.add(refresh);  // 삽입 성공 시 리스트에 추가
	                }
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("DAO checkDuplicateSeat 오류");
	        e.printStackTrace();
	    } finally {
	        if (rs != null) try { rs.close(); } catch (Exception e) {}
	        if (pstmtCheck != null) try { pstmtCheck.close(); } catch (Exception e) {}
	        if (pstmtInsert != null) try { pstmtInsert.close(); } catch (Exception e) {}
	        if (conn != null) try { conn.close(); } catch (Exception e) {}
	    }
	    return insertedList;  // 추가된 항목들 반환
	}



}
