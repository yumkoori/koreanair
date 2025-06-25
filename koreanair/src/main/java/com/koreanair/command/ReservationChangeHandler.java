package com.koreanair.command;

import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.dto.User;
import com.koreanair.model.service.BookingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ReservationChangeHandler implements CommandHandler {

    private final BookingService bookingService = new BookingService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());

        switch (action) {
            case "/changeReservationSelect.do":
                return showSelectionPage(request, response);

            case "/changeReservationSearch.do":
                return showSearchPage(request, response);

            default:
                return "redirect:/dashboard.do";
        }
    }

    /**
     * 1단계: 변경할 여정/승객 선택 페이지를 보여주는 메소드
     */
    private String showSelectionPage(HttpServletRequest request, HttpServletResponse response) {
        // 1. 세션에서 로그인한 사용자 정보 가져오기
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 2. 비로그인 시 로그인 페이지로 리디렉션
        if (user == null) {
            return "redirect:/loginForm.do";
        }

        // 3. 요청에서 bookingId 가져오기
        String bookingId = request.getParameter("bookingId");
        
        // 4. 비회원 조회에서 넘어온 경우 예약 소유자 확인
        String guestBookingId = (String) session.getAttribute("guestBookingId");
        ReservationDTO reservation = null;
        
        if (guestBookingId != null && guestBookingId.equals(bookingId)) {
            // 비회원 조회에서 로그인으로 넘어온 경우, 예약 소유자 확인
            reservation = bookingService.getBookingDetailsById(bookingId, user.getUserId());
            if (reservation == null) {
                // 예약 정보가 로그인한 사용자와 일치하지 않음
                session.setAttribute("error", "변경하려는 예약이 회원님의 예약과 일치하지 않습니다. 본인의 예약만 변경할 수 있습니다.");
                // 비회원 조회 정보 제거
                session.removeAttribute("guestBookingId");
                session.removeAttribute("guestBookingInfo");
                session.removeAttribute("isGuestLookup");
                return "redirect:/dashboard.do";
            }
            // 일치하는 경우 비회원 조회 정보 제거
            session.removeAttribute("guestBookingId");
            session.removeAttribute("guestBookingInfo");
            session.removeAttribute("isGuestLookup");
        } else {
            // 일반적인 로그인 사용자 예약 변경
            reservation = bookingService.getBookingDetailsById(bookingId, user.getUserId());
        }

        // 5. 성공/실패 분기 처리
        if (reservation != null) {
            request.setAttribute("reservation", reservation);
            return "/WEB-INF/views/reservationSelect.jsp";
        } else {
            // 본인 예약이 아닐 경우
            session.setAttribute("error", "변경하려는 예약이 회원님의 예약과 일치하지 않습니다. 본인의 예약만 변경할 수 있습니다.");
            return "redirect:/dashboard.do";
        }
    }

    /**
     * 2단계: 새로운 항공편을 검색할 페이지를 보여주는 메소드
     */
    private String showSearchPage(HttpServletRequest request, HttpServletResponse response) {
        // 1. 세션에서 로그인한 사용자 정보 가져오기
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 2. 비로그인 시 로그인 페이지로 리디렉션
        if (user == null) {
            return "redirect:/loginForm.do";
        }

        // 3. 요청에서 bookingId 가져오기
        String bookingId = request.getParameter("bookingId");
        
        // 4. 비회원 조회에서 넘어온 경우 예약 소유자 확인
        String guestBookingId = (String) session.getAttribute("guestBookingId");
        ReservationDTO reservation = null;
        
        if (guestBookingId != null && guestBookingId.equals(bookingId)) {
            // 비회원 조회에서 로그인으로 넘어온 경우, 예약 소유자 확인
            reservation = bookingService.getBookingDetailsById(bookingId, user.getUserId());
            if (reservation == null) {
                // 예약 정보가 로그인한 사용자와 일치하지 않음
                session.setAttribute("error", "변경하려는 예약이 회원님의 예약과 일치하지 않습니다. 본인의 예약만 변경할 수 있습니다.");
                // 비회원 조회 정보 제거
                session.removeAttribute("guestBookingId");
                session.removeAttribute("guestBookingInfo");
                session.removeAttribute("isGuestLookup");
                return "redirect:/dashboard.do";
            }
            // 일치하는 경우 비회원 조회 정보 제거
            session.removeAttribute("guestBookingId");
            session.removeAttribute("guestBookingInfo");
            session.removeAttribute("isGuestLookup");
        } else {
            // 일반적인 로그인 사용자 예약 변경
            reservation = bookingService.getBookingDetailsById(bookingId, user.getUserId());
        }

        // 5. 성공/실패 분기 처리
        if (reservation != null) {
            request.setAttribute("reservation", reservation);
            return "/WEB-INF/views/reservationChange.jsp";
        } else {
            // 본인 예약이 아닐 경우
            session.setAttribute("error", "변경할 예약 정보를 찾을 수 없습니다.");
            return "redirect:/dashboard.do";
        }
    }
}