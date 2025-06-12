package com.koreanair.command;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.koreanair.model.service.AirportService;

public class AirportSearchHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	       String keyword = request.getParameter("keyword"); // 사용자가 입력한 검색어

	        AirportService service = new AirportService();
	        List<String> cityKorList = service.searchAirportCities(keyword);

	        // JSON 형식으로 응답
	        response.setContentType("application/json; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        Gson gson = new Gson(); // 구글 GSON 라이브러리 사용 추천
	        out.print(gson.toJson(cityKorList));
	        out.flush();

	        return null; // view 이동 없음 (AJAX 응답만 보냄)
	}

}
