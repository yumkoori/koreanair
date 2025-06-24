package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestPaymentHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bookingId = request.getParameter("bookingId");
		String totalAmount = request.getParameter("totalAmount");

		System.out.println(bookingId);
		System.out.println(totalAmount);

		// 리다이렉트 방식으로 파라미터 전달
		String redirectUrl = request.getContextPath() + "/views/payment/Payment_Page.jsp?bookingId=" + bookingId + "&totalAmount=" + totalAmount;
		response.sendRedirect(redirectUrl);
		
		return null; // 리다이렉트 후에는 null 반환
	}

}
