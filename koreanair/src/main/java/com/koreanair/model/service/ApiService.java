package com.koreanair.model.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.koreanair.model.dto.AirportDTO;

public class ApiService {
	
	    private static final String SERVICE_KEY = "8P8uJUdkLc4fdMmPElOLwBHcmo5822gwkjaY%2Fy8hWEf%2FUqhENmeY7NbJFpg%2Bsf%2FkvNj3UHtZWDSphVKixegI5g%3D%3D";

	    public List<AirportDTO> fetchAirportList() throws Exception {
	        List<AirportDTO> resultList = new ArrayList<>();
	        int pageNo = 1;
	        int numOfRows = 100;
	        int totalCount = Integer.MAX_VALUE;

	        while ((pageNo - 1) * numOfRows < totalCount) {
	            String apiUrl = "https://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList"
	                          + "?ServiceKey=" + SERVICE_KEY
	                          + "&pageNo=" + pageNo
	                          + "&numOfRows=" + numOfRows;

	            // ✅ 실제 URL 확인
	            System.out.println("요청 URL: " + apiUrl);

	            URL url = new URL(apiUrl);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("GET");

	            int responseCode = conn.getResponseCode();
	            System.out.println("응답 코드: " + responseCode);

	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	            StringBuilder sb = new StringBuilder();
	            String line;
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	            br.close();

	            
	            System.out.println("응답 내용:\n" + sb.toString());  // 여기에 추가

	            
	            
	            // XML 파싱
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));
	            doc.getDocumentElement().normalize();

	            NodeList totalCountTag = doc.getElementsByTagName("totalCount");
	            if (totalCountTag.getLength() > 0) {
	                totalCount = Integer.parseInt(totalCountTag.item(0).getTextContent());
	            } else {
	                throw new RuntimeException("응답 XML에 totalCount가 없습니다. API KEY 혹은 요청 URL을 확인하세요.");
	            }

	            NodeList nodeList = doc.getElementsByTagName("item");

	            for (int i = 0; i < nodeList.getLength(); i++) {
	                Element el = (Element) nodeList.item(i);

	                String airportId = getTagValue(el, "airportId");
	                String airportName = getTagValue(el, "airportNm");
	                String airportEngName = getTagValue(el, "airportEngNm");
	                String cityKor = getTagValue(el, "cityKor");
	                String cityEng = getTagValue(el, "cityEng");

	                AirportDTO dto = AirportDTO.builder()
	                    .airportId(airportId)
	                    .airportName(airportName)
	                    .airportEngName(airportEngName)
	                    .cityKor(cityKor)
	                    .cityEng(cityEng)
	                    .build();

	                resultList.add(dto);
	            }

	            pageNo++;
	        }

	        return resultList;
	    }

	    private String getTagValue(Element el, String tagName) {
	        NodeList node = el.getElementsByTagName(tagName);
	        if (node.getLength() > 0 && node.item(0) != null) {
	            return node.item(0).getTextContent();
	        }
	        return "";
	    }

	    // ✅ 단독 실행 테스트
	    public static void main(String[] args) {
	        ApiService service = new ApiService();
	        try {
	            List<AirportDTO> list = service.fetchAirportList();
	            for (AirportDTO dto : list) {
	                System.out.println(dto);
	            }
	            System.out.println("총 개수: " + list.size());
	        } catch (Exception e) {
	            System.out.println("API 호출 또는 파싱 중 에러 발생");
	            e.printStackTrace();
	        }
	    }
}

