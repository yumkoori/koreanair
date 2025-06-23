package com.koreanair.model.service;

import com.koreanair.model.dao.PassengerDAO;
import com.koreanair.model.dto.PassengerDTO;

public class PassengerService {
	

	private final PassengerDAO dao = new PassengerDAO();
	
	public void savePassenger(PassengerDTO dto) {
		dao.savePassenger(dto);
	}
	
	
	
}

