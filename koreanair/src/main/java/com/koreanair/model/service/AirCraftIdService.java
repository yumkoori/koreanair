package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.FlightSeatSaveDTO;

public class AirCraftIdService {
	
	public int searchaircraftid( String flight_id ) throws Exception{
	System.out.println(flight_id);
	ProjectDao dao = new ProjectDaoimpl();
	
	return dao.searchcarftid(flight_id);
	}
}
