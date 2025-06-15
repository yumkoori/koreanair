package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.FlightDAO;
import com.koreanair.model.dao.FlightDAOImpl;
import com.koreanair.model.dto.SearchFlightDTO;
import com.koreanair.model.dto.SearchFlightResultDTO;

public class FlightSearchService {
	FlightDAO dao = new FlightDAOImpl();
	
	
	public List<SearchFlightResultDTO> searchFlight(SearchFlightDTO searchFlightDTO) {
		
		return dao.getSearchFlight(searchFlightDTO);
		
	}
}
