package com.koreanair.model.service;

import java.util.List;
import java.util.Map;

import com.koreanair.model.dao.FlightDAO;
import com.koreanair.model.dao.FlightDAOImpl;
import com.koreanair.model.dto.SeatAvailabilityDTO;

public class FlightSeatService {
	
	FlightDAO dao = new FlightDAOImpl();
	
	public List<SeatAvailabilityDTO> getAvailabilitySeatsInfo(String flightId) {
		
		return dao.getReservedSeats(flightId);
	}
	
	public Map<String, Integer> getSeatsPriceByflightId(String flightId) {
		return dao.getSeatsPrice(flightId);
		
	}
	
	
}
