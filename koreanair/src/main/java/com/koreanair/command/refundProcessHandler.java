package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.RefundProcessDTO;
import com.koreanair.model.dto.User;
import com.koreanair.model.service.refundProcessService;

public class refundProcessHandler implements CommandHandler {

	private final refundProcessService refundProcessService;

	public refundProcessHandler() {
		this.refundProcessService = new refundProcessService();
	}

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// HTTP 메서드에 따른 처리 분기
		String method = request.getMethod();

		if ("GET".equals(method)) {
			// GET 요청: 환불 확인 페이지 표시
			return handleGetRequest(request, response);
		} else if ("POST".equals(method)) {
			// POST 요청: 환불 처리 실행
			return handlePostRequest(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다.");
			return null;
		}
	}

	/**
	 * GET 요청 처리 - 환불 확인 페이지 표시
	 */
	private String handleGetRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 환불 확인에 필요한 파라미터 받기
		String bookingId = request.getParameter("bookingId");
		String passengerName = request.getParameter("passengerName");

		// 파라미터를 request에 설정하여 JSP에서 사용할 수 있도록 함
		if (bookingId != null) {
			request.setAttribute("bookingId", bookingId);
		}
		if (passengerName != null) {
			request.setAttribute("passengerName", passengerName);
		}

		// 환불 확인 JSP 페이지로 포워딩
		return "/views/payment/refundProcess.jsp";
	}

	/**
	 * POST 요청 처리 - 환불 처리 실행
	 */
	private String handlePostRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setContentType("application/json; charset=UTF-8");

		try {
			// 1. 세션에서 사용자 정보 가져오기
			
			  HttpSession session = request.getSession(false); if (session == null) {
			  response.getWriter().
			  write("{\"success\": false, \"message\": \"세션이 만료되었습니다. 다시 로그인해주세요.\"}");
			  return null; }
			  
			  User user = (User) session.getAttribute("user"); if (user == null) {
			  response.getWriter().
			  write("{\"success\": false, \"message\": \"사용자 정보가 없습니다. 다시 로그인해주세요.\"}");
			  return null; }
			 
			  int userNo = extractUserNo(user);
			

			// 2. 요청 파라미터 추출
			String bookingId = request.getParameter("bookingId");
			String refundReason = request.getParameter("refundReason");

			if (bookingId == null || bookingId.trim().isEmpty()) {
				response.getWriter().write("{\"success\": false, \"message\": \"예약번호가 누락되었습니다.\"}");
				return null;
			}

			System.out.println("[DEBUG] 환불 요청 - BookingId: " + bookingId + ", UserNo: " + userNo);

			// 3. 위변조 검사를 위한 DTO 생성
			RefundProcessDTO validationDto = new RefundProcessDTO(bookingId, userNo);

			// 4. 위변조 검사 수행
			String merchantUid = refundProcessService.validateRefundRequest(validationDto);

			// 5. 환불 처리를 위한 DTO 생성 (amount는 실제로는 DB에서 조회해야 함)
			String amount = getRefundAmount(merchantUid);
			RefundProcessDTO refundDto = new RefundProcessDTO(bookingId, merchantUid, userNo, amount,
					refundReason != null ? refundReason : "사용자 요청");

			// 6. 환불 처리 실행
			boolean refundSuccess = refundProcessService.processRefund(refundDto);

			if (refundSuccess) {
				response.getWriter().write("{\"success\": true, \"message\": \"환불이 성공적으로 처리되었습니다.\"}");
			} else {
				response.getWriter().write("{\"success\": false, \"message\": \"환불 처리 중 오류가 발생했습니다.\"}");
			}

		} catch (IllegalArgumentException e) {
			// 입력값 검증 오류 처리
			System.err.println("[VALIDATION ERROR] " + e.getMessage());
			response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");

		} catch (Exception e) {
			// 시스템 오류 처리
			System.err.println("[SYSTEM ERROR] 환불 처리 중 오류 발생: " + e.getMessage());
			e.printStackTrace(); // 디버깅을 위해 스택 트레이스 출력
			response.getWriter().write("{\"success\": false, \"message\": \"환불 처리 중 시스템 오류가 발생했습니다.\"}");
		}

		return null; // AJAX 응답이므로 JSP 페이지로 이동하지 않음
	}

	/**
	 * User 객체에서 사용자 번호 추출 TODO: User DTO의 실제 구조에 맞게 수정 필요
	 */
	private int extractUserNo(User user) {
		// User DTO에 userNo 필드가 있다면 직접 사용
		// 없다면 userId를 이용해서 DB에서 조회하거나 다른 방법 사용
		// 현재는 임시로 userId를 해시코드로 변환 (실제 구현에서는 수정 필요)
		return Math.abs(user.getUserId().hashCode()) % 1000 + 1;
	}

	/**
	 * merchant_uid로 환불 금액 조회 TODO: 실제 DB에서 결제 금액을 조회하는 로직 구현 필요
	 */
	private String getRefundAmount(String merchantUid) {
		try {
			// refundProcessService를 통해 실제 결제 금액 조회
			String paymentAmount = refundProcessService.getPaymentAmount(merchantUid);
			
			if (paymentAmount != null && !paymentAmount.trim().isEmpty()) {
				System.out.println("[SUCCESS] 환불 금액 조회 성공 - MerchantUid: " + merchantUid + ", Amount: " + paymentAmount);
				return paymentAmount;
			} else {
				System.err.println("[WARNING] 환불 금액 조회 실패 - MerchantUid: " + merchantUid);
				return "0"; // 조회 실패시 0 반환
			}
		} catch (Exception e) {
			System.err.println("[ERROR] 환불 금액 조회 중 오류 발생 - MerchantUid: " + merchantUid + ", Error: " + e.getMessage());
			return "0"; // 오류 발생시 0 반환
		}
	}
}

