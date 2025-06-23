package com.koreanair.model.service;

import java.util.List;
import java.util.Map;

import com.koreanair.model.dao.FlightDAO;
import com.koreanair.model.dao.FlightDAOImpl;
import com.koreanair.model.dto.SeatAvailabilityDTO;

public class FlightSeatService {
	
	FlightDAO dao = new FlightDAOImpl();
	
	public List<SeatAvailabilityDTO> getAvailabilitySeatsInfo(String flightId, int passengers) {
		
		return dao.getReservedSeats(flightId, passengers);
	}
	
	public Map<String, Integer> getSeatsPriceByflightId(String flightId) {
		return dao.getSeatsPrice(flightId);
		
	}
	
	public void releaseExpiredPendingSeats() {
		
		System.out.println("좌석 상태 초기화 ........");
		
		dao.releaseExpiredPendingSeats();
	}
}
