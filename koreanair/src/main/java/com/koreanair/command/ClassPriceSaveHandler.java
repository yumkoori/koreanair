package com.koreanair.command;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.ClassPriceSaveDTO;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.service.ClassPriceSaveService;
import com.koreanair.model.service.FlightSeatSaveService;

public class ClassPriceSaveHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String flightid = request.getParameter("flight_id");
		Map<String, Object> responseMap = new HashMap<>();
		
	    try {
	        // 1. request의 body에서 JSON 데이터 읽기
	        StringBuilder buffer = new StringBuilder();
	        BufferedReader reader = request.getReader();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            buffer.append(line);
	        }
	        String jsonData = buffer.toString();
	        
	        System.out.println("핸들러가 받은 JSON 데이터: " + jsonData);
	        
	        // 2. JSON → DTO 배열 → 리스트로 변환
	        Gson gson = new Gson();
	        ClassPriceSaveDTO[] priceArray = gson.fromJson(jsonData, ClassPriceSaveDTO[].class);
	        List<ClassPriceSaveDTO> priceList = Arrays.asList(priceArray);
	        
	        System.out.println("priceList에 들어가있는 값 >>   " + priceList);
	        // DTO에 flight_id 설정 (프론트엔드에서 오지 않을 수 있으므로)
			/*
			 * for (FlightSeatSaveDTO seat : seatList) { if (seat.getFlight_id() == null ||
			 * seat.getFlight_id().isEmpty()) { seat.setFlight_id("FL001"); // 기본값 설정 } }
			 */
	        
	        ClassPriceSaveService priceSave = new ClassPriceSaveService();
	        
	        int check = priceSave.priceSave(priceList , flightid);


	       
	        if(check>=1) {
	        responseMap.put("status", "success");
	        responseMap.put("message", flightid + "에 좌석정보들이 저장되었습니다");
	        }else {
	            responseMap.put("status", "fail");
	            responseMap.put("message", "문제가 발생했습니다");
	        }
	        
	    } catch (Exception e) {
	        System.out.println("FlightSeatSaveHandler 오류: " + e.getMessage());
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
