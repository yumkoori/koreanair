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
		
		// form 데이터에서 승객 정보 추출
		String nationality = request.getParameter("passengers[0].nationality");
		String lastName = request.getParameter("passengers[0].lastName");
		String firstName = request.getParameter("passengers[0].firstName");
		String gender = request.getParameter("passengers[0].gender");
		String birthDate = request.getParameter("passengers[0].birthDate");
		String jobAirline = request.getParameter("passengers[0].jobAirline");
		String memberNumber = request.getParameter("passengers[0].memberNumber");
		String discountType = request.getParameter("passengers[0].discountType");
		String returnDiscountType = request.getParameter("passengers[0].returnDiscountType");
		
		// bookingId 정보 추출
		String bookingId = request.getParameter("bookingId");
		String outBookingId = request.getParameter("outBookingId");
		String returnBookingId = request.getParameter("returnBookingId");
		
		System.out.println("=== 받은 승객 정보 ===");
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
		
		// PassengerDTO 생성 - bookingId 우선, 없으면 outBookingId 사용
		String primaryBookingId = (bookingId != null && !bookingId.isEmpty() && !bookingId.equals("null")) 
				? bookingId : outBookingId;
		
		PassengerDTO dto = PassengerDTO.builder()
				.passengerId(null) // 자동 생성
				.userNo("1") // 세션에서 가져오거나 추후 설정
				.bookingId(primaryBookingId) // 기본 예약ID 또는 가는편 예약ID 설정
				.lastName(lastName)
				.firstName(firstName)
				.birthDate(birthDate)
				.gender(gender)
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
