package com.koreanair.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.model.dto.BookingDTO;
import com.koreanair.model.service.BookingService;

public class BookingHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookingService bookingService = new BookingService();
		
		String bookingId = null;
		
		String text = request.getParameter("passengers");
		text = java.net.URLDecoder.decode(text, "UTF-8");

		Pattern pattern = java.util.regex.Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(text);

		int totalPassengers = 0;
		while (matcher.find()) {
		    totalPassengers += Integer.parseInt(matcher.group());
		}
		
		
		
		if(request.getParameter("tripType").equals("round")) {			
			BookingDTO bookingDTO = BookingDTO.builder()
					.outboundFlightId(request.getParameter("outboundFlightId"))
					.returnFlightId(request.getParameter("returnFlightId"))
					.userNo("1")
					.promotionId("PROMO10")
					.bookingPw(null)
					.build();
			
			
			bookingId = bookingService.saveBookingToPending(bookingDTO);
			
			
			String outboundSeatClass = request.getParameter("outboundSeatClass");
			String returnSeatClass = request.getParameter("returnSeatClass");
			
			System.out.println("이것은 1    " + bookingDTO);
			System.out.println("이것은 2    " + outboundSeatClass);
			
			bookingService.updateSeatStatusToPending(bookingDTO.getOutboundFlightId(), outboundSeatClass, totalPassengers);
			
			bookingService.updateSeatStatusToPending(bookingDTO.getReturnFlightId(), returnSeatClass, totalPassengers);

			
		} else {
			BookingDTO dto = BookingDTO.builder()
					.outboundFlightId(request.getParameter("outboundFlightId"))
					.userNo("1")
					.promotionId("PROMO10")
					.bookingPw(null)
					.build();
			
			
			bookingId = bookingService.saveBookingToPending(dto);
			
			String seatClass = request.getParameter("seatClass");
			
			bookingService.updateSeatStatusToPending(dto.getOutboundFlightId(), seatClass, totalPassengers);
	
		}
		
		request.setAttribute("bookingId", bookingId);
		
		return "/views/booking/booking.jsp";
	}

}
