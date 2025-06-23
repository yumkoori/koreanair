package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.koreanair.model.dto.PaymentCompareDTO;
import com.koreanair.util.DBConnection;

/**
 * 결제 검증을 위한 DAO 구현체
 */
public class PaymentVerifyDAOImpl implements PaymentVerifyDAO {
    
    /**
     * imp_uid로 DB에서 merchant_uid와 amount를 조회
     * @param impUid 아임포트 결제 고유번호
     * @return PaymentCompareDTO 검증용 결제 정보 (merchant_uid, amount)
     * @throws Exception 조회 중 오류 발생 시
     */
    @Override
    public PaymentCompareDTO getPaymentCompareInfo(String impUid) throws Exception {
        if (impUid == null || impUid.trim().isEmpty()) {
            throw new IllegalArgumentException("imp_uid는 필수값입니다.");
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PaymentCompareDTO compareDTO = null;
        
        try {
            conn = DBConnection.getConnection();
            
            // SQL 쿼리 - 실제 테이블명과 컬럼명은 프로젝트에 맞게 수정 필요
            String sql = "SELECT merchant_uid, amount FROM payment WHERE imp_uid = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, impUid);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                compareDTO = new PaymentCompareDTO();
                compareDTO.setMerchantUid(rs.getString("merchant_uid"));
                compareDTO.setAmount(rs.getInt("amount"));
                
                System.out.println("[DB QUERY SUCCESS] 결제 정보 조회 성공 - imp_uid: " + impUid + 
                                 ", merchant_uid: " + compareDTO.getMerchantUid() + 
                                 ", amount: " + compareDTO.getAmount());
            } else {
                System.err.println("[DB QUERY FAIL] 해당 imp_uid로 결제 정보를 찾을 수 없습니다: " + impUid);
                throw new Exception("해당 imp_uid로 결제 정보를 찾을 수 없습니다: " + impUid);
            }
            
        } catch (SQLException e) {
            System.err.println("[DB ERROR] 결제 정보 조회 중 DB 오류: " + e.getMessage());
            throw new Exception("결제 정보 조회 중 데이터베이스 오류가 발생했습니다: " + e.getMessage(), e);
        } finally {
            // 리소스 해제
            closeResources(rs, pstmt, conn);
        }
        
        return compareDTO;
    }
    
    /**
     * merchant_uid로 DB에서 merchant_uid와 amount를 조회 (아임포트 데이터와 비교용)
     * @param merchantUid 가맹점 주문번호
     * @return PaymentCompareDTO 검증용 결제 정보 (merchant_uid, amount)
     * @throws Exception 조회 중 오류 발생 시
     */
    public PaymentCompareDTO getPaymentCompareInfoByMerchantUid(String merchantUid) throws Exception {
        if (merchantUid == null || merchantUid.trim().isEmpty()) {
            throw new IllegalArgumentException("merchant_uid는 필수값입니다.");
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PaymentCompareDTO compareDTO = null;
        
        try {
            conn = DBConnection.getConnection();
            
            // SQL 쿼리 - 실제 테이블명과 컬럼명은 프로젝트에 맞게 수정 필요
            String sql = "SELECT merchant_uid, payment_amount FROM payment WHERE merchant_uid = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, merchantUid);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                compareDTO = new PaymentCompareDTO();
                compareDTO.setMerchantUid(rs.getString("merchant_uid"));
                compareDTO.setAmount(rs.getInt("payment_amount"));
                
                System.out.println("[DB QUERY SUCCESS] merchant_uid로 결제 정보 조회 성공 - merchant_uid: " + merchantUid + 
                                 ", amount: " + compareDTO.getAmount());
            } else {
                System.err.println("[DB QUERY FAIL] 해당 merchant_uid로 결제 정보를 찾을 수 없습니다: " + merchantUid);
                throw new Exception("해당 merchant_uid로 결제 정보를 찾을 수 없습니다: " + merchantUid);
            }
            
        } catch (SQLException e) {
            System.err.println("[DB ERROR] merchant_uid로 결제 정보 조회 중 DB 오류: " + e.getMessage());
            throw new Exception("결제 정보 조회 중 데이터베이스 오류가 발생했습니다: " + e.getMessage(), e);
        } finally {
            closeResources(rs, pstmt, conn);
        }
        
        return compareDTO;
    }
    
    /**
     * merchant_uid로 결제 상태를 PAID로 업데이트하고 paid_at 시간을 설정
     * @param merchantUid 가맹점 주문번호
     * @return 업데이트 성공 여부
     * @throws Exception 업데이트 중 오류 발생 시
     */
    @Override
    public boolean updatePaymentStatusToPaid(String merchantUid) throws Exception {
        if (merchantUid == null || merchantUid.trim().isEmpty()) {
            throw new IllegalArgumentException("merchant_uid는 필수값입니다.");
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            // SQL 쿼리 - status_code를 PAID로, paid_at을 현재 시간으로 업데이트
            
            String sql = "UPDATE payment SET status_code = 'PAID', paid_at = ? WHERE merchant_uid = ?";
            LocalDateTime now = LocalDateTime.now();
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(now));
            pstmt.setString(2, merchantUid);
            
            int updatedRows = pstmt.executeUpdate();
            
            if (updatedRows > 0) {
                System.out.println("[DB UPDATE SUCCESS] 결제 상태 업데이트 성공 - merchant_uid: " + merchantUid + 
                                 ", status_code: PAID, paid_at: 현재시간");
                return true;
            } else {
                System.err.println("[DB UPDATE FAIL] 해당 merchant_uid로 업데이트할 결제 정보를 찾을 수 없습니다: " + merchantUid);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("[DB ERROR] 결제 상태 업데이트 중 DB 오류: " + e.getMessage());
            throw new Exception("결제 상태 업데이트 중 데이터베이스 오류가 발생했습니다: " + e.getMessage(), e);
        } finally {
            // 리소스 해제
            closeResources(null, pstmt, conn);
        }
    }
    
    /**
     * 리소스 해제 공통 메서드
     */
    private void closeResources(ResultSet rs, PreparedStatement pstmt, Connection conn) {
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