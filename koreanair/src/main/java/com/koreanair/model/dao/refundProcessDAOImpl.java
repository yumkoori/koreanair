package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.koreanair.model.dto.RefundProcessDTO;
import com.koreanair.util.DBConnection;

public class refundProcessDAOImpl implements RefundProcessDAO {

    @Override
    public String validateAndGetMerchantUid(RefundProcessDTO dto) throws Exception {
        String merchantUid = null;
        
        // 위변조 검사용 SQL 쿼리
        String sql = "SELECT merchant_uid FROM payment " +
                    "WHERE booking_id = (SELECT booking_id FROM booking " +
                    "WHERE booking_id = ? AND user_no = ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dto.getBookingId());
            pstmt.setInt(2, dto.getUserNo());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    merchantUid = rs.getString("merchant_uid");
                    System.out.println("[SUCCESS] 위변조 검사 통과 - BookingId: " + dto.getBookingId() + 
                                     ", UserNo: " + dto.getUserNo() + ", MerchantUid: " + merchantUid);
                } else {
                    System.out.println("[WARNING] 위변조 검사 실패 - BookingId: " + dto.getBookingId() + 
                                     ", UserNo: " + dto.getUserNo());
                }
            }
            
        } catch (SQLException e) {
            System.err.println("[DB ERROR] 위변조 검사 중 오류 발생: " + e.getMessage());
            throw new Exception("데이터베이스 연결 또는 쿼리 실행 중 오류가 발생했습니다.", e);
        }
        
        return merchantUid;
    }

    @Override
    public boolean updateRefundStatus(String merchantUid, String status) throws Exception {
        boolean result = false;
        
        // 환불 상태 업데이트 SQL
        String sql = "UPDATE payment SET status_code = ? WHERE merchant_uid = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, merchantUid);
            
            int rowsAffected = pstmt.executeUpdate();
            result = rowsAffected > 0;
            
            if (result) {
                System.out.println("[SUCCESS] 환불 상태 업데이트 성공 - MerchantUid: " + merchantUid + 
                                 ", Status: " + status);
            } else {
                System.out.println("[WARNING] 환불 상태 업데이트 실패 - MerchantUid: " + merchantUid);
            }
            
        } catch (SQLException e) {
            System.err.println("[DB ERROR] 환불 상태 업데이트 중 오류 발생: " + e.getMessage());
            throw new Exception("환불 상태 업데이트 중 오류가 발생했습니다.", e);
        }
        
        return result;
    }

    @Override
    public String getPaymentAmount(String merchantUid) throws Exception {
        String paymentAmount = null;
        
        // 결제 금액 조회 SQL
        String sql = "SELECT payment_amount FROM payment WHERE merchant_uid = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, merchantUid);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    paymentAmount = rs.getString("payment_amount");
                    System.out.println("[SUCCESS] 결제 금액 조회 성공 - MerchantUid: " + merchantUid + 
                                     ", Amount: " + paymentAmount);
                } else {
                    System.out.println("[WARNING] 결제 금액 조회 실패 - MerchantUid: " + merchantUid);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("[DB ERROR] 결제 금액 조회 중 오류 발생: " + e.getMessage());
            throw new Exception("결제 금액 조회 중 오류가 발생했습니다.", e);
        }
        
        return paymentAmount;
    }
} 