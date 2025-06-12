package com.koreanair.model.dao;

import java.util.List;

import com.koreanair.model.dto.FlightSeatSaveDTO;

public interface ProjectDao {
	
	public String fetchFlightData(String apiUrl) throws Exception;
	
	public int seatsave(List<FlightSeatSaveDTO> seatList , String id) throws Exception;
	
	public List<FlightSeatSaveDTO> flightSeatload(String planeType) throws Exception;
	
	public int searchcarftid( String craftid) throws Exception;
}
