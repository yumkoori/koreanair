package com.koreanair.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.koreanair.model.dto.FlightScheduleDTO;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.service.FlightScheduleService;
import com.koreanair.model.service.FlightSeatLoadService;

public class FlightSeatLoadHandler implements CommandHandler{
	
	
    private FlightSeatLoadService loadservice = new FlightSeatLoadService();
    private Gson gson = new Gson(); // Gson 객체는 재사용 가능하므로 멤버로 빼도 좋습니다.
    
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FlightSeatSaveDTO> seatload = loadservice.seatload();
		System.out.println("1번 도착");
		return null;
	}

}
