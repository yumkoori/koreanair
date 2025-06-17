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
    	// System.out.println("서비스에 도착");
    	
        if (flightType == null || flightType.trim().isEmpty()) {
            flightType = "all";
            
        }
        if ("realtime".equalsIgnoreCase(flightType)) {
            requestedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            // System.out.println("8888");
        }
        if (requestedDate == null || requestedDate.trim().isEmpty()) {
            throw new IllegalArgumentException("날짜 파라미터가 필요합니다.");
        }

        // 모든 페이지의 데이터를 가져오기 위한 동적 페이징 처리
        return fetchAllFlightData(flightType, requestedDate);
    }

    /**
     * 모든 페이지의 데이터를 동적으로 가져오는 메소드
     */
    private List<FlightScheduleDTO> fetchAllFlightData(String flightType, String requestedDate) throws Exception {
        List<FlightScheduleDTO> allFlights = new ArrayList<>();
        
        //// System.out.println("=== 동적 페이징 시작 ===");
        //// System.out.println("flightType: " + flightType + ", date: " + requestedDate);
        
        // 1단계: 첫 페이지를 가져와서 총 개수 확인
        //// System.out.println("1단계: 총 데이터 개수 확인을 위해 첫 페이지 요청");
        String firstPageUrl = buildApiUrl(flightType, requestedDate, 1, 10); // 적은 수로 먼저 테스트
        String firstResponse = projectDao.fetchFlightData(firstPageUrl);
        
        int totalCount = getTotalCountFromResponse(firstResponse);
        //// System.out.println("API에서 확인된 총 데이터 개수: " + totalCount);
        
        if (totalCount <= 0) {
            //// System.out.println("데이터가 없습니다.");
            return allFlights;
        }
        
        // 2단계: 총 개수에 맞춰 적절한 페이지 수로 모든 데이터 가져오기
        int itemsPerPage = 100; // 한 번에 가져올 데이터 수
        int totalPages = (int) Math.ceil((double) totalCount / itemsPerPage);
        
        //// System.out.println("2단계: 총 " + totalPages + "페이지의 데이터를 " + itemsPerPage + "개씩 가져오기");
        
        for (int currentPage = 1; currentPage <= totalPages; currentPage++) {
            //// System.out.println("페이지 " + currentPage + "/" + totalPages + " 데이터 요청 중...");
            
            // 현재 페이지의 API URL 생성
            String apiUrl = buildApiUrl(flightType, requestedDate, currentPage, itemsPerPage);
            
            // DAO를 통해 데이터 가져오기
            String xmlResponse = projectDao.fetchFlightData(apiUrl);
            
            // XML 응답을 DTO 리스트로 변환
            List<FlightScheduleDTO> pageFlights = parseAndMapToDtoList(xmlResponse, flightType, requestedDate);
            
            if (pageFlights.isEmpty()) {
                //// System.out.println("페이지 " + currentPage + "에서 데이터가 없음. 조기 종료.");
                break;
            } else {
                //// System.out.println("페이지 " + currentPage + "에서 " + pageFlights.size() + "개 데이터 수집");
                allFlights.addAll(pageFlights);
            }
            
            // 안전장치: 최대 100페이지까지만
            if (currentPage >= 100) {
                //// System.out.println("최대 페이지 수 도달. 페이징 종료.");
                break;
            }
        }
        
        //// System.out.println("=== 동적 페이징 완료 ===");
        //// System.out.println("총 수집된 데이터: " + allFlights.size() + "개 (예상: " + totalCount + "개)");
        return allFlights;
    }
    
    /**
     * API 응답에서 총 개수를 추출하는 메소드
     */
    private int getTotalCountFromResponse(String xmlResponse) {
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(xmlResponse);
            JSONObject responseNode = xmlJSONObj.optJSONObject("response");
            if (responseNode != null) {
                JSONObject bodyNode = responseNode.optJSONObject("body");
                if (bodyNode != null) {
                    int totalCount = bodyNode.optInt("totalCount", 0);
                    // System.out.println("응답에서 추출한 totalCount: " + totalCount);
                    return totalCount;
                }
            }
        } catch (Exception e) {
            // System.out.println("totalCount 추출 중 오류: " + e.getMessage());
        }
        return 0;
    }

    /**
     * flightType과 날짜에 따라 적절한 API URL을 생성합니다. (기본값 사용)
     */
    private String buildApiUrl(String flightType, String date) throws Exception {
        return buildApiUrl(flightType, date, 1, 200);
    }

    /**
     * flightType, 날짜, 페이지 번호, 개수에 따라 적절한 API URL을 생성합니다.
     */
    private String buildApiUrl(String flightType, String date, int pageNo, int numOfRows) throws Exception {
        String schDateForApi = date.replace("-", "");
        String baseUrl;
        
        //// System.out.println("=== API URL 구성 디버깅 ===");
        //// System.out.println("입력 flightType: " + flightType);
        //// System.out.println("입력 date: " + date);
        //// System.out.println("API용 날짜: " + schDateForApi);
        
        if ("domestic".equalsIgnoreCase(flightType)) {
            baseUrl = "http://openapi.airport.co.kr/service/rest/FlightScheduleList/getDflightScheduleList";
            // System.out.println("국내선 API 선택");
        } else if ("international".equalsIgnoreCase(flightType)) {
            baseUrl = "http://openapi.airport.co.kr/service/rest/FlightScheduleList/getIflightScheduleList";
            // System.out.println("국제선 API 선택");
        } else { // "all" 또는 "realtime"
            // System.out.println("전체/실시간 - 국제선 API 사용");
            baseUrl = "http://openapi.airport.co.kr/service/rest/FlightScheduleList/getIflightScheduleList";
        }
        
        String finalUrl = baseUrl + "?serviceKey=" + serviceKey +
               "&schDate=" + URLEncoder.encode(schDateForApi, StandardCharsets.UTF_8) +
               "&numOfRows=" + numOfRows + "&pageNo=" + pageNo;
        
        //// System.out.println("최종 API URL (페이지 " + pageNo + ", 개수 " + numOfRows + "): " + finalUrl);
        return finalUrl;
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
        
        
        // // System.out.println("=== API 응답 데이터 전체 ===");
        // // System.out.println("총 항목 수: " + itemsArrayFromApi.length());
        
        for (int i = 0; i < itemsArrayFromApi.length(); i++) {
            JSONObject apiFlight = itemsArrayFromApi.getJSONObject(i);
            
            //// System.out.println("=== 항목 " + (i + 1) + " 상세 데이터 ===");
            //// System.out.println("전체 JSON: " + apiFlight.toString(2));
            
            // 실제 사용 가능한 필드들 확인
			/*
			 * // System.out.println("사용 가능한 키들:"); for (String key : apiFlight.keySet()) {
			 * // System.out.println("  - " + key + ": " + apiFlight.opt(key)); }
			 */
            
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
                statusVal = "제공 안한";
            } else {
                // "international", "all", "realtime" 모두 국제선 API 응답 구조로 처리
                flightNoVal = apiFlight.optString("internationalNum", "N/A");
                airlineVal = apiFlight.optString("airlineKorean", "N/A");
                
                //// System.out.println("디버깅 - 항공편명: " + flightNoVal);
                //// System.out.println("디버깅 - 항공사: " + airlineVal);
                
                // 국제선 API는 출발/도착 구분이 internationalIoType으로 됨
                String ioType = apiFlight.optString("internationalIoType", "");
                // // System.out.println("디버깅 - IO타입: " + ioType);
                
                if("OUT".equalsIgnoreCase(ioType)) {
                    // 출발편: 인천 -> 해외도시
                    originVal = apiFlight.optString("cityCode", "제공안함");  // 기본값을 인천으로 설정
                    destVal = apiFlight.optString("airportCode", "제공안함");
                    depTimeVal = formatTime(apiFlight.optString("internationalTime", ""));
                    arrTimeVal = addHoursToTime(depTimeVal, 2); // 출발시간에 2시간 추가
                    // // System.out.println("디버깅 - 출발편: " + originVal + " -> " + destVal + " at " + depTimeVal);
                } else if("IN".equalsIgnoreCase(ioType)) {
                    // 도착편: 해외도시 -> 인천
                    originVal = apiFlight.optString("airportCode", "N/A");
                    destVal = apiFlight.optString("cityCode", "인천");  // 기본값을 인천으로 설정
                    depTimeVal = formatTime(apiFlight.optString("internationalTime", ""));
                    arrTimeVal = addHoursToTime(depTimeVal, 2); // 출발시간에 2시간 추가
                    //// System.out.println("디버깅 - 도착편: " + originVal + " -> " + destVal + " at " + arrTimeVal);
                } else {
                    // IO타입이 명확하지 않은 경우
                    originVal = apiFlight.optString("city", "N/A");
                    destVal = apiFlight.optString("airport", "N/A");
                    depTimeVal = formatTime(apiFlight.optString("internationalTime", ""));
                    arrTimeVal = "N/A";
                   // // System.out.println("디버깅 - 타입 불명: " + originVal + " <-> " + destVal);
                }
                statusVal = "제공안함쓰";
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
        // // System.out.println("디버깅 - 시간 포맷팅 입력값: '" + timeStr + "'");
        
        if (timeStr == null || timeStr.isEmpty()) {
            // // System.out.println("디버깅 - 시간값이 null 또는 비어있음");
            return "N/A";
        }
        
        // 4자리 숫자 형식 (예: "0000", "0745")
        if (timeStr.length() == 4 && timeStr.matches("\\d{4}")) {
            String formatted = timeStr.substring(0, 2) + ":" + timeStr.substring(2, 4);
            // // System.out.println("디버깅 - 4자리 시간 포맷팅: " + timeStr + " -> " + formatted);
            return formatted;
        }
        // 12자리 이상의 긴 숫자 형식
        else if (timeStr.length() >= 12 && timeStr.matches("\\d{12,}")) {
            String formatted = timeStr.substring(8, 10) + ":" + timeStr.substring(10, 12);
            // // System.out.println("디버깅 - 긴 시간 포맷팅: " + timeStr + " -> " + formatted);
            return formatted;
        }
        // 이미 포맷된 시간이거나 다른 형식
        else {
            // // System.out.println("디버깅 - 시간 포맷팅 불가, 원본 반환: " + timeStr);
            return timeStr;
        }
    }
    
    /**
     * 시간 문자열에 지정된 시간을 추가하는 헬퍼 메소드
     */
    private String addHoursToTime(String timeStr, int hoursToAdd) {
        if (timeStr == null || timeStr.equals("N/A") || !timeStr.contains(":")) {
            return timeStr; // 유효하지 않은 시간은 그대로 반환
        }
        
        try {
            String[] timeParts = timeStr.split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);
            
            // 시간 추가
            hours += hoursToAdd;
            
            // 24시간 형식으로 조정
            if (hours >= 24) {
                hours = hours % 24;
            }
            
            // 포맷팅해서 반환
            return String.format("%02d:%02d", hours, minutes);
        } catch (Exception e) {
            // // System.out.println("시간 계산 오류: " + e.getMessage());
            return timeStr; // 오류 시 원본 반환
        }
    }
}