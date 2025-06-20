package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.model.dto.BookingDTO;
import com.koreanair.model.service.BookingService;

public class BookingHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookingService bookingService = new BookingService();
		
		String bookingId = null;
		
		if(request.getParameter("tripType").equals("round")) {			
			BookingDTO bookingDTO = BookingDTO.builder()
					.outboundFlightId(request.getParameter("outboundFlightId"))
					.returnFlightId(request.getParameter("returnFlightId"))
					.userNo("1")
					.promotionId("PROMO10")
					.bookingPw(null)
					.build();
			
			
			bookingId = bookingService.saveBookingToPending(bookingDTO);
			
			
		} else {
			BookingDTO dto = BookingDTO.builder()
					.outboundFlightId(request.getParameter("outboundFlightId"))
					.userNo("1")
					.promotionId("PROMO10")
					.bookingPw(null)
					.build();
			
			bookingId = bookingService.saveBookingToPending(dto);
			
		}
		
		request.setAttribute("bookingId", bookingId);
		
		return "/views/booking/booking.jsp";
	}

}
