package com.koreanair.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        // 조회 결과를 request 객체에 저장
        request.setAttribute("bookingInfo", bookingInfo);
        
        // 조회 실패 시 사용자에게 피드백을 주기 위한 플래그
        if (bookingInfo == null) {
            request.setAttribute("lookupFailed", true);
        }
        
        // [중요] 결과 페이지를 다시 index.jsp로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}