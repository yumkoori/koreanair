package com.koreanair.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koreanair.model.dto.AdminReservationDTO;
import com.koreanair.model.service.ReservationService;

public class ReservationsHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String searchType = request.getParameter("searchType");
		String searchKeyword = request.getParameter("searchKeyword");
		String status = request.getParameter("status");
		
		ReservationService reservations = new ReservationService();
		List<AdminReservationDTO> reservationList = reservations.reservation(searchType, searchKeyword, status);
		
		Map<String, Object> responseMap = new HashMap<>();
		 
		 if (reservationList != null ) {
			 responseMap.put("status", "success");
			 responseMap.put("data", reservationList);
			 System.out.println("응답 상태: success");
		} else {
			 responseMap.put("status", "error");
			 responseMap.put("message", "데이터를 불러올 수 없습니다");
			 System.out.println("응답 상태: error");
		 }
		 
	     // json 으로 변환
		 Gson gson = new GsonBuilder().create();
		 String json = gson.toJson(responseMap);
		 
		 System.out.println("최종 JSON 응답: " + json);
		 
		// 응답 설정
		 response.setContentType("application/json");
		 response.setCharacterEncoding("UTF-8");

	    // JSON 응답 출력
		 response.getWriter().write(json);
		
		System.out.println("=== ReservationsHandler 완료 ===");
		return null;
	
	}

}
