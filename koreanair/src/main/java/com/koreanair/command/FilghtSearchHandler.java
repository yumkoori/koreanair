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
		System.out.println("process 호출됨.");
		
		FlightSearchService searchService = new FlightSearchService();
		FlightSeatService seatService = new FlightSeatService();
		
		Map<SearchFlightResultDTO, List<SeatAvailabilityDTO>> map = new HashMap<SearchFlightResultDTO, List<SeatAvailabilityDTO>>();
		
		
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
		
	
		
		
		
		System.out.println(flightList.size());
		
		for (int i = 0; i < flightList.size(); i++) {
			System.out.println(flightList.get(i).getFlightId());
			List<SeatAvailabilityDTO> seatList = seatService.getAvailabilitySeatsInfo(flightList.get(i).getFlightId());
			map.put(flightList.get(i), seatList);
			
		}
		
		System.out.println(map);
		
		request.setAttribute("flightList", flightList);
		request.setAttribute("flightSeat", map);
		
		return "/views/search/search.jsp";
	}

}
