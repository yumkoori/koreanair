<%@ page contentType="application/json; charset=UTF-8" language="java" %>
<%@ page import="java.io.BufferedReader, java.io.InputStreamReader, java.net.HttpURLConnection, java.net.URL, java.net.URLEncoder, java.time.LocalDate, java.time.format.DateTimeFormatter" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="org.json.JSONArray, org.json.JSONObject, org.json.XML" %>
<%--
    이 JSP는 클라이언트(index5.jsp)로부터 특정 날짜와 항공편 유형(flightType)을 파라미터로 받아,
    flightType에 따라 적절한 외부 API를 호출하고 그 결과를 JSON으로 변환하여 반환합니다.
--%>
<%
    String requestedDate = request.getParameter("date");
    String flightType = request.getParameter("flightType");

    if (flightType == null || flightType.trim().isEmpty()) {
        flightType = "all";
    }
    // "realtime" 일 경우 요청 날짜를 현재 서버 날짜로 변경
    if ("realtime".equalsIgnoreCase(flightType)) {
        requestedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    System.out.println("WorkspaceFlightData.jsp received date: " + requestedDate + ", flightType: " + flightType);

    String domesticApiUrlBase = "http://openapi.airport.co.kr/service/rest/FlightScheduleList/getDflightScheduleList";
    String domesticApiServiceKey = "dLBL5CIKkYQdMzdojTQt8S0fxCrV6tsTTLnqWu%2BDf67TZrgwb3mAinbPtYPmX%2BbNEQD7%2FRhDKq5oo8DGlDrXcA%3D%3D";

    String internationalApiUrlBase = "http://openapi.airport.co.kr/service/rest/FlightScheduleList/getIflightScheduleList";
    String internationalApiServiceKey = "dLBL5CIKkYQdMzdojTQt8S0fxCrV6tsTTLnqWu%2BDf67TZrgwb3mAinbPtYPmX%2BbNEQD7%2FRhDKq5oo8DGlDrXcA%3D%3D";

    String realtimeApiUrlBase = "http://openapi.airport.co.kr/service/rest/FlightStatusList/getFlightStatusList";
    String realtimeApiServiceKey = "dLBL5CIKkYQdMzdojTQt8S0fxCrV6tsTTLnqWu%2BDf67TZrgwb3mAinbPtYPmX%2BbNEQD7%2FRhDKq5oo8DGlDrXcA%3D%3D";

    // "all"과 "realtime"은 동일한 API(FlightStatusList)를 사용
    String allInOneApiUrlBase = "http://openapi.airport.co.kr/service/rest/FlightStatusList/getFlightStatusList";
    String allInOneApiServiceKey = "dLBL5CIKkYQdMzdojTQt8S0fxCrV6tsTTLnqWu%2BDf67TZrgwb3mAinbPtYPmX%2BbNEQD7%2FRhDKq5oo8DGlDrXcA%3D%3D";

    String jsonOutput = "[]"; // 최종 클라이언트 반환 JSON 문자열
    JSONArray clientCompatibleArray = new JSONArray(); // API 응답을 가공하여 담을 JSON 배열

    // 날짜 파라미터 유효성 검사
    if (requestedDate == null || requestedDate.trim().isEmpty()) {
        JSONObject errorJson = new JSONObject();
        errorJson.put("error", "날짜 파라미터가 필요합니다.");
        clientCompatibleArray.put(errorJson);
        jsonOutput = clientCompatibleArray.toString();
        out.print(jsonOutput);
        out.flush();
        return;
    }

    String schDateForApi = requestedDate.replace("-", ""); // API 요청용 날짜 형식 변경 (YYYY-MM-DD -> YYYYMMDD)
    HttpURLConnection conn = null;
    BufferedReader br = null;
    StringBuilder responseDataXml = new StringBuilder(); // API 응답(XML)을 저장할 StringBuilder
    String apiUrlToCall = ""; // 호출할 API URL
    String currentServiceKeyUsed = ""; // 현재 사용된 서비스 키 (로깅용)

    try {
        // flightType에 따라 API URL 및 서비스 키 설정
        if ("domestic".equalsIgnoreCase(flightType)) {
            currentServiceKeyUsed = domesticApiServiceKey;
            apiUrlToCall = domesticApiUrlBase + "?serviceKey=" + currentServiceKeyUsed + 
                           "&schDate=" + URLEncoder.encode(schDateForApi, "UTF-8") +
                           "&numOfRows=200&pageNo=1";
        } else if ("international".equalsIgnoreCase(flightType)) {
            currentServiceKeyUsed = internationalApiServiceKey;
            apiUrlToCall = internationalApiUrlBase + "?serviceKey=" + currentServiceKeyUsed +
                           "&schDate=" + URLEncoder.encode(schDateForApi, "UTF-8") +
                           "&numOfRows=200&pageNo=1";
        } else if ("realtime".equalsIgnoreCase(flightType)) {
            currentServiceKeyUsed = realtimeApiServiceKey;
            apiUrlToCall = realtimeApiUrlBase + "?serviceKey=" + currentServiceKeyUsed +
                           "&schDate=" + URLEncoder.encode(schDateForApi, "UTF-8") + // realtime도 schDate 사용
                           "&numOfRows=200&pageNo=1";
        } else { // "all" 또는 정의되지 않은 flightType (기본적으로 "all"로 처리됨)
            currentServiceKeyUsed = allInOneApiServiceKey;
            apiUrlToCall = allInOneApiUrlBase + "?serviceKey=" + currentServiceKeyUsed +
                           "&schDate=" + URLEncoder.encode(schDateForApi, "UTF-8") +
                           "&numOfRows=200&pageNo=1";
        }

        System.out.println("Requesting API URL (WorkspaceFlightData.jsp for " + flightType + "): " + apiUrlToCall);

        URL url = new URL(apiUrlToCall);
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000); // 연결 타임아웃 10초
        conn.setReadTimeout(15000);    // 읽기 타임아웃 15초
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/xml"); // XML 응답 요청
        conn.setUseCaches(false); // 캐시 사용 안함

        int responseCode = conn.getResponseCode(); // API 응답 코드 받기
        System.out.println("API Response Code (" + flightType + "): " + responseCode);

        if (responseCode != 200) { // API 호출 실패
            String errorResponse = "";
            if (conn.getErrorStream() != null) { // 오류 스트림에서 상세 내용 읽기
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                StringBuilder errorSb = new StringBuilder(); String errorLine;
                while((errorLine = errorReader.readLine()) != null) { errorSb.append(errorLine); }
                errorReader.close(); errorResponse = errorSb.toString();
                System.out.println("API Error Response Body (" + flightType + "): " + errorResponse);
            }
            JSONObject errorResult = new JSONObject();
            errorResult.put("error", flightType + " API 호출 실패: HTTP " + responseCode + " " + conn.getResponseMessage());
            errorResult.put("errorDetail", errorResponse); // 상세 오류 내용 추가
            clientCompatibleArray.put(errorResult);
        } else { // API 호출 성공
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                responseDataXml.append(line);
            }
            br.close();
        }
    } catch (Exception e) { // API 호출 또는 데이터 읽기 중 예외 발생
        JSONObject exceptionResult = new JSONObject();
        exceptionResult.put("error", flightType + " API 처리 중 예외 발생: " + e.getMessage());
        clientCompatibleArray.put(exceptionResult);
        System.err.println("WorkspaceFlightData.jsp Error for " + flightType + ": " + e.getMessage());
        e.printStackTrace();
    } finally { // 자원 해제
        if (br != null) try { br.close(); } catch (Exception e) { /* ignore */ }
        if (conn != null) conn.disconnect();
    }

    // XML 파싱 및 JSON 변환 (API 호출이 성공했고, 아직 오류가 기록되지 않았을 경우)
    if (responseDataXml.length() > 0 && clientCompatibleArray.length() == 0) {
        System.out.println("Raw XML Response Data (WorkspaceFlightData.jsp - for " + requestedDate + ", type " + flightType + " - first 1000 chars): " + responseDataXml.toString().substring(0, Math.min(responseDataXml.length(), 1000)) + "...");
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(responseDataXml.toString());
            JSONObject responseNode = xmlJSONObj.optJSONObject("response");

            if (responseNode == null) { // 응답에 'response' 노드가 없는 경우
                JSONObject noDataResult = new JSONObject();
                noDataResult.put("error", "파싱된 API 응답 형식이 예상과 다릅니다 (response 노드 없음).");
                clientCompatibleArray.put(noDataResult);
            } else {
                JSONObject headerNode = responseNode.optJSONObject("header");
                // API 자체에서 오류 코드를 반환한 경우 (정상 서비스 코드가 아니거나, 메시지에 NORMAL/SUCCESS가 없는 경우)
                if (headerNode != null && !"00".equals(headerNode.optString("resultCode")) 
                    && !(headerNode.optString("resultMsg","").toUpperCase().contains("NORMAL") || headerNode.optString("resultMsg","").toUpperCase().contains("SUCCESS"))) {
                    JSONObject apiErrorResult = new JSONObject();
                    apiErrorResult.put("error", "API 오류 ("+flightType+"): " + headerNode.optString("resultMsg", "알 수 없는 API 오류") + " (코드: " + headerNode.optString("resultCode") + ")");
                    clientCompatibleArray.put(apiErrorResult);
                } else {
                    JSONObject bodyNode = responseNode.optJSONObject("body");
                    // body 노드가 있고, items가 있거나 totalCount가 0인 경우 (데이터가 없는 정상 응답 포함)
                    if (bodyNode != null && (bodyNode.has("items") || bodyNode.optInt("totalCount", -1) == 0)) {
                        JSONArray itemsArrayFromApi = new JSONArray();
                        // items가 있고, 그 안에 item이 있는 경우 (item은 단일 객체 또는 배열일 수 있음)
                        if (bodyNode.has("items") && bodyNode.optJSONObject("items") != null && bodyNode.getJSONObject("items").has("item")) {
                            Object itemsObj = bodyNode.getJSONObject("items").get("item");
                            if (itemsObj instanceof JSONArray) {
                                itemsArrayFromApi = (JSONArray) itemsObj;
                            } else if (itemsObj instanceof JSONObject) {
                                itemsArrayFromApi.put(itemsObj); // 단일 아이템도 배열에 추가
                            }
                        }
                        
                        System.out.println("Number of items from API (" + flightType + "): " + itemsArrayFromApi.length() + ", TotalCount in body: " + bodyNode.optInt("totalCount", -1));
                        
                        if (itemsArrayFromApi.length() > 0) {
                            System.out.println("DEBUG_JSP: Structure of FIRST item from " + flightType + " API BEFORE mapping:");
                            System.out.println(itemsArrayFromApi.getJSONObject(0).toString(2)); // 첫번째 아이템 구조 로깅
                        }

                        for (int i = 0; i < itemsArrayFromApi.length(); i++) {
                            JSONObject apiFlight = itemsArrayFromApi.getJSONObject(i);
                            JSONObject clientFlight = new JSONObject(); // 클라이언트에 전달할 최종 JSON 객체
                            
                            // 필드 초기화
                            String flightNoVal = "N/A", airlineVal = "N/A", originVal = "N/A", destVal = "N/A";
                            String depTimeVal = "N/A", arrTimeVal = "N/A", statusVal = "N/A";
                            String tempTime; // 시간 포맷팅용 임시 변수

                            if ("domestic".equalsIgnoreCase(flightType)) {
                                flightNoVal = apiFlight.optString("domesticNum", "N/A");
                                airlineVal = apiFlight.optString("airlineKorean", apiFlight.optString("airlineEnglish", "N/A"));
                                originVal = apiFlight.optString("startcity", "N/A");
                                destVal = apiFlight.optString("arrivalcity", "N/A");
                                
                                tempTime = apiFlight.optString("domesticStartTime", "");
                                if (tempTime.length() == 4) depTimeVal = tempTime.substring(0,2) + ":" + tempTime.substring(2,4); else depTimeVal = "N/A";
                                
                                tempTime = apiFlight.optString("domesticArrivalTime", "");
                                if (tempTime.length() == 4) arrTimeVal = tempTime.substring(0,2) + ":" + tempTime.substring(2,4); else arrTimeVal = "N/A";
                                
                                statusVal = "예정"; // 국내선 스케줄 API는 보통 "예정" (실제 상태 필드 확인 필요)
                                
                            } else if ("international".equalsIgnoreCase(flightType)) {
                                flightNoVal = apiFlight.optString("internationalNum", "N/A");
                                airlineVal = apiFlight.optString("airlineKorean", apiFlight.optString("airlineEnglish", "N/A"));
                                
                                String ioType = apiFlight.optString("internationalIoType", ""); // IN 또는 OUT
                                if("OUT".equalsIgnoreCase(ioType)) { // 출발편
                                    originVal = apiFlight.optString("city", "N/A"); 
                                    destVal = apiFlight.optString("airport", "N/A");
                                    tempTime = apiFlight.optString("internationalTime", ""); // 출발 시간
                                    if (tempTime.length() == 4) depTimeVal = tempTime.substring(0,2) + ":" + tempTime.substring(2,4); else depTimeVal = "N/A";
                                    arrTimeVal = "N/A"; 
                                } else { // 도착편 (IN)
                                    originVal = apiFlight.optString("airport", "N/A");
                                    destVal = apiFlight.optString("city", "N/A");
                                    // 수정된 부분: formatTime 호출 제거 및 직접 처리
                                    String internationalTimeValue = apiFlight.optString("internationalTime", ""); // 도착 시간
                                    if (internationalTimeValue.length() == 4) {
                                        arrTimeVal = internationalTimeValue.substring(0,2) + ":" + internationalTimeValue.substring(2,4);
                                    } else {
                                        arrTimeVal = "N/A";
                                    }
                                    depTimeVal = "N/A";
                                }
                                statusVal = "예정"; // 국제선 스케줄 API는 보통 "예정" (실제 상태 필드 확인 필요)

                            } else { // "all", "realtime" (FlightStatusList API 사용)
                                flightNoVal = apiFlight.optString("airFln", "N/A");
                                airlineVal = apiFlight.optString("airlineKorean", apiFlight.optString("airlineEnglish", "N/A"));
                                originVal = apiFlight.optString("boardingKor", apiFlight.optString("boardingEng", "N/A")); // 출발지
                                destVal = apiFlight.optString("arrivedKor", apiFlight.optString("arrivedEng", "N/A"));   // 도착지
                                statusVal = apiFlight.optString("rmkKor", apiFlight.optString("rmkEng", "N/A"));          // 운항상태

                                // 시간 처리 (etd: 변경 출발시간, std: 계획 출발시간)
                                String timeApi = apiFlight.optString("etd", apiFlight.optString("std", "")); // 변경시간 우선, 없으면 계획시간
                                tempTime = timeApi;
                                String formattedTime = "N/A";
                                if (tempTime.length() == 4) { // HHMM 형식
                                    formattedTime = tempTime.substring(0,2) + ":" + tempTime.substring(2,4);
                                } else if (tempTime.length() >= 12 && tempTime.matches("\\d{12,}")) { // YYYYMMDDHHMM 형식
                                    formattedTime = tempTime.substring(8, 10) + ":" + tempTime.substring(10, 12); // HH:MM 추출
                                } else if (!tempTime.isEmpty()){ // 기타 형식 (그대로 사용)
                                    formattedTime = tempTime;
                                }

                                String flightIoTypeVal = apiFlight.optString("io", ""); // O: 출발, I: 도착
                                if ("O".equalsIgnoreCase(flightIoTypeVal)) { // 출발편
                                    depTimeVal = formattedTime; arrTimeVal = "N/A";
                                } else if ("I".equalsIgnoreCase(flightIoTypeVal)) { // 도착편
                                    arrTimeVal = formattedTime; depTimeVal = "N/A";
                                } else { // 구분 없을 시 출발시간으로 간주 (또는 API 명세에 따라 결정)
                                    depTimeVal = formattedTime; arrTimeVal = "N/A";
                                }
                            }
                            
                            // 클라이언트에 전달할 최종 데이터 매핑
                            clientFlight.put("id", requestedDate + "-" + flightType + "-FL" + flightNoVal.replaceAll("[^a-zA-Z0-9]", "") + "_" + i);
                            clientFlight.put("flightNo", flightNoVal);
                            clientFlight.put("airline", airlineVal);
                            clientFlight.put("origin", originVal);
                            clientFlight.put("destination", destVal);
                            clientFlight.put("departureTime", depTimeVal);
                            clientFlight.put("arrivalTime", arrTimeVal);
                            clientFlight.put("status", statusVal);

                            clientCompatibleArray.put(clientFlight);
                        }
                        // 데이터 파싱 후 최종 배열 상태 로깅
                         if (clientCompatibleArray.length() == 0 && itemsArrayFromApi.length() > 0) {
                             System.out.println("DEBUG_JSP: Data received from "+flightType+" API but no items parsed into clientCompatibleArray (check field mappings or loop logic) for " + requestedDate);
                         } else if (clientCompatibleArray.length() == 0 && itemsArrayFromApi.length() == 0 && bodyNode.optInt("totalCount", 0) == 0) {
                             System.out.println("DEBUG_JSP: No items (totalCount:0 or items node missing/empty) from "+flightType+" API for " + requestedDate);
                         }

                    } else { // body 노드가 없거나, items가 없고 totalCount도 0이 아닌 경우 (비정상 응답)
                        JSONObject noBodyOrItemsResult = new JSONObject();
                        noBodyOrItemsResult.put("error", "API 응답("+flightType+")에 'body' 또는 'items' 노드가 없거나, 데이터가 없는 것으로 판단됩니다. (bodyNode: " + (bodyNode != null) + ", totalCount: " + (bodyNode != null ? bodyNode.optInt("totalCount", -1) : "N/A") + ")");
                        clientCompatibleArray.put(noBodyOrItemsResult);
                    }
                }
            }
        } catch (Exception e) { // XML 파싱 또는 JSON 변환 중 예외 발생
            JSONObject parseErrorResult = new JSONObject();
            parseErrorResult.put("error", "API 응답("+flightType+") 파싱 또는 처리 중 예외: " + e.getMessage());
            clientCompatibleArray.put(parseErrorResult);
            System.err.println("WorkspaceFlightData.jsp XML Parsing/Processing Error for " + flightType + ": " + e.getMessage());
            e.printStackTrace();
        }

    } else if (clientCompatibleArray.length() == 0) { // API 호출 실패 또는 응답 데이터가 없는 경우 (오류 메시지가 이미 clientCompatibleArray에 담겨있을 수 있음)
        // clientCompatibleArray가 비어있는 경우에만 (즉, API 호출 try-catch나 파싱 try-catch에서 오류를 안담은 경우) 메시지 추가
        // 보통은 이전 단계에서 오류가 담기므로 이 블록은 잘 실행되지 않을 수 있음.
        boolean errorAlreadyReported = false;
        if(jsonOutput.contains("\"error\":")){ // 이미 오류가 jsonOutput 기본값에 설정된 형태인지 (더 정확한 검사 필요)
             // 다만, clientCompatibleArray 기준으로 확인해야함
        }
        
        // clientCompatibleArray가 여전히 비어있다면 (어떤 이유로든 데이터도 없고 오류도 안담겼다면)
        if(clientCompatibleArray.length() == 0) {
             JSONObject emptyResponseResult = new JSONObject();
             emptyResponseResult.put("error", flightType + " API로부터 유효한 응답을 받지 못했거나 처리할 데이터가 없습니다. (responseDataXml empty or error already in array)");
             clientCompatibleArray.put(emptyResponseResult);
        }
    }

    jsonOutput = clientCompatibleArray.toString();
    System.out.println("Final JSON Output to Client (WorkspaceFlightData.jsp for " + requestedDate + ", type " + flightType + " - " + clientCompatibleArray.length() + " items): " + jsonOutput.substring(0, Math.min(jsonOutput.length(), 500)) + (jsonOutput.length() > 500 ? "..." : ""));
    out.print(jsonOutput);
    out.flush();
%>