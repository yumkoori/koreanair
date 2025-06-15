package com.koreanair.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		SearchFlightDTO dto = SearchFlightDTO.builder()
			.departure(request.getParameter("departure"))
			.arrival(request.getParameter("arrival"))
			.departureDate(request.getParameter("departureDate"))
			.returnDate(request.getParameter("returnDate"))
			.passengers(1)
			.seatClass(request.getParameter("seatClass"))
			.tripType(request.getParameter("tripType"))
			.build();
		
		List<SearchFlightResultDTO> flightList = searchService.searchFlight(dto);
		
		
		for (int i = 0; i < flightList.size(); i++) {
			String flightId = flightList.get(i).getFlightId();
			List<SeatAvailabilityDTO> seatList = seatService.getAvailabilitySeatsInfo(flightId);
			map.put(flightList.get(i), seatList);
			
			Map<String, Integer> seatPriceMap = seatService.getSeatsPriceByflightId(flightId);
			priceMap.put(flightId, seatPriceMap);
		}

		request.setAttribute("seatsPriceMap", priceMap);
		request.setAttribute("flightList", flightList);
		request.setAttribute("flightSeat", map);
		
		
		System.out.println(map);
		return "/views/search/search.jsp";
	}

}
