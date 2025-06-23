package com.koreanair.command;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.koreanair.model.dto.AirportDTO;
import com.koreanair.model.service.AirportService;

public class AutoCompleteHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        AirportService service = new AirportService();
        List<AirportDTO> resultList = service.searchAirportsByKeyword(keyword);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(resultList));
        out.flush();
        return null; // AJAX 응답이므로 JSP 페이지 이동 없음
    }
	}


