package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.model.dto.BookingDTO;
import com.koreanair.model.service.BookingService;

public class BookingHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookingService bookingService = new BookingService();
		
		

		
		
		BookingDTO.builder()
			.flightId(request.getParameter("outboundFlightId"))
		bookingService.saveBookingToPending(bookingDTO);
		
		
		
		
		return "/views/booking/booking.jsp";
	}

}
