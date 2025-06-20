package com.koreanair.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import com.koreanair.model.dto.PaymentPrepareDTO;
import com.koreanair.model.dao.PaymentPrepareDAO;
import com.koreanair.util.DBConnection;

public class PaymentPrepareDAOImpl implements PaymentPrepareDAO {

	@Override
	public boolean insertMerchantUid(PaymentPrepareDTO dto) throws Exception {
		String sql = "INSERT INTO payment (payment_id, booking_id, status_code, merchant_uid, imp_uid, payment_method, payment_amount, created_at ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			String paymentId = "PAY_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12); // ✅ 고유 ID 생성
			String status = "READY";
			String impUid = "imp87380624";

			pstmt.setString(1, paymentId);
			pstmt.setString(2, dto.getBookingId());
			pstmt.setString(3, status);
			pstmt.setString(4, dto.getMerchantUid());
			pstmt.setString(5, impUid);
			pstmt.setString(6, dto.getPayment_method());
			pstmt.setString(7, dto.getAmount());
			pstmt.setString(8, dto.getCreated_at());
			

			int result = pstmt.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			throw new Exception("결제 정보 저장 중 데이터베이스 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}
}