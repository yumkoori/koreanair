package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.dto.User;
import com.koreanair.util.DBConnection;
import com.koreanair.util.PasswordUtil;

public class UserDAO {
    
    // 회원가입
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (user_id, password, korean_name, english_name, birth_date, gender, email, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getKoreanName());
            pstmt.setString(4, user.getEnglishName());
            pstmt.setDate(5, user.getBirthDate());
            pstmt.setString(6, user.getGender());
            pstmt.setString(7, user.getEmail());
            pstmt.setString(8, user.getPhone());
            pstmt.setString(9, user.getAddress());
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 로그인 (사용자 인증) - BCrypt 사용
    public User loginUser(String userId, String password) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                
                // BCrypt로 비밀번호 검증
                if (PasswordUtil.verifyPassword(password, hashedPassword)) {
                    User user = new User();
                    user.setUserId(rs.getString("user_id"));
                    user.setPassword(rs.getString("password"));
                    user.setKoreanName(rs.getString("korean_name"));
                    user.setEnglishName(rs.getString("english_name"));
                    user.setBirthDate(rs.getDate("birth_date"));
                    user.setGender(rs.getString("gender"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setRegDate(rs.getTimestamp("reg_date"));
                    return user;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    // 사용자 ID 중복 체크
    public boolean isUserIdExists(String userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return false;
    }
    
    // 회원 정보 조회
    public User getUserById(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setPassword(rs.getString("password"));
                user.setKoreanName(rs.getString("korean_name"));
                user.setEnglishName(rs.getString("english_name"));
                user.setBirthDate(rs.getDate("birth_date"));
                user.setGender(rs.getString("gender"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setRegDate(rs.getTimestamp("reg_date"));
                return user;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    // 회원탈퇴
    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // [추가된 메소드] 특정 사용자의 모든 예약 목록 조회
    public List<ReservationDTO> getUserReservations(String userId) {
        List<ReservationDTO> reservationList = new ArrayList<>();
        String sql = "SELECT b.booking_id, f.departure_time, f.arrival_time, " +
                     "da.airport_id AS departure_airport_id, da.airport_name AS departure_airport_name, " +
                     "aa.airport_id AS arrival_airport_id, aa.airport_name AS arrival_airport_name " +
                     "FROM booking b " +
                     "JOIN flight f ON b.flight_id = f.flight_id " +
                     "JOIN passenger p ON b.booking_id = p.booking_id " +
                     "JOIN users u ON p.user_no = u.user_no " +
                     "JOIN airport da ON f.departure_airport_id = da.airport_id " +
                     "JOIN airport aa ON f.arrival_airport_id = aa.airport_id " +
                     "WHERE u.user_id = ? " +
                     "GROUP BY b.booking_id, f.departure_time, f.arrival_time, departure_airport_id, departure_airport_name, arrival_airport_id, arrival_airport_name " +
                     "ORDER BY f.departure_time DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ReservationDTO reservation = new ReservationDTO();
                reservation.setBookingId(rs.getString("booking_id"));
                reservation.setDepartureTime(rs.getTimestamp("departure_time"));
                reservation.setArrivalTime(rs.getTimestamp("arrival_time"));
                reservation.setDepartureAirportId(rs.getString("departure_airport_id"));
                reservation.setDepartureAirportName(rs.getString("departure_airport_name"));
                reservation.setArrivalAirportId(rs.getString("arrival_airport_id"));
                reservation.setArrivalAirportName(rs.getString("arrival_airport_name"));
                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return reservationList;
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