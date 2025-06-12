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
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.service.FlightSeatSaveService;

public class FlightSeatSaveHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("craftid");
		System.out.println("저장하려고 받아온 값> " + id);
		Map<String, Object> responseMap = new HashMap();
		System.out.println("> FlightSeatSaveHandler for Initial Page Load with Data called...");
		
		// 1. request의 body에서 JSON 데이터 읽기
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String jsonData = buffer.toString();
        
        System.out.println("핸들러가 받은 JSON 데이터: " + jsonData);
        
         // 2. JSON 문자열을 자바 DTO 객체 리스트로 변환
        Gson gson = new Gson();
        FlightSeatSaveDTO[] seatArray = gson.fromJson(jsonData, FlightSeatSaveDTO[].class);
        System.out.println("seatArray" + seatArray);
        List<FlightSeatSaveDTO> seatList = Arrays.asList(seatArray);
        System.out.println("seatList" + seatList);
        FlightSeatSaveService saveService = new FlightSeatSaveService();
        
        int check = saveService.seatSave(seatList , id);
        
        // 4. 성공했을 경우, 보낼 응답 내용을 Map에 담기
        responseMap.put("status", "success");
        responseMap.put("message", check + "개의 좌석 정보가 성공적으로 저장되었습니다.");
        responseMap.put("savedCount", check);
        
        // 6. Map을 JSON 문자열로 변환
        String jsonResponse = gson.toJson(responseMap);

        // 7. 클라이언트에 응답 보내기
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        
        System.out.println(check);
        
        
		return null;
	}

}
