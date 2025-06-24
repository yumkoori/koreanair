package com.koreanair.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koreanair.model.dto.DashBoardStatsDTO;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.service.DashBoardStatsService;

public class DashBoardStatsHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("=== DashBoardStatsHandler 시작 ===");
		System.out.println("요청 시간: " + new java.util.Date());
		
		DashBoardStatsService dash = new DashBoardStatsService();
		
		List<DashBoardStatsDTO> dashload = dash.dashLoad();
		
		System.out.println("DAO에서 반환된 데이터 개수: " + (dashload != null ? dashload.size() : "null"));
		if (dashload != null && dashload.size() > 0) {
			DashBoardStatsDTO dto = dashload.get(0);
			System.out.println("첫 번째 데이터:");
			System.out.println("- 총 사용자 수: " + dto.getTotalCount());
			System.out.println("- 남성 수: " + dto.getMaleCount());
			System.out.println("- 여성 수: " + dto.getFmaleCount());
			System.out.println("- 예약 수: " + dto.getReservations());
		}
		
		Map<String, Object> responseMap = new HashMap<>();
		 
		 if (dashload != null ) {
			 responseMap.put("status", "success");
			 responseMap.put("data", dashload);
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
		
		System.out.println("=== DashBoardStatsHandler 완료 ===");
		return null;
	}

}
