package com.koreanair.command;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.FlightScheduleDTO;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.dto.SaveSchedulesDBDTO;
import com.koreanair.model.service.FlightSeatSaveService;
import com.koreanair.model.service.SaveSchedulesDBService;

public class SaveSchedulesDBHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    Map<String, Object> responseMap = new HashMap<>();
	    System.out.println("> SaveSchedulesDBHandler for Initial Page Load with Data called...");
	    
	    try {
	        // 1. request의 body에서 JSON 데이터 읽기
	        StringBuilder buffer = new StringBuilder();
	        BufferedReader reader = request.getReader();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            buffer.append(line);
	        }
	        String jsonData = buffer.toString();
	        
	        // System.out.println("핸들러가 받은 JSON 데이터: " + jsonData);
	        
	        // 2. JSON → DTO 배열 → 리스트로 변환
	        
	        JsonObject root = JsonParser.parseString(jsonData).getAsJsonObject();
	        JsonArray schedulesJsonArray = root.getAsJsonArray("schedules");

	        Gson gson = new Gson();
	        List<SaveSchedulesDBDTO> scheduleList = gson.fromJson(
	            schedulesJsonArray,
	            new com.google.gson.reflect.TypeToken<List<SaveSchedulesDBDTO>>(){}.getType()
	        );
			/*
			 * Gson gson = new Gson(); SaveSchedulesDBDTO[] schedulesArray =
			 * gson.fromJson(jsonData, SaveSchedulesDBDTO[].class); List<SaveSchedulesDBDTO>
			 * scheduleList = Arrays.asList(schedulesArray);
			 */
	        
	        
	        SaveSchedulesDBService saveSchedules = new SaveSchedulesDBService();
	        
	        int checkdb = saveSchedules.saveSchdulesDB(scheduleList);
	        if (checkdb >= 1) {
	        	responseMap.put("status", "success");
			}
	        ProjectDao dao = new ProjectDaoimpl();

	        // int checkdb = dao.saveSchdulesDB(scheduleList);

	        
	    } catch (Exception e) {
	        System.out.println("SaveSchedulesDBHandler 오류: " + e.getMessage());
	        e.printStackTrace();
	        
	        responseMap.put("status", "error");
	        responseMap.put("message", "좌석 저장 중 오류가 발생했습니다: " + e.getMessage());
	    }

	    // 공통 응답 처리
	    Gson gson = new Gson();
	    String jsonResponse = gson.toJson(responseMap);
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(jsonResponse);
	    
	    System.out.println("응답: " + jsonResponse);

	    return null;
	}
}

