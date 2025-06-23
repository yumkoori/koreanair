package com.koreanair.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet; // web.xml에서 매핑하므로 주석 처리
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.dto.User;
import com.koreanair.model.service.BookingService;

// @WebServlet("/reservationDetail") // web.xml에 직접 매핑하였으므로 주석 처리합니다.
public class ReservationDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingIdFromUrl = request.getParameter("bookingId");
        HttpSession session = request.getSession(false);

        // 세션에서 필요한 정보들을 미리 가져옴
        ReservationDTO lookupResult = (session != null) ? (ReservationDTO) session.getAttribute("lookupResult") : null;
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        // [흐름 1] '다른 예약 조회'를 통해 받은 임시 이용권(lookupResult)이 있는지 최우선으로 확인
        // 이 로직은 로그인 여부와 관계없이 가장 먼저 실행되어야 함
        if (lookupResult != null && lookupResult.getBookingId().equals(bookingIdFromUrl)) {
            // 성공: 임시 이용권이 유효하므로 상세 정보 표시
            request.setAttribute("reservation", lookupResult);
            session.removeAttribute("lookupResult"); // 사용한 임시 이용권은 즉시 제거
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/reservationDetail.jsp");
            dispatcher.forward(request, response);
            
        } else if (loggedInUser != null) {
            // [흐름 2] 임시 이용권이 없는 로그인 사용자 (예: 마이페이지에서 자기 예약을 클릭한 경우)
            ReservationDTO reservationDetails = bookingService.getBookingDetailsById(bookingIdFromUrl, loggedInUser.getUserId());

            if (reservationDetails != null) {
                // 성공: 본인 예약이 맞음
                request.setAttribute("reservation", reservationDetails);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/reservationDetail.jsp");
                dispatcher.forward(request, response);
            } else {
                // 실패: 존재하지 않거나, 타인의 예약에 직접 접근 시도
                request.setAttribute("error", "해당 예약 정보를 찾을 수 없거나, 고객님의 예약이 아닙니다.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/dashboard.do"); // 대시보드로 이동
                dispatcher.forward(request, response);
            }
        } else {
            // [흐름 3] 임시 이용권도 없고, 로그인도 안 된 모든 비정상 접근
            response.sendRedirect(request.getContextPath() + "/loginForm.do");
        }
    }
}