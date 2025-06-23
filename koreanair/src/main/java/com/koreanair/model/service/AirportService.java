package com.koreanair.model.service;

import java.util.List;
import com.koreanair.model.dao.AirportDAO;
import com.koreanair.model.dto.AirportDTO;

public class AirportService {

    private AirportDAO dao = new AirportDAO();

	public List<AirportDTO> searchAirportsByKeyword(String keyword) {
        return dao.findAirportsByKeyword(keyword);
    }

	
}


