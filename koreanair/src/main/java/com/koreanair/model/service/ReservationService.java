package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.AdminAccountDTO;
import com.koreanair.model.dto.AdminReservationDTO;

public class ReservationService {
	
	public List<AdminReservationDTO> reservation ( String searchType, String searchKeyword, String status) throws Exception {
		ProjectDao dao = new ProjectDaoimpl();
		
		
		return dao.reservations(searchType, searchKeyword, status);
	}
}
