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
		try (Connection conn = DBConnection.getConnection()) {
			insertMerchantUid(dto, conn);
			return true;
		}
	}

	/**
	 * Connection을 받아서 payment 테이블에 저장하고 paymentId를 반환합니다.
	 */
	public String insertMerchantUid(PaymentPrepareDTO dto, Connection conn) throws Exception {
		String sql = "INSERT INTO payment (payment_id, booking_id, status_code, merchant_uid, imp_uid, payment_method, payment_amount, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			String paymentId = "PAY_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
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
			if (result > 0) {
				return paymentId;
			} else {
				throw new Exception("payment 테이블 저장에 실패했습니다.");
			}
		} catch (SQLException e) {
			throw new Exception("결제 정보 저장 중 데이터베이스 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean insertPaymentRequestLog(String paymentId, String merchantUid, String payMethod, 
	                                     String requestedAmount, String requestedAt) throws Exception {
		try (Connection conn = DBConnection.getConnection()) {
			// 기존 파라미터를 DTO로 변환하여 새로운 메서드 호출
			PaymentPrepareDTO dto = new PaymentPrepareDTO("", merchantUid, payMethod, requestedAmount, requestedAt);
			return insertPaymentRequestLog(paymentId, dto, conn);
		}
	}

	/**
	 * Connection을 받아서 payment_request_log 테이블에 저장합니다. (기존 파라미터 방식)
	 */
	public boolean insertPaymentRequestLog(String paymentId, String merchantUid, String payMethod, 
	                                     String requestedAmount, String requestedAt, Connection conn) throws Exception {
		// 기존 파라미터를 DTO로 변환하여 새로운 메서드 호출
		PaymentPrepareDTO dto = new PaymentPrepareDTO("", merchantUid, payMethod, requestedAmount, requestedAt);
		return insertPaymentRequestLog(paymentId, dto, conn);
	}

	/**
	 * Connection을 받아서 payment_request_log 테이블에 저장합니다. (DTO 방식)
	 */
	public boolean insertPaymentRequestLog(String paymentId, PaymentPrepareDTO dto, Connection conn) throws Exception {
		
		// 입력값 유효성 검증
		if (paymentId == null || paymentId.trim().isEmpty()) {
			throw new IllegalArgumentException("결제 ID가 누락되었습니다.");
		}
		if (dto == null) {
			throw new IllegalArgumentException("결제 준비 정보가 누락되었습니다.");
		}
		if (dto.getMerchantUid() == null || dto.getMerchantUid().trim().isEmpty()) {
			throw new IllegalArgumentException("주문 번호가 누락되었습니다.");
		}
		if (dto.getPayment_method() == null || dto.getPayment_method().trim().isEmpty()) {
			throw new IllegalArgumentException("결제 방법이 누락되었습니다.");
		}
		if (dto.getAmount() == null || dto.getAmount().trim().isEmpty()) {
			throw new IllegalArgumentException("요청 금액이 누락되었습니다.");
		}
		if (dto.getCreated_at() == null || dto.getCreated_at().trim().isEmpty()) {
			throw new IllegalArgumentException("요청 시간이 누락되었습니다.");
		}
		
		// 1. 기존 행 개수 조회하여 새로운 refund_request_id 생성
		String countSql = "SELECT COUNT(*) FROM payment_request_log";
		int nextRefundRequestId = 1;
		
		try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
			var rs = countStmt.executeQuery();
			if (rs.next()) {
				nextRefundRequestId = rs.getInt(1) + 1;
			}
			System.out.println("[INFO] 새로운 refund_request_id 생성: " + nextRefundRequestId);
		} catch (SQLException e) {
			System.err.println("[DB ERROR] 기존 행 개수 조회 실패: " + e.getMessage());
			throw new Exception("기존 행 개수 조회 중 데이터베이스 오류가 발생했습니다: " + e.getMessage(), e);
		} catch (Exception e) {
			System.err.println("[SYSTEM ERROR] 행 개수 조회 중 예상치 못한 오류: " + e.getMessage());
			throw new Exception("행 개수 조회 중 시스템 오류가 발생했습니다: " + e.getMessage(), e);
		}
		
		// 2. payment_request_log 테이블에 데이터 삽입
		String sql = "INSERT INTO payment_request_log (request_log_id, payment_id, merchant_uid, pay_method, requested_payment_amount, requested_at) VALUES (?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, nextRefundRequestId);
			pstmt.setString(2, paymentId);
			pstmt.setString(3, dto.getMerchantUid());
			pstmt.setString(4, dto.getPayment_method());
			pstmt.setString(5, dto.getAmount());
			pstmt.setString(6, dto.getCreated_at());
			
			int result = pstmt.executeUpdate();
			
			if (result > 0) {
				System.out.println("[SUCCESS] 결제 요청 로그 저장 성공 - RefundRequestId: " + nextRefundRequestId + 
				                 ", PaymentId: " + paymentId + ", MerchantUid: " + dto.getMerchantUid());
				return true;
			} else {
				System.err.println("[WARNING] 결제 요청 로그 저장 실패 - 영향받은 행이 없음");
				throw new Exception("결제 요청 로그 저장에 실패했습니다. 데이터가 저장되지 않았습니다.");
			}
			
		} catch (SQLException e) {
			System.err.println("[DB ERROR] 결제 요청 로그 저장 실패 - PaymentId: " + paymentId + 
			                 ", MerchantUid: " + dto.getMerchantUid() + ", Error: " + e.getMessage());
			
			// SQL 오류 유형별 상세 처리
			if (e.getErrorCode() == 1062) { // MySQL 중복 키 오류
				throw new Exception("결제 요청 로그 저장 실패: 중복된 데이터입니다.", e);
			} else if (e.getErrorCode() == 1146) { // MySQL 테이블 없음 오류
				throw new Exception("결제 요청 로그 저장 실패: payment_request_log 테이블이 존재하지 않습니다.", e);
			} else if (e.getErrorCode() == 1054) { // MySQL 컬럼 없음 오류
				throw new Exception("결제 요청 로그 저장 실패: 테이블 구조가 올바르지 않습니다.", e);
			} else {
				throw new Exception("결제 요청 로그 저장 중 데이터베이스 오류가 발생했습니다: " + e.getMessage(), e);
			}
			
		} catch (Exception e) {
			System.err.println("[SYSTEM ERROR] 결제 요청 로그 저장 중 예상치 못한 오류 - PaymentId: " + paymentId + 
			                 ", Error: " + e.getMessage());
			throw new Exception("결제 요청 로그 저장 중 시스템 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean insertPaymentWithLog(PaymentPrepareDTO dto) throws Exception {
		Connection conn = null;
		
		try {
			// 1. 커넥션 획득 및 트랜잭션 시작
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작
			
			// 2. payment 테이블 저장 (기존 메서드 활용)
			String paymentId = insertMerchantUid(dto, conn);
			
			// 3. payment_request_log 테이블 저장 (insertMerchantUid에서 생성된 값들 활용)
			boolean logResult = insertPaymentRequestLog(paymentId, dto, conn);
			
			if (!logResult) {
				throw new Exception("payment_request_log 테이블 저장에 실패했습니다.");
			}
			
			// 4. 트랜잭션 커밋
			conn.commit();
			System.out.println("[SUCCESS] 결제 정보 및 로그 저장 성공 - PaymentId: " + paymentId + ", MerchantUid: " + dto.getMerchantUid());
			return true;
			
		} catch (Exception e) {
			// 5. 트랜잭션 롤백
			if (conn != null) {
				try {
					conn.rollback();
					System.err.println("[ROLLBACK] 트랜잭션 롤백 완료 - MerchantUid: " + dto.getMerchantUid());
				} catch (SQLException rollbackEx) {
					System.err.println("[ROLLBACK ERROR] 롤백 중 오류 발생: " + rollbackEx.getMessage());
				}
			}
			throw new Exception("결제 정보 및 로그 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
			
		} finally {
			// 6. 리소스 정리
			if (conn != null) {
				try {
					conn.setAutoCommit(true); // 자동 커밋 복원
					conn.close();
				} catch (SQLException e) {
					System.err.println("[RESOURCE ERROR] 리소스 정리 중 오류 발생: " + e.getMessage());
				}
			}
		}
	}
}