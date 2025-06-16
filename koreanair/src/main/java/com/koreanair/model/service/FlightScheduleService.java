package com.koreanair.model.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

// 스크린샷에 보이는 정확한 패키지 경로로 DAO와 DTO 클래스를 import 합니다.
import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.FlightScheduleDTO;


public class FlightScheduleService {

    // DAO 인터페이스와 구현 클래스를 스크린샷의 이름에 맞게 사용합니다.
    private final ProjectDao projectDao;
    
    private final String serviceKey = "UNnuFixx2cWRxnujKddwl8pYBr1uw946cRcT6JayP4%2B5uvZqT0FnuZFWETlNz8N7%2BeSga0fya9NJzMv%2BUVm7wg%3D%3D";

    public FlightScheduleService() {
        this.projectDao = new ProjectDaoimpl(); // 클래스 이름 'ProjectDaoimpl' 사용
    }

    /**
     * 핸들러로부터 요청을 받아 항공편 데이터를 DTO 리스트로 반환하는 메인 메소드
     */
                                                    //처음에는  오늘날짜             all     
    public List<FlightScheduleDTO> getFlightData(String requestedDate, String flightType) throws Exception {
    	System.out.println("서비스에 도착");
    	
        if (flightType == null || flightType.trim().isEmpty()) {
            flightType = "all";
        }
        if ("realtime".equalsIgnoreCase(flightType)) {
            requestedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            System.out.println("8888");
        }
        if (requestedDate == null || requestedDate.trim().isEmpty()) {
            throw new IllegalArgumentException("날짜 파라미터가 필요합니다.");
        }

        String apiUrlToCall = buildApiUrl(flightType, requestedDate);
        // DAO 호출
        String xmlResponse = projectDao.fetchFlightData(apiUrlToCall);
        // DTO 클래스 이름을 FlightScheduleDTO로 수정
        return parseAndMapToDtoList(xmlResponse, flightType, requestedDate);
    }

    /**
     * flightType과 날짜에 따라 적절한 API URL을 생성합니다.
     */
    private String buildApiUrl(String flightType, String date) throws Exception {
        String schDateForApi = date.replace("-", "");
        String baseUrl;
        
        if ("domestic".equalsIgnoreCase(flightType)) {
            baseUrl = "http://openapi.airport.co.kr/service/rest/FlightScheduleList/getDflightScheduleList";
        } else if ("international".equalsIgnoreCase(flightType)) {
            baseUrl = "http://openapi.airport.co.kr/service/rest/FlightScheduleList/getIflightScheduleList";
        } else { // "all" 또는 "realtime"
            baseUrl = "http://openapi.airport.co.kr/service/rest/FlightStatusList/getFlightStatusList";
        }
        
        return baseUrl + "?serviceKey=" + serviceKey +
               "&schDate=" + URLEncoder.encode(schDateForApi, StandardCharsets.UTF_8) +
               "&numOfRows=200&pageNo=1";
    }

    /**
     * DAO가 가져온 XML 문자열을 파싱하고 가공하여 최종 DTO 리스트로 만듭니다.
     */
    // 반환 타입과 리스트 타입을 FlightScheduleDTO로 수정
    private List<FlightScheduleDTO> parseAndMapToDtoList(String xmlResponse, String flightType, String requestedDate) throws Exception {
        List<FlightScheduleDTO> flightList = new ArrayList<>();
        JSONObject xmlJSONObj = XML.toJSONObject(xmlResponse);

        JSONObject responseNode = xmlJSONObj.optJSONObject("response");
        if (responseNode == null) {
            throw new Exception("API 응답 형식이 올바르지 않습니다 (response 노드 없음).");
        }

        JSONObject headerNode = responseNode.optJSONObject("header");
        if (headerNode != null && !"00".equals(headerNode.optString("resultCode"))) {
            if (!headerNode.optString("resultMsg","").toUpperCase().contains("NORMAL")) {
                 throw new Exception("API 오류: " + headerNode.optString("resultMsg", "알 수 없는 오류"));
            }
        }
        
        JSONObject bodyNode = responseNode.optJSONObject("body");
        if (bodyNode == null || bodyNode.optInt("totalCount", -1) == 0) {
            return flightList;
        }

        JSONArray itemsArrayFromApi = new JSONArray();
        if (bodyNode.has("items")) {
            Object itemsObj = bodyNode.getJSONObject("items").opt("item");
            if (itemsObj instanceof JSONArray) {
                itemsArrayFromApi = (JSONArray) itemsObj;
            } else if (itemsObj instanceof JSONObject) {
                itemsArrayFromApi.put(itemsObj);
            }
        }

        for (int i = 0; i < itemsArrayFromApi.length(); i++) {
            JSONObject apiFlight = itemsArrayFromApi.getJSONObject(i);
            // DTO 객체 생성 부분을 FlightScheduleDTO로 수정
            FlightScheduleDTO flightDto = new FlightScheduleDTO(); 
            
            String flightNoVal, airlineVal, originVal, destVal, depTimeVal, arrTimeVal, statusVal;

            if ("domestic".equalsIgnoreCase(flightType)) {
                flightNoVal = apiFlight.optString("domesticNum", "N/A");
                airlineVal = apiFlight.optString("airlineKorean", "N/A");
                originVal = apiFlight.optString("startcity", "N/A");
                destVal = apiFlight.optString("arrivalcity", "N/A");
                depTimeVal = formatTime(apiFlight.optString("domesticStartTime", ""));
                arrTimeVal = formatTime(apiFlight.optString("domesticArrivalTime", ""));
                statusVal = "예정";
            } else if ("international".equalsIgnoreCase(flightType)) {
                flightNoVal = apiFlight.optString("internationalNum", "N/A");
                airlineVal = apiFlight.optString("airlineKorean", "N/A");
                if("OUT".equalsIgnoreCase(apiFlight.optString("internationalIoType", ""))) {
                    originVal = apiFlight.optString("city", "N/A");
                    destVal = apiFlight.optString("airport", "N/A");
                    depTimeVal = formatTime(apiFlight.optString("internationalTime", ""));
                    arrTimeVal = "N/A";
                } else {
                    originVal = apiFlight.optString("airport", "N/A");
                    destVal = apiFlight.optString("city", "N/A");
                    arrTimeVal = formatTime(apiFlight.optString("internationalTime", ""));
                    depTimeVal = "N/A";
                }
                statusVal = "예정";
            } else { // "all", "realtime"
                flightNoVal = apiFlight.optString("airFln", "N/A");
                airlineVal = apiFlight.optString("airlineKorean", "N/A");
                originVal = apiFlight.optString("boardingKor", "N/A");
                destVal = apiFlight.optString("arrivedKor", "N/A");
                statusVal = apiFlight.optString("rmkKor", "N/A");
                String timeApi = apiFlight.optString("etd", apiFlight.optString("std", ""));
                String formattedTime = formatTime(timeApi);
                if ("O".equalsIgnoreCase(apiFlight.optString("io", ""))) {
                    depTimeVal = formattedTime;
                    arrTimeVal = "N/A";
                } else {
                    arrTimeVal = formattedTime;
                    depTimeVal = "N/A";
                }
            }

            // DTO 객체에 값을 설정합니다. (FlightScheduleDTO에 setter가 있어야 합니다)
            flightDto.setId(requestedDate + "-" + flightType + "-FL" + flightNoVal.replaceAll("[^a-zA-Z0-9]", "") + "_" + i);
            flightDto.setFlightNo(flightNoVal);
            flightDto.setAirline(airlineVal);
            flightDto.setOrigin(originVal);
            flightDto.setDestination(destVal);
            flightDto.setDepartureTime(depTimeVal);
            flightDto.setArrivalTime(arrTimeVal);
            flightDto.setStatus(statusVal);

            flightList.add(flightDto);
        }

        return flightList;
    }
    
    /**
     * 시간 문자열을 포맷팅하는 헬퍼 메소드
     */
    private String formatTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty() || timeStr.length() < 4) return "N/A";
        
        if (timeStr.length() == 4) {
            return timeStr.substring(0, 2) + ":" + timeStr.substring(2, 4);
        } else if (timeStr.length() >= 12 && timeStr.matches("\\d{12,}")) {
            return timeStr.substring(8, 10) + ":" + timeStr.substring(10, 12);
        }
        return timeStr;
    }
}