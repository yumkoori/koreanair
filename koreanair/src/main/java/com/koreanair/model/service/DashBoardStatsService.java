package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.DashBoardStatsDTO;

public class DashBoardStatsService {
	
	public List<DashBoardStatsDTO> dashLoad() throws Exception{
		ProjectDao dao = new ProjectDaoimpl();
		
		return dao.dashLoad();
	}
}
