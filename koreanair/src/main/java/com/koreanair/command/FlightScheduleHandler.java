package com.koreanair.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson; // Gson 라이브러리 import
import com.koreanair.model.dto.FlightScheduleDTO;
import com.koreanair.model.service.FlightScheduleService;

public class FlightScheduleHandler implements CommandHandler {

    private FlightScheduleService flightService = new FlightScheduleService();
    private Gson gson = new Gson();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
<<<<<<< HEAD
        System.out.println("=== FlightScheduleHandler 시작 ===");
        System.out.println("요청 URI: " + request.getRequestURI());
        System.out.println("요청 메소드: " + request.getMethod());
=======
        System.out.println("> FlightScheduleHandler called...");
>>>>>>> 1f3e8f056f7dad1b16f9666de5ce634c38e3b706
        
        // 요청 파라미터 받기
        String date = request.getParameter("date");
        String flightType = request.getParameter("flightType");
        
        // 기본값 설정
        if (date == null || date.trim().isEmpty()) {
            date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        if (flightType == null || flightType.trim().isEmpty()) {
<<<<<<< HEAD
            flightType = "international";
=======
            flightType = "all";
>>>>>>> 1f3e8f056f7dad1b16f9666de5ce634c38e3b706
        }
        
        System.out.println("요청 파라미터 - date: " + date + ", flightType: " + flightType);
        
        // 스케줄 데이터 조회
        List<FlightScheduleDTO> flightList = flightService.getFlightData(date, flightType);
        
        // 항상 JSON 응답 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String jsonResponse = gson.toJson(flightList);
        System.out.println("JSON 응답 데이터 크기: " + flightList.size());
<<<<<<< HEAD
        // System.out.println(flightList);
=======
        
>>>>>>> 1f3e8f056f7dad1b16f9666de5ce634c38e3b706
        response.getWriter().write(jsonResponse);
        return null; // JSP로 포워딩하지 않음
    }
}