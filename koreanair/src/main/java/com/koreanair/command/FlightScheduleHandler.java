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
    private Gson gson = new Gson(); // Gson 객체는 재사용 가능하므로 멤버로 빼도 좋습니다.

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("> FlightScheduleHandler for Initial Page Load with Data called...");

        // 1. 페이지 첫 로딩 시 보여줄 기본 데이터 조회 (오늘 날짜, 전체)
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String flightType = "all";
        
        List<FlightScheduleDTO> flightList = flightService.getFlightData(date, flightType);

        // 2. 조회된 DTO 리스트를 JSON 문자열로 변환
        String initialDataJson = gson.toJson(flightList);

        // 3. View(JSP)에 'JSON 문자열'을 전달
        request.setAttribute("initialData", initialDataJson);
        
        // 4. JSP 페이지로 포워딩
        return "/views/adminpage/index5.jsp";
    }
}