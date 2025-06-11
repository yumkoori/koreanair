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

@WebServlet("/reservationDetail")
public class ReservationDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");

        if (bookingId != null && !bookingId.trim().isEmpty()) {
            ReservationDTO reservationDetails = bookingService.getBookingDetailsById(bookingId);
            request.setAttribute("reservation", reservationDetails);
        }
        
        // WEB-INF 내부에 JSP를 두어 보안 강화
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/reservationDetail.jsp");
        dispatcher.forward(request, response);
    }
}