package com.koreanair.model.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.Url;

import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.util.DBConn;

public class ProjectDaoimpl implements ProjectDao{
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	

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


	@Override
	public int seatsave(List<FlightSeatSaveDTO> seatList) throws Exception {
	    // 1. Connection과 PreparedStatement는 메서드 내의 지역 변수로 선언하는 것이 좋습니다.
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    int totalSavedCount = 0;

	    String sql = "INSERT INTO temporary (aircraft, row, seat, price) VALUES (?, ?, ?, ?)";
	    
	    try {
	        // 2. DBConn 유틸리티를 사용해 커넥션을 얻어옵니다.
	        conn = DBConn.getConnection(); 
	        
	        // conn.setAutoCommit(false); // 트랜잭션 시작
	        pstmt = conn.prepareStatement(sql);

	        // 3. 파라미터로 받은 'seatList'를 반복문으로 처리합니다.
	        //    'dto' 변수는 여기서 각 좌석 정보를 받아와 사용합니다.
	        for (FlightSeatSaveDTO dto : seatList) { 
	            // 4. 'dto' 객체의 값으로 PreparedStatement를 설정합니다.
	            pstmt.setString(1, dto.getAircraft());
	            pstmt.setInt(2, dto.getRow()); 
	            pstmt.setString(3, dto.getSeat());
	            pstmt.setInt(4, dto.getPrice());
	            
	            pstmt.addBatch(); // 실행할 쿼리에 추가
	        }
	        
	        int[] resultCounts = pstmt.executeBatch(); // 배치 쿼리 실행
	        // conn.commit(); // 트랜잭션 성공 (커밋)

	        totalSavedCount = resultCounts.length; // 성공한 개수

	    } catch (Exception e) {
	        if (conn != null) conn.rollback(); // 오류 시 롤백
	        System.out.println("DAO seatsave 오류");
	        e.printStackTrace();
	        throw e; // 오류를 상위로 전달
	    } finally {
	        // 5. 자원 해제
	    	DBConn.close(conn, pstmt);  // DBConn 유틸리티에 close 메서드가 있다면 사용
	    }
	    
	    return totalSavedCount;
	}

}
