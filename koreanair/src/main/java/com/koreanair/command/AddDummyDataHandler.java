package com.koreanair.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.koreanair.model.dto.SaveSchedulesDBDTO;

public class AddDummyDataHandler implements CommandHandler {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("AddDummyDataHandler 도착 - 더미데이터 추가 요청 처리");
        
        try {
            // 요청 파라미터 받기
            String date = request.getParameter("date");
            String filterType = request.getParameter("filterType");
            
            // 기본값 설정
            if (date == null || date.trim().isEmpty()) {
                date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            if (filterType == null || filterType.trim().isEmpty()) {
                filterType = "all";
            }
            
            System.out.println("더미데이터 생성 파라미터 - date: " + date + ", filterType: " + filterType);
            
            // 더미 데이터 생성
            List<SaveSchedulesDBDTO> dummyData = generateDummySchedules(date, filterType);
            
            // 응답 데이터 준비
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("status", "success");
            resultMap.put("message", dummyData.size() + "개의 더미 스케줄이 추가되었습니다.");
            resultMap.put("newSchedules", dummyData);
            resultMap.put("date", date);
            resultMap.put("filterType", filterType);
            
            // JSON 응답 반환
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(resultMap);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);
            
            System.out.println("더미데이터 " + dummyData.size() + "개 추가 완료");
            return null; // 바로 응답 종료
            
        } catch (Exception e) {
            System.err.println("AddDummyDataHandler 오류: " + e.getMessage());
            e.printStackTrace();
            
            // 오류 응답
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "더미데이터 생성 중 오류가 발생했습니다: " + e.getMessage());
            
            Gson gson = new Gson();
            String errorResponse = gson.toJson(errorMap);
            response.getWriter().write(errorResponse);
            
            return null;
        }
    }
    
    /**
     * 더미 스케줄 데이터 생성 메서드
     */
    private List<SaveSchedulesDBDTO> generateDummySchedules(String date, String filterType) {
        List<SaveSchedulesDBDTO> dummyList = new ArrayList<>();
        Random random = new Random();
        
        // 더미 항공편 정보 배열
        String[] airlines = {"대한항공", "아시아나항공", "진에어", "제주항공", "티웨이항공", "에어부산", "이스타항공"};
        String[] domesticOrigins = {"서울(인천)", "서울(김포)", "부산", "제주", "대구", "광주", "울산"};
        String[] domesticDestinations = {"부산", "제주", "대구", "광주", "울산", "여수", "포항"};
        String[] internationalOrigins = {"서울(인천)", "서울(김포)", "부산"};
        String[] internationalDestinations = {"도쿄", "오사카", "방콕", "싱가포르", "홍콩", "상하이", "베이징", "마닐라", "호치민"};
        String[] statuses = {"예정", "지연", "탑승중", "출발", "도착"};
        
        // 필터 타입에 따라 다른 데이터 생성
        String[] origins, destinations;
        String flightPrefix;
        
        if ("realtime".equals(filterType)) {
            // 실시간 데이터는 국제선 위주로
            origins = internationalOrigins;
            destinations = internationalDestinations;
            flightPrefix = "RT"; // RealTime 접두사
        } else {
            // 전체 또는 기본은 국내선과 국제선 혼합
            if (random.nextBoolean()) {
                origins = domesticOrigins;
                destinations = domesticDestinations;
                flightPrefix = "DM"; // Domestic 접두사
            } else {
                origins = internationalOrigins;
                destinations = internationalDestinations;
                flightPrefix = "IN"; // International 접두사
            }
        }
        
        // 3-7개의 더미 데이터 생성
        int dummyCount = 3 + random.nextInt(5); // 3~7개
        
        for (int i = 0; i < dummyCount; i++) {
            SaveSchedulesDBDTO dummy = new SaveSchedulesDBDTO();
            
            // 고유한 항공편 번호 생성
            String flightNo = flightPrefix + (1000 + random.nextInt(9000));
            
            // 현재 시간을 기준으로 랜덤 시간 생성
            int depHour = 6 + random.nextInt(18); // 06:00 ~ 23:59
            int depMin = random.nextInt(60);
            int arrHour = (depHour + 1 + random.nextInt(8)) % 24; // 출발시간 + 1~8시간
            int arrMin = random.nextInt(60);
            
            // 출발지와 도착지는 다르게 설정
            String origin = origins[random.nextInt(origins.length)];
            String destination;
            do {
                destination = destinations[random.nextInt(destinations.length)];
            } while (origin.equals(destination));
            
            // ID 생성 (날짜-필터타입-항공편번호_인덱스)
            dummy.setId(date + "-" + filterType + "-FL" + flightNo + "_" + i);
            dummy.setFlightNo(flightNo);
            dummy.setAirline(airlines[random.nextInt(airlines.length)]);
            dummy.setOrigin(origin);
            dummy.setDestination(destination);
            dummy.setDepartureTime(String.format("%02d:%02d", depHour, depMin));
            dummy.setArrivalTime(String.format("%02d:%02d", arrHour, arrMin));
            dummy.setStatus(statuses[random.nextInt(statuses.length)]);
            
            dummyList.add(dummy);
        }
        
        System.out.println("더미 데이터 " + dummyCount + "개 생성됨 (날짜: " + date + ", 타입: " + filterType + "):");
        for (SaveSchedulesDBDTO dummy : dummyList) {
            System.out.println(" - " + dummy.getFlightNo() + ": " + dummy.getOrigin() + " → " + dummy.getDestination() + " (" + dummy.getStatus() + ")");
        }
        
        return dummyList;
    }
} 