package com.koreanair.model.service;

import java.io.BufferedReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.FlightSeatSaveDTO;

public class FlightSeatSaveService {
	
	public int seatSave(List<FlightSeatSaveDTO> seatList , String id) throws Exception {
		System.out.println("seatList 서비스 > " + seatList);
		ProjectDao dao = new ProjectDaoimpl();
		
		return dao.seatsave(seatList, id);
	}
}
