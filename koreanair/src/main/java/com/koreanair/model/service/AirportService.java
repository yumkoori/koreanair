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

public class AirportService {

	
	public List<String> searchAirportCities(String keyword) throws Exception {
	    String url = "http://openapi.airport.co.kr/service/rest/AirportCodeList/getAirportCodeList";
	    url += "?ServiceKey=dLBL5CIKkYQdMzdojTQt8S0fxCrV6tsTTLnqWu%2BDf67TZrgwb3mAinbPtYPmX%2BbNEQD7%2FRhDKq5oo8DGlDrXcA%3D%3D&pageNo=1";

	    List<String> result = new ArrayList<>();

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

	    // XML 파싱
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(new InputSource(new StringReader(response.toString())));
	    doc.getDocumentElement().normalize();

	    NodeList nList = doc.getElementsByTagName("item");

	    for (int i = 0; i < nList.getLength(); i++) {
	        Element element = (Element) nList.item(i);
	        String cityKor = element.getElementsByTagName("cityKor").item(0).getTextContent();

	        // keyword가 포함된 도시명만 추가
	        if (cityKor.contains(keyword)) {
	            result.add(cityKor);
	        }
	    }

	    return result;
	}
	
}


