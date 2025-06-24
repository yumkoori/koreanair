package com.koreanair.command;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.koreanair.model.service.BookingService;

public class NonUserHandler implements CommandHandler{
	
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BookingService bookingService = new BookingService();
		
		System.out.println("🔥 NonUserHandler.process() 호출됨!");
		System.out.println("🔍 요청 메서드: " + request.getMethod());
		System.out.println("🔍 요청 URI: " + request.getRequestURI());
		System.out.println("🔍 요청 URL: " + request.getRequestURL());
		
		// JSON 응답 설정
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			// GET 요청인 경우 테스트 응답
			if ("GET".equals(request.getMethod())) {
				System.out.println("🧪 GET 요청 테스트 응답");
				String testResponse = "{\"success\":true,\"message\":\"NonUserHandler GET 테스트 성공\",\"method\":\"GET\"}";
				out.print(testResponse);
				out.flush();
				return null;
			}
			
			// POST 요청 body에서 JSON 데이터 읽기
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			
			String jsonString = sb.toString();
			System.out.println("📦 받은 JSON 데이터: " + jsonString);
			
			// 간단한 JSON 파싱 (정규식 사용)
			String bookingId = extractJsonValue(jsonString, "bookingId");
			String bookingPW = extractJsonValue(jsonString, "bookingPW");
			
			System.out.println("🔐 추출된 데이터 - bookingId: " + bookingId + ", bookingPW 길이: " + (bookingPW != null ? bookingPW.length() : 0));
			
			// 유효성 검사
			if (bookingId == null || bookingId.trim().isEmpty()) {
				throw new IllegalArgumentException("bookingId가 비어있습니다.");
			}
			
			if (bookingPW == null || bookingPW.trim().isEmpty()) {
				throw new IllegalArgumentException("bookingPW가 비어있습니다.");
			}
			
			// 일단 테스트를 위해 서비스 호출 없이 성공 응답
			System.out.println("🧪 테스트 모드: 서비스 호출 없이 성공 응답");
			
			// Gson을 사용하여 응답 생성
			ResponseData responseData = new ResponseData(true, "비밀번호가 성공적으로 업데이트되었습니다.", bookingId);
			String responseJson = new Gson().toJson(responseData);
			
			System.out.println("📤 응답 JSON: " + responseJson);
			out.print(responseJson);
			out.flush();
			
			bookingService.updateNonUserPw(bookingId, bookingPW);
			
		} catch (Exception e) {
			System.err.println("❌ NonUserHandler 오류: " + e.getMessage());
			e.printStackTrace();
			
			// 오류 응답
			ResponseData errorResponse = new ResponseData(false, "서버 오류: " + e.getMessage(), null);
			String errorJson = new Gson().toJson(errorResponse);
			
			response.setStatus(500); // Internal Server Error
			out.print(errorJson);
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					System.err.println("PrintWriter 닫기 오류: " + e.getMessage());
				}
			}
		}
		
		System.out.println("🔚 NonUserHandler.process() 완료");
		return null; // JSON 응답이므로 JSP 페이지로 리다이렉트하지 않음
	}
	
	// 간단한 JSON 값 추출 메서드
	private String extractJsonValue(String json, String key) {
		try {
			String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
			java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
			java.util.regex.Matcher m = p.matcher(json);
			
			if (m.find()) {
				return m.group(1);
			}
			return null;
		} catch (Exception e) {
			System.err.println("JSON 파싱 오류: " + e.getMessage());
			return null;
		}
	}
	
	// 응답 데이터 클래스
	private static class ResponseData {
		private boolean success;
		private String message;
		private String bookingId;
		
		public ResponseData(boolean success, String message, String bookingId) {
			this.success = success;
			this.message = message;
			this.bookingId = bookingId;
		}
		
		// Gson을 위한 getter 메서드들
		public boolean isSuccess() {
			return success;
		}
		
		public String getMessage() {
			return message;
		}
		
		public String getBookingId() {
			return bookingId;
		}
	}
}
