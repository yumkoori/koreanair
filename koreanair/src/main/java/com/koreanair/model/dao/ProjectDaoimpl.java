package com.koreanair.model.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.koreanair.model.dto.AdminReservationDTO;
import com.koreanair.model.dto.AirCraftId;
import com.koreanair.model.dto.ClassPriceSaveDTO;
import com.koreanair.model.dto.DashBoardStatsDTO;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.dto.MonthReservationDTO;
import com.koreanair.model.dto.PopularDTO;
import com.koreanair.model.dto.SaveSchedulesDBDTO;
import com.koreanair.model.dto.SearchUserDTO;
import com.koreanair.model.dto.SeatRevenueDTO;
import com.koreanair.util.DBConn;

public class ProjectDaoimpl implements ProjectDao {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	@Override
	public String fetchFlightData(String apiUrl) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader br = null;

		try {
			URL url = new URL(apiUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
			conn.setUseCaches(false);

			int responseCode = conn.getResponseCode();

			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
				StringBuilder responseDataXml = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					responseDataXml.append(line);
				}
				return responseDataXml.toString();
			} else {
				throw new Exception("API call failed with HTTP code: " + responseCode);
			}
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	@Override
	public int seatsave(List<FlightSeatSaveDTO> seatList, String id) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int totalSavedCount = 0;

		String sql = "INSERT INTO flight_seat (seat_id, flight_id, class_id, status, price, row, seat) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);

			for (FlightSeatSaveDTO dto : seatList) {
				UUID uuid = UUID.randomUUID();
				pstmt.setString(1, uuid.toString());
				pstmt.setString(2, id);
				pstmt.setString(3, dto.getClassseat());
				pstmt.setString(4, "AVAILABLE");
				pstmt.setInt(5, dto.getPrice());
				pstmt.setInt(6, dto.getRow());
				pstmt.setString(7, dto.getSeat());
				pstmt.addBatch();
			}

			int[] resultCounts = pstmt.executeBatch();
			totalSavedCount = resultCounts.length;

		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			System.out.println("DAO seatsave 오류");
			e.printStackTrace();
			throw e;
		} finally {
			DBConn.close(conn, pstmt);
		}

		return totalSavedCount;
	}

	@Override
	public List<FlightSeatSaveDTO> flightSeatload(String flight_id) throws Exception {
		System.out.println(flight_id + " 들고무사히 도착했습니다!!!");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM flight_seat WHERE flight_id = ? AND `row` BETWEEN 7 AND 57";
		List<FlightSeatSaveDTO> list = new ArrayList<>();

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
			if (conn != null) {
				conn.rollback();
			}
			System.out.println("DAO seatsave 오류");
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			DBConn.close(conn, pstmt);
		}

		return list;
	}

	@Override
	public int searchcarftid(String flight_id) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int checkid = 0;

		String sql = "SELECT * FROM flight WHERE flight_id = ?";

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, flight_id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				checkid = 1;
			}

		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			System.out.println("DAO seatsave 오류");
			e.printStackTrace();
			throw e;
		} finally {
			DBConn.close(conn, pstmt);
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
						System.out.println("중복 좌석 발견: Flight_id=" + seat.getFlight_id() +
								", Row=" + seat.getRow() + ", Seat=" + seat.getSeat());
						return true;
					}
				}

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
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}

		return false;
	}

	@Override
	public int saveSchdulesDB(List<SaveSchedulesDBDTO> scheduleList) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int totalSavedCount = 0;

		String sql = "INSERT INTO flight (flight_id, aircraft_id, departure_airport_id, arrival_airport_id, arrival_time, departure_time, terminal, status, flightNo) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);

			for (SaveSchedulesDBDTO dto : scheduleList) {
				String id = dto.getId();
				String dateStr = id.split("-all-")[0];
				LocalDate date = LocalDate.parse(dateStr);
				String flid = id.split("-all-")[1];
				UUID uuid = UUID.randomUUID();
				pstmt.setString(1, uuid.toString());
				pstmt.setString(2, "B789");
				pstmt.setString(3, dto.getOrigin());
				pstmt.setString(4, dto.getDestination());

				if ("N/A".equalsIgnoreCase(dto.getDepartureTime())) {
					pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
				} else {
					String departureDateTimeStr = date + " " + dto.getDepartureTime() + ":00";
					pstmt.setTimestamp(5, Timestamp.valueOf(departureDateTimeStr));
				}

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

			int[] resultCounts = pstmt.executeBatch();
			totalSavedCount = resultCounts.length;

		} catch (Exception e) {
			System.out.println("DAO seatsave 오류");
			e.printStackTrace();
		} finally {
			DBConn.close(conn, pstmt);
		}

		return totalSavedCount;
	}

	@Override
	public boolean refreshCheck(List<SaveSchedulesDBDTO> refresList) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(*) FROM flight WHERE flight_id = ?";

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);

			for (SaveSchedulesDBDTO refresh : refresList) {
				String id = refresh.getId();
				String dateStr = id.split("-all-")[0];
				String flid = id.split("-all-")[1];
				pstmt.setString(1, flid);
				rs = pstmt.executeQuery();

				if (rs.next()) {
					int count = rs.getInt(1);
					if (count > 0) {
						System.out.println("중복 좌석 발견: Flight_id=" + flid);
						return true;
					}
				}

				if (rs != null) {
					rs.close();
					rs = null;
				}
			}

		} catch (Exception e) {
			System.out.println("DAO checkDuplicateSeat 오류");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}

		return false;
	}

	@Override
	public List<SaveSchedulesDBDTO> refreshSchdules(List<SaveSchedulesDBDTO> refresList) throws Exception {
		List<SaveSchedulesDBDTO> insertedList = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmtCheck = null;
		PreparedStatement pstmtInsert = null;
		ResultSet rs = null;

		String sqlCheck = "SELECT COUNT(*) FROM flight WHERE departure_airport_id = ? AND arrival_airport_id = ? AND arrival_time = ? AND departure_time = ?";
		String sqlInsert = "INSERT INTO flight (flight_id, aircraft_id, departure_airport_id, arrival_airport_id, arrival_time, departure_time, terminal, status, flifhtNo) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			conn = DBConn.getConnection();
			pstmtCheck = conn.prepareStatement(sqlCheck);
			pstmtInsert = conn.prepareStatement(sqlInsert);

			for (SaveSchedulesDBDTO refresh : refresList) {
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
				LocalDate date = LocalDate.parse(dateStr);

				String departureTime = refresh.getDepartureTime();
				String arrivalTime = refresh.getArrivalTime();

				if (departureTime == null || "N/A".equalsIgnoreCase(departureTime)) {
					departureTime = "00:00";
				}
				if (arrivalTime == null || "N/A".equalsIgnoreCase(arrivalTime)) {
					arrivalTime = "00:00";
				}

				String departureDateTimeStr = date + " " + departureTime + ":00";
				String arrivalDateTimeStr = date + " " + arrivalTime + ":00";

				String origin = refresh.getOrigin() != null ? refresh.getOrigin() : "UNKNOWN";
				String destination = refresh.getDestination() != null ? refresh.getDestination() : "UNKNOWN";

				pstmtCheck.setString(1, origin);
				pstmtCheck.setString(2, destination);
				pstmtCheck.setTimestamp(3, Timestamp.valueOf(arrivalDateTimeStr));
				pstmtCheck.setTimestamp(4, Timestamp.valueOf(departureDateTimeStr));
				rs = pstmtCheck.executeQuery();

				int count = 0;
				if (rs.next()) {
					count = rs.getInt(1);
				}
				rs.close();
				rs = null;

				if (count == 0) {
					UUID uuid = UUID.randomUUID();

					pstmtInsert.setString(1, uuid.toString());
					pstmtInsert.setString(2, "B789");
					pstmtInsert.setString(3, origin);
					pstmtInsert.setString(4, destination);

					if ("N/A".equalsIgnoreCase(refresh.getDepartureTime())) {
						pstmtInsert.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
					} else {
						pstmtInsert.setTimestamp(5, Timestamp.valueOf(departureDateTimeStr));
					}

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
						insertedList.add(refresh);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("DAO checkDuplicateSeat 오류");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (pstmtCheck != null) {
				try {
					pstmtCheck.close();
				} catch (Exception e) {
				}
			}
			if (pstmtInsert != null) {
				try {
					pstmtInsert.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return insertedList;
	}

	@Override
	public int priceSave(List<ClassPriceSaveDTO> priceList, String flightid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int checkid = 0;

		String sql = "INSERT INTO seat_price (flight_id, class_id, price) VALUES (?, ?, ?)";

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);

			for (ClassPriceSaveDTO dto : priceList) {
				pstmt.setString(1, flightid);
				pstmt.setString(2, dto.getClassId());
				pstmt.setInt(3, Integer.parseInt(dto.getPrice()));
				pstmt.addBatch();
			}

			int[] result = pstmt.executeBatch();

			for (int i : result) {
				if (i == Statement.SUCCESS_NO_INFO || i >= 0) {
					checkid++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConn.close(conn, pstmt);
		}
		return checkid;
	}

	@Override
	public List<SearchUserDTO> searchUsers(String username) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM users WHERE korean_name = ?";
		List<SearchUserDTO> list = new ArrayList<SearchUserDTO>();

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SearchUserDTO dto = new SearchUserDTO();
				dto.setUser_no(rs.getString("user_no"));
				dto.setGrade(rs.getString("grade"));
				dto.setUser_id(rs.getString("user_id"));
				dto.setPw(rs.getString("password"));
				dto.setEmail(rs.getString("email"));
				dto.setKo_name(rs.getString("korean_name"));
				dto.setEn_name(rs.getString("english_name"));
				dto.setBirth_date(rs.getDate("birth_date"));
				dto.setGender(rs.getString("gender"));
				dto.setAddress(rs.getString("address"));
				dto.setPhone_number(rs.getString("phone"));
				dto.setCreated_at(rs.getDate("reg_date"));
				dto.setStatus(rs.getString("status"));
				list.add(dto);
			}

		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			System.out.println("DAO seatsave 오류");
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			DBConn.close(conn, pstmt);
		}

		return list;
	}

	@Override
	public List<DashBoardStatsDTO> dashLoad() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT gender FROM users";
		String sql2 = "SELECT count(*) FROM booking";
		DashBoardStatsDTO dto = new DashBoardStatsDTO();
		List<DashBoardStatsDTO> resultList = new ArrayList<>();

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String gender = rs.getString("gender");

				if ("M".equalsIgnoreCase(gender)) {
					dto.setMaleCount(dto.getMaleCount() + 1);
				} else if ("F".equalsIgnoreCase(gender)) {
					dto.setFmaleCount(dto.getFmaleCount() + 1);
				}
			}

			dto.setTotalCount(dto.getFmaleCount() + dto.getMaleCount());
			resultList.add(dto);

			rs.close();

			pstmt = conn.prepareStatement(sql2);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto.setReservations(rs.getInt(1));
			}

			resultList.add(dto);
		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			System.out.println("DAO dashLoad 오류");
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			DBConn.close(conn, pstmt);
		}

		return resultList;
	}

	@Override
	public List<SeatRevenueDTO> seatRevenue() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT " +
				"    sp.class_id, " +
				"    SUM(sp.price) AS total_revenue, " +
				"    ROUND(SUM(sp.price) * 100.0 / ( " +
				"        SELECT SUM(sp2.price) " +
				"        FROM booking b2 " +
				"        JOIN seat_price sp2 ON b2.flight_id = sp2.flight_id " +
				"    ), 2) AS revenue_ratio_percent " +
				"FROM booking b " +
				"JOIN seat_price sp ON b.flight_id = sp.flight_id " +
				"GROUP BY sp.class_id " +
				"ORDER BY total_revenue DESC";

		List<SeatRevenueDTO> resultList = new ArrayList<>();

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SeatRevenueDTO dto = new SeatRevenueDTO();

				dto.setSeat_id(rs.getString("class_id"));
				dto.setTotal_revenue(rs.getInt("total_revenue"));
				dto.setRevenue_percent(rs.getDouble("revenue_ratio_percent"));
				resultList.add(dto);
			}
		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			System.out.println("DAO dashLoad 오류");
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			DBConn.close(conn, pstmt);
		}

		return resultList;
	}

	@Override
	public List<MonthReservationDTO> monthReservation(String year) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT "
			         + " TO_CHAR(f.departure_time, 'MM') AS month, "
			         + " COUNT(b.booking_id) AS booking_count "
			         + " FROM booking b "
			         + " JOIN flight f ON b.flight_id = f.flight_id "
			         + " WHERE TO_CHAR(f.departure_time, 'YYYY') = ? "
			         + " GROUP BY TO_CHAR(f.departure_time, 'YYYY-MM') "
			         + " ORDER BY month";

		List<MonthReservationDTO> resultList = new ArrayList<>();

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, year);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MonthReservationDTO dto = new MonthReservationDTO();
				dto.setReservationdate(rs.getString("month"));
				dto.setReservation_count(rs.getInt("booking_count"));
				resultList.add(dto);
			}
		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			System.out.println("DAO monthReservation 오류");
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			DBConn.close(conn, pstmt);
		}

		return resultList;
	}

	@Override
	public List<PopularDTO> popularroutes(String year) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT " 
			         + " da.airport_name AS 출발지, "
			         + " aa.airport_name AS 도착지, "
			         + " COUNT(b.booking_id) AS 예약건수 "
			         + " FROM " 
			         + " flight f "
 			         + " JOIN " 
			         + " booking b ON f.flight_id = b.flight_id "
			         + " JOIN " 
			         + " airport da ON f.departure_airport_id = da.airport_id "
			         + " JOIN " 
			         + " airport aa ON f.arrival_airport_id = aa.airport_id "
			         + " WHERE " 
			         + "  SUBSTRING(f.departure_time, 1, 4) = ? "
			         + " GROUP BY "
			         + " da.airport_name, aa.airport_name "
			         + " ORDER BY "
			         + " 예약건수 DESC "
			         + " LIMIT 5 " ;

		List<PopularDTO> resultList = new ArrayList<>();
		int totalReservations = 0; // 전체 예약 수 계산용

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, year);
			rs = pstmt.executeQuery();

			// 먼저 데이터를 수집하고 전체 예약 수 계산
			while (rs.next()) {
				PopularDTO dto = new PopularDTO();
				dto.setDeparture_airport_id(rs.getString("출발지"));
				dto.setArrival_airport_id(rs.getString("도착지"));
				dto.setCount(rs.getInt("예약건수"));
				totalReservations += dto.getCount();
				resultList.add(dto);
			}
			
		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			System.out.println("DAO monthReservation 오류");
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			DBConn.close(conn, pstmt);
		}

		return resultList;
	}

	@Override
	public List<AdminReservationDTO> reservations(String searchType, String searchKeyword, String status) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT "
				+ "    b.booking_id AS 예약번호,"
				+ "    u.korean_name AS 고객명,"
				+ "    u.email AS 이메일,"
				+ "    u.phone AS 전화번호,"
				+ " "
				+ "    da.airport_name AS 출발지,"
				+ "    aa.airport_name AS 도착지,"
				+ "    SUBSTRING ( f.arrival_time, 1, 10 ) AS 출발일,"
				+ " "
				+ "    SUBSTRING ( bs.booking_completed_at, 1, 10 ) AS 예약완료시간,"
				+ " "
				+ "    fs.class_id AS 좌석등급,"
				+ "    sp.price AS 좌석가격, "
				+ "    f.flightNO AS 항공편명, "
				+ "    CAST(f.departure_time AS CHAR) AS 도착시간 ,"
				+ "    CAST(u.birth_date AS CHAR) AS 생일 "
				+ " "
				+ "FROM "
				+ "    booking b "
				+ "JOIN users u ON b.user_no = u.user_no "
				+ "JOIN flight f ON b.flight_id = f.flight_id "
				+ "JOIN airport da ON f.departure_airport_id = da.airport_id "
				+ "JOIN airport aa ON f.arrival_airport_id = aa.airport_id "
				+ "JOIN booking_status bs ON b.booking_id = bs.booking_id "
				+ "JOIN booking_seat bseat ON b.booking_id = bseat.booking_id "
				+ "JOIN flight_seat fs ON bseat.flight_seat_id = fs.seat_id "
				+ "                     AND f.flight_id = fs.flight_id "
				+ "JOIN seat_price sp ON fs.class_id = sp.class_id "
				+ "                   AND f.flight_id = sp.flight_id " 
				+ "WHERE u.korean_name = ? ";

		List<AdminReservationDTO> resultList = new ArrayList<>();
		int totalReservations = 0; // 전체 예약 수 계산용

		try {
			conn = DBConn.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchKeyword);
			rs = pstmt.executeQuery();

			// 먼저 데이터를 수집하고 전체 예약 수 계산
			while (rs.next()) {
				AdminReservationDTO dto = new AdminReservationDTO();
				dto.setBooking_id(rs.getString("예약번호"));
				dto.setUser_name(rs.getString("고객명"));
				dto.setEmail(rs.getString("이메일"));
				dto.setPhone(rs.getString("전화번호"));
				dto.setStart(rs.getString("출발지"));
				dto.setEnd(rs.getString("도착지"));
				dto.setStartDate(rs.getString("출발일"));
				dto.setBookingDate(rs.getString("예약완료시간"));
				dto.setSeat_class(rs.getString("좌석등급"));
				dto.setPassenger(1);
				dto.setTotalPrice(rs.getInt("좌석가격"));
				dto.setFlightNO(rs.getString("항공편명"));
				dto.setEndDate(rs.getString("도착시간"));
				dto.setBirth_date(rs.getString("생일"));
				resultList.add(dto);
			}
			
		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			System.out.println("DAO reservations 오류");
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			DBConn.close(conn, pstmt);
		}

		return resultList;
	}
}
