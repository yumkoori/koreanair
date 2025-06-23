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
                	return mapResultSetToUser(rs);
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
            	return mapResultSetToUser(rs);
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
                     "WHERE u.user_id = ? AND f.departure_time > NOW() " + // 1. 미래의 예약만 필터링
                     "GROUP BY b.booking_id, f.departure_time, f.arrival_time, departure_airport_id, departure_airport_name, arrival_airport_id, arrival_airport_name " +
                     "ORDER BY f.departure_time ASC " + // 2. 가장 가까운 순서로 정렬
                     "LIMIT 1"; // 3. 1개의 결과만 가져옴

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            // 결과가 1개 또는 0개이므로 while 대신 if를 사용해도 됩니다.
            if (rs.next()) {
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
    
    // 이메일 기준 회원탈퇴 (카카오 계정용)
    public boolean deleteUserByEmail(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 카카오 ID로 사용자 조회
    public User getUserByKakaoId(String kakaoId) {
        String sql = "SELECT * FROM users WHERE kakao_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kakaoId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    // 이메일로 사용자 조회 (카카오 연동용)
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    // 카카오 사용자 등록 (신규 회원가입)
    public boolean insertKakaoUser(User user) {
        String sql = "INSERT INTO users (korean_name, english_name, birth_date, gender, email, phone, address, kakao_id, login_type, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getKoreanName());
            pstmt.setString(2, user.getEnglishName());
            pstmt.setDate(3, user.getBirthDate());
            pstmt.setString(4, user.getGender());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPhone());
            pstmt.setString(7, user.getAddress());
            pstmt.setString(8, user.getKakaoId());
            pstmt.setString(9, user.getLoginType());
            pstmt.setString(10, user.getProfileImage());
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 카카오 계정에 일반 계정 정보 연동 (일반 회원가입 시 카카오 계정이 있는 경우)
    public boolean linkNormalToKakaoUser(User user) {
        // 카카오 정보는 보존하고, 비어있는 필드만 일반 계정 정보로 채움
        String sql = "UPDATE users SET " +
                    "user_id = ?, " +
                    "password = ?, " +
                    "korean_name = COALESCE(NULLIF(korean_name, ''), ?), " +
                    "english_name = COALESCE(NULLIF(english_name, ''), ?), " +
                    "birth_date = COALESCE(birth_date, ?), " +
                    "gender = COALESCE(NULLIF(gender, ''), ?), " +
                    "phone = COALESCE(NULLIF(phone, ''), ?), " +
                    "address = COALESCE(NULLIF(address, ''), ?), " +
                    "login_type = 'both' " +
                    "WHERE email = ?";
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
            pstmt.setString(7, user.getPhone());
            pstmt.setString(8, user.getAddress());
            pstmt.setString(9, user.getEmail());
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 기존 계정에 카카오 연동 (이메일이 같은 경우)
    public boolean linkKakaoToExistingUser(String email, String kakaoId, String profileImage) {
        String sql = "UPDATE users SET kakao_id = ?, login_type = 'both', profile_image = ? WHERE email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kakaoId);
            pstmt.setString(2, profileImage);
            pstmt.setString(3, email);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 회원 정보 수정 (비밀번호 제외)
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET korean_name = ?, english_name = ?, birth_date = ?, gender = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getKoreanName());
            pstmt.setString(2, user.getEnglishName());
            pstmt.setDate(3, user.getBirthDate());
            pstmt.setString(4, user.getGender());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPhone());
            pstmt.setString(7, user.getAddress());
            pstmt.setString(8, user.getUserId());
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 비밀번호 변경
    public boolean updatePassword(String userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userId);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // ResultSet을 User 객체로 매핑하는 헬퍼 메소드
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
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
        user.setKakaoId(rs.getString("kakao_id"));
        user.setLoginType(rs.getString("login_type"));
        user.setProfileImage(rs.getString("profile_image"));
        return user;
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