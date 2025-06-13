package com.koreanair.command;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.service.FlightSeatSaveService;

public class FlightSeatSaveHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String id = request.getParameter("flight_id");
	    System.out.println("저장하려고 받아온 값> " + id);
	    Map<String, Object> responseMap = new HashMap<>();
	    System.out.println("> FlightSeatSaveHandler for Initial Page Load with Data called...");
	    
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
	        FlightSeatSaveDTO[] seatArray = gson.fromJson(jsonData, FlightSeatSaveDTO[].class);
	        List<FlightSeatSaveDTO> seatList = Arrays.asList(seatArray);
	        
	        // DTO에 flight_id 설정 (프론트엔드에서 오지 않을 수 있으므로)
	        for (FlightSeatSaveDTO seat : seatList) {
	            if (seat.getFlight_id() == null || seat.getFlight_id().isEmpty()) {
	                seat.setFlight_id("FL001"); // 기본값 설정
	            }
	        }
	        
	        FlightSeatSaveService saveService = new FlightSeatSaveService();
	        ProjectDao dao = new ProjectDaoimpl();

	        boolean hasDuplicate = dao.checkDuplicateSeat(seatList);

	        if (!hasDuplicate) { // 중복이 없으면 저장
	            int check = saveService.seatSave(seatList , id);
	            responseMap.put("status", "success");
	            responseMap.put("message", check + "개의 좌석 정보가 성공적으로 저장되었습니다.");
	            responseMap.put("savedCount", check);
	        } else { // 중복이 있으면 저장 안함
	            responseMap.put("status", "fail");
	            responseMap.put("message", "이미 등록된 좌석이 있습니다. 다른 좌석을 선택해주세요.");
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
