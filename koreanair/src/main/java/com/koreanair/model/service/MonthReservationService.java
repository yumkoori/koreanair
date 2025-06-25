package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.MonthReservationDTO;

public class MonthReservationService {
	
	public List<MonthReservationDTO> monthreservation (String year) throws Exception{
		ProjectDao dao = new ProjectDaoimpl();
		return dao.monthReservation(year);
	}
}
