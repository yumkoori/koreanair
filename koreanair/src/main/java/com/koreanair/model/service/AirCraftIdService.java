package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.FlightSeatSaveDTO;

public class AirCraftIdService {
	
	public int searchaircraftid( String craftid ) throws Exception{
	System.out.println(craftid);
	ProjectDao dao = new ProjectDaoimpl();
	
	return dao.searchcarftid(craftid);
	}
}
