package com.koreanair.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koreanair.model.dto.FlightScheduleDTO;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.service.FlightScheduleService;
import com.koreanair.model.service.FlightSeatLoadService;

public class FlightSeatLoadHandler implements CommandHandler{
	
	
    private FlightSeatLoadService loadservice = new FlightSeatLoadService();
    private Gson gson = new Gson(); // Gson 객체는 재사용 가능하므로 멤버로 빼도 좋습니다.
    
    
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("1번 도착");
		String flight_id = request.getParameter("flight_id"); 
		List<FlightSeatSaveDTO> seatload = loadservice.seatload(flight_id);
		
		 // JSON 변환
	    Gson gson = new GsonBuilder().create();
	    String json = gson.toJson(seatload);

	    // 응답 설정
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    // JSON 응답 출력
	    response.getWriter().write(json);

	    // 컨트롤러에서 직접 응답했으므로 null 반환
	    return null;
	}

}
