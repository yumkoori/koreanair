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
		DashBoardStatsService dash = new DashBoardStatsService();
		
		
		List<DashBoardStatsDTO> dashload = dash.dashLoad();
		
		System.out.println(dashload);
		Map<String, Object> responseMap = new HashMap<>();
		 
		 if (dashload != null ) {
			 responseMap.put("status", "success");
			 responseMap.put("data", dashload);
		}
		 
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
