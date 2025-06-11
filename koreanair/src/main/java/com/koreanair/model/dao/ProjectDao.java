package com.koreanair.model.dao;

import java.util.List;

import com.koreanair.model.dto.FlightSeatSaveDTO;

public interface ProjectDao {
	
	public String fetchFlightData(String apiUrl) throws Exception;
	
	public int seatsave(List<FlightSeatSaveDTO> seatList) throws Exception;
	
	public String flightSeatload(String abc) throws Exception;
}
