package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // HttpSession 임포트

import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.dto.User; // User DTO 임포트
import com.koreanair.model.service.BookingService;

public class CheckinDetailHandler implements CommandHandler {

    private final BookingService bookingService = new BookingService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession(false); // 세션이 없으면 새로 만들지 않음
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        ReservationDTO reservation = null;

        // [분기 처리] 로그인 상태인지 확인
        if (user != null) {
            // --- 1. 로그인 사용자 체크인 처리 ---
            String bookingId = request.getParameter("bookingId");
            // 서비스의 다른 메소드를 호출하여 본인 예약인지 검증하며 조회
            // 이 메소드는 내부적으로 "SELECT ... WHERE booking_id = ? AND user_id = ?" 쿼리를 사용해야 안전합니다.
            reservation = bookingService.getBookingDetailsById(bookingId, user.getUserId());

        } else {
            // --- 2. 비회원 사용자 체크인 처리 ---
            String bookingId = request.getParameter("bookingId");
            String departureDate = request.getParameter("departureDate");
            String lastName = request.getParameter("lastName");
            String firstName = request.getParameter("firstName");
            
            reservation = bookingService.searchBooking(bookingId, departureDate, lastName, firstName);
        }

        // --- 공통 후처리 ---
        if (reservation != null) {
            // 조회 성공 시, request에 정보를 담아 jsp로 포워딩
            request.setAttribute("reservation", reservation);
            return "/WEB-INF/views/checkinDetail.jsp";
        } else {
            // 조회 실패 시, 에러 메시지와 함께 메인 페이지로 리다이렉트
            request.getSession().setAttribute("lookupError", "입력하신 정보와 일치하는 예약이 없습니다.");
            return "redirect:/index.do";
        }
    }
}