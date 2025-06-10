package com.koreanair.model.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.jasper.tagplugins.jstl.core.Url;

public class ProjectDaoimpl implements ProjectDao{

	@Override
	public String fetchFlightData(String apiUrl) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader br = null;
		
		try {
			URL url = new URL(apiUrl);
			conn =(HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
            conn.setUseCaches(false);
            
            int responseCode = conn.getResponseCode();
            
            if( responseCode == 200 ) {
           br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
           StringBuilder responseDataXml = new StringBuilder();
           String line;
           while ((line = br.readLine()) != null) {
               responseDataXml.append(line);
           }
           // 성공 시, 가공하지 않은 원본 XML 문자열 반환
           return responseDataXml.toString();
            }else {
            	throw new Exception("API call failed with HTTP code: " + responseCode);
            }
		} finally {
            // 자원 해제
            if (br != null) 
            	try { 
            	br.close(); } 
            catch (Exception e) {
            	e.printStackTrace();
            }
            if (conn != null) conn.disconnect();
        }
	}

}
