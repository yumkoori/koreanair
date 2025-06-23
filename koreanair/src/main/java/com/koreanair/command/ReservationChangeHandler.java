package com.koreanair.command;

import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.service.BookingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReservationChangeHandler implements CommandHandler {

    private final BookingService bookingService = new BookingService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());

        switch (action) {
            // 1. 예약 변경 첫 페이지 (여정/승객 선택)를 보여주는 로직
            case "/changeReservationSelect.do":
                return showSelectionPage(request, response);

            // 2. 두 번째 페이지 (날짜/등급 변경)를 보여주는 로직
            case "/changeReservationSearch.do":
                return showSearchPage(request, response);
                
            default:
                return "redirect:/index.do";
        }
    }

    // reservationSelect.jsp를 보여주는 메서드
    private String showSelectionPage(HttpServletRequest request, HttpServletResponse response) {
        String bookingId = request.getParameter("bookingId");
        // 비회원도 접근 가능하다고 가정하고 userId는 null로 처리
        ReservationDTO reservation = bookingService.getBookingDetailsById(bookingId, null);

        if (reservation != null) {
            request.setAttribute("reservation", reservation);
            return "/WEB-INF/views/reservationSelect.jsp";
        } else {
            // 예약 정보가 없으면 에러 처리
            request.setAttribute("error", "예약 정보를 찾을 수 없습니다.");
            return "/dashboard.do";
        }
    }

    // reservationChange.jsp를 보여주는 메서드
    private String showSearchPage(HttpServletRequest request, HttpServletResponse response) {
        String bookingId = request.getParameter("bookingId");
        ReservationDTO reservation = bookingService.getBookingDetailsById(bookingId, null);

         if (reservation != null) {
            request.setAttribute("reservation", reservation);
            return "/WEB-INF/views/reservationChange.jsp";
        } else {
            request.setAttribute("error", "예약 정보를 찾을 수 없습니다.");
            return "/dashboard.do";
        }
    }
}