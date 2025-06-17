package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.model.dto.BookingDTO;
import com.koreanair.model.service.BookingService;

public class BookingHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookingService bookingService = new BookingService();
		
		if(request.getParameter("tripType").equals("round")) {
			BookingDTO outDTO = BookingDTO.builder()
					.flightId(request.getParameter("outboundFlightId"))
					.userNo("1")
					.promotionId("PROMO10")
					.bookingPw(null)
					.build();
			
			BookingDTO returnDTO = BookingDTO.builder()
					.flightId(request.getParameter("returnFlightId"))
					.userNo("1")
					.promotionId("PROMO10")
					.bookingPw(null)
					.build();
			
			bookingService.saveBookingToPending(outDTO);
			bookingService.saveBookingToPending(returnDTO);
			
		} else {
			BookingDTO dto = BookingDTO.builder()
					.flightId(request.getParameter("flightId"))
					.userNo("1")
					.promotionId("PROMO10")
					.bookingPw(null)
					.build();
			
			bookingService.saveBookingToPending(dto);
		}
		return "/views/booking/booking.jsp";
	}

}
