package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.SaveSchedulesDBDTO;

public class SaveSchedulesDBService {
	
	public int saveSchdulesDB( List<SaveSchedulesDBDTO> scheduleList) throws Exception{
		// System.out.println("서비스에서 받고 있습니다>>>>>>>" + scheduleList);
		ProjectDao dao = new ProjectDaoimpl();
		return dao.saveSchdulesDB(scheduleList);
	}
	
}
