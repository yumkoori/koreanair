package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.model.dto.PassengerDTO;
import com.koreanair.model.service.PassengerService;

public class PassengerHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PassengerService service = new PassengerService();
		
		System.out.println("PassengerHandler 호출");
		
		// 요청의 모든 파라미터 출력 (디버깅용)
		System.out.println("=== 모든 요청 파라미터 ===");
		java.util.Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			System.out.println(paramName + " = " + paramValue);
		}
		
		// form 데이터에서 개별 승객 정보 추출
		String nationality = request.getParameter("nationality");
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String gender = request.getParameter("gender");
		String birthDate = request.getParameter("birthDate");
		String jobAirline = request.getParameter("jobAirline");
		String memberNumber = request.getParameter("memberNumber");
		String discountType = request.getParameter("discountType");
		String returnDiscountType = request.getParameter("returnDiscountType");
		
		// 승객 인덱스 정보 추출
		String passengerIndexStr = request.getParameter("passengerIndex");
		int passengerIndex = 1;
		try {
			if (passengerIndexStr != null && !passengerIndexStr.isEmpty()) {
				passengerIndex = Integer.parseInt(passengerIndexStr);
			}
		} catch (NumberFormatException e) {
			System.out.println("승객 인덱스 파싱 오류, 기본값 1 사용: " + e.getMessage());
		}
		
		// bookingId 정보 추출
		String bookingId = request.getParameter("bookingId");
		String outBookingId = request.getParameter("outBookingId");
		String returnBookingId = request.getParameter("returnBookingId");
		
		System.out.println("=== 받은 승객 정보 ===");
		System.out.println("승객 번호: " + passengerIndex);
		System.out.println("국적: " + nationality);
		System.out.println("성: " + lastName);
		System.out.println("이름: " + firstName);
		System.out.println("성별: " + gender);
		System.out.println("생년월일: " + birthDate);
		System.out.println("직업항공사: " + jobAirline);
		System.out.println("회원번호: " + memberNumber);
		System.out.println("할인유형: " + discountType);
		System.out.println("오는여정할인: " + returnDiscountType);
		System.out.println("기본 예약ID: " + bookingId);
		System.out.println("가는편 예약ID: " + outBookingId);
		System.out.println("오는편 예약ID: " + returnBookingId);
		
		// 필수 필드 검증
		if (lastName == null || lastName.trim().isEmpty() || 
			firstName == null || firstName.trim().isEmpty() ||
			gender == null || gender.trim().isEmpty() ||
			birthDate == null || birthDate.trim().isEmpty()) {
			
			System.out.println("❌ 필수 필드 누락:");
			System.out.println("- lastName: " + (lastName != null ? "'" + lastName + "'" : "null"));
			System.out.println("- firstName: " + (firstName != null ? "'" + firstName + "'" : "null"));
			System.out.println("- gender: " + (gender != null ? "'" + gender + "'" : "null"));
			System.out.println("- birthDate: " + (birthDate != null ? "'" + birthDate + "'" : "null"));
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("필수 승객 정보가 누락되었습니다.");
			return null;
		}
		
		// PassengerDTO 생성 - bookingId 우선, 없으면 outBookingId 사용
		String primaryBookingId = (bookingId != null && !bookingId.isEmpty() && !bookingId.equals("null")) 
				? bookingId : outBookingId;
		
		PassengerDTO dto = PassengerDTO.builder()
				.passengerId(null) // 자동 생성
				.userNo("1") // 세션에서 가져오거나 추후 설정
				.bookingId(primaryBookingId) // 기본 예약ID 또는 가는편 예약ID 설정
				.lastName(lastName.trim())
				.firstName(firstName.trim())
				.birthDate(birthDate.trim())
				.gender(gender.trim())
				.type("ADULT") // 기본값
				.build();
		
		System.out.println("사용된 예약ID: " + primaryBookingId);
		System.out.println("생성된 기본 DTO: " + dto);
		
		// 가는편 승객 정보 저장
		service.savePassenger(dto);
		
		// 왕복 여행이고 오는편 예약ID가 있는 경우, 오는편 승객 정보도 저장
		if (returnBookingId != null && !returnBookingId.isEmpty() && !returnBookingId.equals("null")) {
			PassengerDTO returnDto = PassengerDTO.builder()
					.passengerId(null) // 자동 생성
					.userNo("1") // 세션에서 가져오거나 추후 설정
					.bookingId(returnBookingId) // 오는편 예약ID 설정
					.lastName(lastName)
					.firstName(firstName)
					.birthDate(birthDate)
					.gender(gender)
					.type("ADULT") // 기본값
					.build();
			
			System.out.println("생성된 오는편 DTO: " + returnDto);
			service.savePassenger(returnDto);
		}
		
		// 성공 페이지로 리다이렉트
		return "views/booking/booking.jsp";
	}

}
