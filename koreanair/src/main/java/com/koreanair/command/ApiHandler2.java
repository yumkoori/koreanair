package com.koreanair.command;

import java.util.List;

import com.koreanair.model.dao.ApiDAO;
import com.koreanair.model.dto.AirportDTO;
import com.koreanair.model.service.ApiService;

public class ApiHandler2 {

	public static void main(String[] args) {

		ApiService apiservice = new ApiService();
		ApiDAO dao = new ApiDAO();
		
		List<AirportDTO> list = apiservice.fetchAirportList();
		
		System.out.println(list);
		dao.saveAirport(list);
		
		
	}

}
