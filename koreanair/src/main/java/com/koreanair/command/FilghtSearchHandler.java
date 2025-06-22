package com.koreanair.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.model.dto.SearchFlightDTO;
import com.koreanair.model.dto.SearchFlightResultDTO;
import com.koreanair.model.dto.SeatAvailabilityDTO;
import com.koreanair.model.service.FlightSearchService;
import com.koreanair.model.service.FlightSeatService;

public class FilghtSearchHandler implements CommandHandler{

	
	
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		FlightSearchService searchService = new FlightSearchService();
		FlightSeatService seatService = new FlightSeatService();
		
		Map<SearchFlightResultDTO, List<SeatAvailabilityDTO>> map = new HashMap<SearchFlightResultDTO, List<SeatAvailabilityDTO>>();
		Map<String, Map<String,Integer>> priceMap = new HashMap<String, Map<String,Integer>>();
		
		//passensers 인원 수 파악 
		String text = request.getParameter("passengers");
		text = java.net.URLDecoder.decode(text, "UTF-8");

		Pattern pattern = java.util.regex.Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(text);

		int totalPassengers = 0;
		while (matcher.find()) {
		    totalPassengers += Integer.parseInt(matcher.group());
		}
		
		
		SearchFlightDTO dto = SearchFlightDTO.builder()
			.departure(request.getParameter("departure"))
			.arrival(request.getParameter("arrival"))
			.departureDate(request.getParameter("departureDate"))
			.returnDate(request.getParameter("returnDate"))
			.passengers(totalPassengers)
			.seatClass(request.getParameter("seatClass"))
			.tripType(request.getParameter("tripType"))
			.build();
		
		
		seatService.releaseExpiredPendingSeats();
		
		List<SearchFlightResultDTO> flightList = searchService.searchFlight(dto);
		
		
		for (int i = 0; i < flightList.size(); i++) {
			String flightId = flightList.get(i).getFlightId();
			List<SeatAvailabilityDTO> seatList = seatService.getAvailabilitySeatsInfo(flightId, dto.getPassengers());
			map.put(flightList.get(i), seatList);
			
			Map<String, Integer> seatPriceMap = seatService.getSeatsPriceByflightId(flightId);
			priceMap.put(flightId, seatPriceMap);
		}

		request.setAttribute("seatsPriceMap", priceMap);
		request.setAttribute("flightList", flightList);
		request.setAttribute("flightSeat", map);
		request.setAttribute("weekLowPrices", searchService.getWeekLowPrice(dto));
		
		System.out.print("flightList");
		System.out.println(flightList);
		
		System.out.print("flightSeat");
		System.out.println(map);
		System.out.print("seatPriceMap: ");
		System.out.println(priceMap);
		
		System.out.println(searchService.getWeekLowPrice(dto));
		return "/views/search/search.jsp";
	}

}
