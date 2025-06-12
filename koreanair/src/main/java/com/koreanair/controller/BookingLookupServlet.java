package com.koreanair.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.service.BookingService;

@WebServlet("/lookup")
public class BookingLookupServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String bookingId = request.getParameter("bookingId");
        String departureDate = request.getParameter("departureDate");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");

        ReservationDTO bookingInfo = bookingService.searchBooking(bookingId, departureDate, lastName, firstName);
        HttpSession session = request.getSession();

        if (bookingInfo != null) {
            // [성공] 임시 이용권(조회 결과)을 세션에 저장하고 상세 페이지로 이동
            session.setAttribute("lookupResult", bookingInfo);
            session.removeAttribute("lookupFailed"); // 혹시 남아있을 실패 플래그 제거
            response.sendRedirect("reservationDetail?bookingId=" + bookingInfo.getBookingId());
        } else {
            // [실패] 실패 플래그를 세션에 저장하고 메인 페이지로 복귀
            session.setAttribute("lookupFailed", true);
            session.removeAttribute("lookupResult"); // 혹시 남아있을 성공 정보 제거
            response.sendRedirect("index.jsp#checkin");
        }
    }
}