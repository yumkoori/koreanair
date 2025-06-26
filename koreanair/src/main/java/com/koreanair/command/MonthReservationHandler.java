package com.koreanair.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koreanair.model.dto.MonthReservationDTO;
import com.koreanair.model.service.MonthReservationService;

public class MonthReservationHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String year = request.getParameter("year");
		System.out.println(year);
		
		MonthReservationService reservation = new MonthReservationService();
		List<MonthReservationDTO> reservationList = reservation.monthreservation(year);
		
		Map<String, Object> responseMap = new HashMap<>();
		 
		 if (reservationList != null ) {
			 responseMap.put("status", "success");
			 responseMap.put("data", reservationList);
		}
		 
		 System.out.println("SeatRevenueHandler 에서 반환하는 List 값  " + reservationList);
		 
	     // json 으로 변환
		 Gson gson = new GsonBuilder().create();
		 String json = gson.toJson(responseMap);
		// 응답 설정
		 response.setContentType("application/json");
		 response.setCharacterEncoding("UTF-8");

	    // JSON 응답 출력
		 response.getWriter().write(json);
		return null;
	}

}
