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

import com.koreanair.model.dao.AirportDAO;
import com.koreanair.model.dto.AirportDTO;

public class AirportService {

    private AirportDAO dao = new AirportDAO();

	public List<String> searchAirportCities(String keyword) throws Exception {
	    String serviceKey = "8P8uJUdkLc4fdMmPElOLwBHcmo5822gwkjaY%2Fy8hWEf%2FUqhENmeY7NbJFpg%2Bsf%2FkvNj3UHtZWDSphVKixegI5g%3D%3D";
	    int pageNo = 1;
	    int numOfRows = 100; // 너무 크면 서버에서 막히므로 안전한 값 사용
	    int totalCount = Integer.MAX_VALUE; // 처음엔 모름

	    List<String> result = new ArrayList<>();

	    while ((pageNo - 1) * numOfRows < totalCount) {
	        String url = "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList";
	        url += "?ServiceKey=" + serviceKey;
	        url += "&pageNo=" + pageNo;
	        url += "&numOfRows=" + numOfRows;

	        // HttpURLConnection 으로 API 호출
	        URL obj = new URL(url);
	        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
	        conn.setRequestMethod("GET");

	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        String inputLine;
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();

	        String responseXml = response.toString();

	        // XML 파싱
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(new InputSource(new StringReader(responseXml)));
	        doc.getDocumentElement().normalize();

	        // 총 건수 가져오기
	        String totalCountStr = doc.getElementsByTagName("totalCount").item(0).getTextContent();
	        totalCount = Integer.parseInt(totalCountStr);

	        // item 리스트 파싱
	        NodeList nList = doc.getElementsByTagName("item");

	        for (int i = 0; i < nList.getLength(); i++) {
	            Element element = (Element) nList.item(i);
	            String cityKor = element.getElementsByTagName("cityKor").item(0).getTextContent();

	            // keyword가 포함된 도시명만 추가 (공백 제거 후 비교)
	            if (cityKor.replaceAll("\\s+", "").contains(keyword.replaceAll("\\s+", ""))) {
	                result.add(cityKor);
	            }
	        }

	        pageNo++; // 다음 페이지로
	    }

	    return result;
	}

	public List<AirportDTO> searchAirportsByKeyword(String keyword) {
        return dao.findAirportsByKeyword(keyword);
    }

	
}


