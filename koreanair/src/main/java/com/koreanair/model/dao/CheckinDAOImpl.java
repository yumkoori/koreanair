package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.koreanair.model.dto.CheckinDTO;
import com.koreanair.util.DBConnection;

public class CheckinDAOImpl implements CheckinDAO {

    @Override
    public boolean createCheckin(CheckinDTO checkin) {
        String sql = "INSERT INTO check_in (check_in, booking_seat_id, check_in_time, check_in_type, baggage_count, baggage_tag_number, check_in_status) "
                   + "VALUES (?, ?, NOW(), ?, ?, ?, ?)";
        
        // [로그 추가] 실행될 SQL과 파라미터 확인
        System.out.println("[DEBUG - CheckinDAOImpl] createCheckin 실행");
        System.out.println("  > SQL: " + sql);
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // [로그 추가]
            System.out.println("  > 파라미터 1 (check_in_id): " + checkin.getCheckIn());
            System.out.println("  > 파라미터 2 (booking_seat_id): " + checkin.getBookingSeatId());
            System.out.println("  > 파라미터 3 (check_in_type): " + checkin.getCheckInType());
            System.out.println("  > 파라미터 4 (baggage_count): " + checkin.getBaggageCount());
            System.out.println("  > 파라미터 5 (baggage_tag_number): " + checkin.getBaggageTagNumber());
            System.out.println("  > 파라미터 6 (check_in_status): " + checkin.getCheckInStatus());

            pstmt.setString(1, checkin.getCheckIn());
            pstmt.setString(2, checkin.getBookingSeatId());
            pstmt.setString(3, checkin.getCheckInType());
            pstmt.setInt(4, checkin.getBaggageCount());
            pstmt.setString(5, checkin.getBaggageTagNumber());
            pstmt.setString(6, checkin.getCheckInStatus());

            int result = pstmt.executeUpdate();
            
            // [로그 추가] INSERT 결과 확인
            System.out.println("[DEBUG - CheckinDAOImpl] INSERT 결과 (0보다 크면 성공): " + result);
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * [추가된 메소드] 예약 번호(bookingId)와 연결된 check_in 레코드를 삭제합니다.
     * booking_seat 테이블을 서브쿼리로 사용하여 booking_seat_id를 찾아 삭제를 수행합니다.
     */
    @Override
    public boolean deleteCheckin(String bookingId) {
        String sql = "DELETE FROM check_in WHERE booking_seat_id IN " +
                     "(SELECT booking_seat_id FROM booking_seat WHERE booking_id = ?)";
        
        System.out.println("[DEBUG - CheckinDAOImpl] deleteCheckin 실행");
        System.out.println("  > SQL: " + sql);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookingId);
            System.out.println("  > 파라미터 1 (bookingId): " + bookingId);

            // 삭제된 행이 0개 이상이면 성공으로 간주 (이미 삭제되었을 수도 있으므로)
            int result = pstmt.executeUpdate();
            System.out.println("[DEBUG - CheckinDAOImpl] DELETE 결과 (0 이상이면 성공): " + result);
            return result >= 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}