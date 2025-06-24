package com.koreanair.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koreanair.model.dto.PopularDTO;
import com.koreanair.model.service.DashBoardStatsService;
import com.koreanair.model.service.PopularService;

public class PopularHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("PopularHandler 시작 ");
		String year = request.getParameter("year");
		PopularService popular = new PopularService();
		List<PopularDTO> popularList = popular.popularlist(year);
		
		
		Map<String, Object> responseMap = new HashMap<>();
		
		 if (popularList != null ) {
			 responseMap.put("status", "success");
			 responseMap.put("data", popularList);
		}
		 
		 System.out.println("SeatRevenueHandler 에서 반환하는 List 값  " + popularList);
		 
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
