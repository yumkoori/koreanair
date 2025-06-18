package com.koreanair.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
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
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.dto.SaveSchedulesDBDTO;
import com.koreanair.model.service.FlightSeatSaveService;
import com.koreanair.model.service.RefreshSchdulesService;

public class RefreshSchedulesHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("RefreshSchedulesHandler 도착 - 프론트엔드에서 보낸 데이터 확인");
		
		try {
			// 요청 메소드 확인
			String method = request.getMethod();
			System.out.println("요청 메소드: " + method);
			
			// Content-Type 확인
			String contentType = request.getContentType();
			System.out.println("Content-Type: " + contentType);
			
			// POST 요청인 경우 JSON 데이터 읽기
			if ("POST".equalsIgnoreCase(method)) {
				StringBuilder jsonData = new StringBuilder();
				String line;
				
				try (BufferedReader reader = request.getReader()) {
					while ((line = reader.readLine()) != null) {
						jsonData.append(line);
					}
				}
				
				String receivedJson = jsonData.toString();
				System.out.println("받은 JSON 데이터 길이: " + receivedJson.length());
<<<<<<< HEAD
				// System.out.println("받은 JSON 데이터: " + receivedJson);
=======
<<<<<<< HEAD
				// System.out.println("받은 JSON 데이터: " + receivedJson);
=======
				System.out.println("받은 JSON 데이터: " + receivedJson);
>>>>>>> 1f3e8f056f7dad1b16f9666de5ce634c38e3b706
>>>>>>> cd5ba6535013433d0eef20955581fa8717c00dbc
				
		        // JSON 구조를 올바르게 파싱 (SaveSchedulesDBHandler와 동일한 방식)
		        JsonObject root = JsonParser.parseString(receivedJson).getAsJsonObject();
		        JsonArray currentSchedulesJsonArray = root.getAsJsonArray("currentSchedules");

		        Gson gson = new Gson();
		        List<SaveSchedulesDBDTO> refresList = gson.fromJson(
		            currentSchedulesJsonArray,
		            new com.google.gson.reflect.TypeToken<List<SaveSchedulesDBDTO>>(){}.getType()
		        );
		        
		        // 더미 데이터 추가
		        SaveSchedulesDBDTO dummyData = new SaveSchedulesDBDTO();
		        dummyData.setFlightNo("Test001");
		        dummyData.setAirline("AC001");
		        dummyData.setOrigin("ICN");
		        dummyData.setDestination("PUS");
		        // LocalDateTime 필드가 있다면 현재 시간으로 설정 (실제 필드명에 맞게 수정 필요)
		         dummyData.setDepartureTime("05:13");
		         dummyData.setArrivalTime("05:13");
<<<<<<< HEAD
		        dummyData.setStatus("T160");
=======
<<<<<<< HEAD
		        dummyData.setStatus("T160");
=======
		        dummyData.setStatus("T1");
>>>>>>> 1f3e8f056f7dad1b16f9666de5ce634c38e3b706
>>>>>>> cd5ba6535013433d0eef20955581fa8717c00dbc
		        // 추가 필드가 있다면 설정
		         dummyData.setId("2025-06-16-all-Test001");
		        
		        refresList.add(dummyData);
		        System.out.println("더미 데이터가 추가되었습니다: " + dummyData.getFlightNo());
		        
		        RefreshSchdulesService refreshschdules = new RefreshSchdulesService();
		        
		        ProjectDao dao = new ProjectDaoimpl();

		        boolean refreshcheck = dao.refreshCheck(refresList);
		        
		        Map<String, Object> resultMap = new HashMap<>();

		        if (!refreshcheck) { // 중복이 없으면 저장
		            List<SaveSchedulesDBDTO> addedList = refreshschdules.refreshschdules(refresList);

		            resultMap.put("status", "success");
		            resultMap.put("message", "스케줄이 성공적으로 추가되었습니다.");
		            resultMap.put("newSchedules", addedList);

		        } else { // 중복이 있으면 저장 안함
		            resultMap.put("status", "fail");
		            resultMap.put("message", "새로 추가된 내용이 없습니다.");
		            resultMap.put("newSchedules", null);
		        }
				
				
	            String jsonResponse = gson.toJson(resultMap);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonResponse);
	            
	            return null;  // 바로 응답 종료

			}
			// POST가 아니면 null 반환(필요시 다른 처리 가능)
	        return null;

			
		} catch (Exception e) {
			System.err.println("RefreshSchedulesHandler 오류: " + e.getMessage());
			e.printStackTrace();
			
			// 오류 응답
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			StringBuilder errorJson = new StringBuilder();
			errorJson.append("{");
			errorJson.append("\"status\":\"error\",");
			errorJson.append("\"message\":\"스케줄 새로고침 중 오류가 발생했습니다: ");
			errorJson.append(e.getMessage().replace("\"", "\\\""));
			errorJson.append("\"");
			errorJson.append("}");
			
			response.getWriter().write(errorJson.toString());
			return null;
		}
	}
	

}
