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

		request.setAttribute("bookingId", bookingId);
		request.setAttribute("totalAmount", totalAmount);

		return null;		//payment.jsp 반환 
	}

}
