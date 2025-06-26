package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.SeatRevenueDTO;

public class SeatRevenueService {
	
	public List<SeatRevenueDTO> revenueList() throws Exception{
		
		ProjectDao dao = new ProjectDaoimpl();
		return dao.seatRevenue();
	}
}
