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
        
        String method = request.getMethod();
        
        if ("POST".equalsIgnoreCase(method)) {
            // POST 요청: 비회원 체크인 조회
            return handleGuestCheckin(request, response);
        } else {
            // GET 요청: 로그인 사용자 체크인
            return handleUserCheckin(request, response);
        }
    }
    
    // 비회원 체크인 조회 처리
    private String handleGuestCheckin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String bookingId = request.getParameter("bookingId");
        String departureDate = request.getParameter("departureDate");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String ajax = request.getParameter("ajax");
        
        ReservationDTO reservation = bookingService.searchBooking(bookingId, departureDate, lastName, firstName);
        
        // AJAX 요청인 경우 JSON 응답
        if ("true".equals(ajax)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            try (java.io.PrintWriter out = response.getWriter()) {
                if (reservation != null) {
                    // 조회 성공: 세션에 저장하고 리다이렉트 URL 반환
                    request.getSession().setAttribute("checkinResult", reservation);
                    out.print("{\"success\":true,\"redirectUrl\":\"checkinDetail.do\"}");
                } else {
                    // 조회 실패: 에러 메시지 반환
                    out.print("{\"success\":false,\"error\":\"입력하신 정보와 일치하는 예약이 없습니다. 다시 확인해 주세요.\"}");
                }
            }
            return null; // JSON 응답이므로 JSP로 포워딩하지 않음
        }
        
        // 일반 폼 제출인 경우 기존 방식
        if (reservation != null) {
            // 조회 성공: checkinDetail.jsp로 포워딩
            request.setAttribute("reservation", reservation);
            return "/WEB-INF/views/checkinDetail.jsp";
        } else {
            // 조회 실패: 에러 메시지와 함께 메인 페이지로 리다이렉트
            request.getSession().setAttribute("checkinError", "입력하신 정보와 일치하는 예약이 없습니다.");
            return "redirect:/index.do";
        }
    }
    
    // 로그인 사용자 체크인 처리
    private String handleUserCheckin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false); // 세션이 없으면 새로 만들지 않음
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        ReservationDTO reservation = null;

        // 체크인 조회 결과가 세션에 있는지 확인 (AJAX 조회에서 넘어온 경우)
        ReservationDTO checkinResult = (session != null) ? (ReservationDTO) session.getAttribute("checkinResult") : null;
        if (checkinResult != null) {
            // AJAX 체크인 조회에서 넘어온 경우
            reservation = checkinResult;
            // checkinResult는 예매내역 조회에서도 사용하므로 제거하지 않음
        } else {
            // [분기 처리] 로그인 상태인지 확인
            if (user != null) {
            // --- 1. 로그인 사용자 체크인 처리 ---
            String bookingId = request.getParameter("bookingId");
            
            // 비회원 조회에서 넘어온 경우 예약 소유자 확인
            String guestBookingId = (String) session.getAttribute("guestBookingId");
            if (guestBookingId != null && guestBookingId.equals(bookingId)) {
                // 비회원 조회에서 로그인으로 넘어온 경우, 예약 소유자 확인
                reservation = bookingService.getBookingDetailsById(bookingId, user.getUserId());
                // 비회원 조회 정보 제거
                session.removeAttribute("guestBookingId");
                session.removeAttribute("guestBookingInfo");
                session.removeAttribute("isGuestLookup");
            } else {
                // 일반적인 로그인 사용자 체크인
                reservation = bookingService.getBookingDetailsById(bookingId, user.getUserId());
            }

            } else {
                // --- 2. 비회원 사용자 체크인 처리 ---
                String bookingId = request.getParameter("bookingId");
                String departureDate = request.getParameter("departureDate");
                String lastName = request.getParameter("lastName");
                String firstName = request.getParameter("firstName");
                
                reservation = bookingService.searchBooking(bookingId, departureDate, lastName, firstName);
            }
        }

        // --- 공통 후처리 ---
        if (reservation != null) {
            // 조회 성공 시, request에 정보를 담아 jsp로 포워딩
            request.setAttribute("reservation", reservation);
            
            // 비회원 체크인에서 온 경우 세션에 표시
            if (user == null) {
                session.setAttribute("fromGuestCheckin", true);
                session.setAttribute("guestCheckinBookingId", reservation.getBookingId());
            }
            
            return "/WEB-INF/views/checkinDetail.jsp";
        } else {
            // 조회 실패 시, 에러 메시지와 함께 적절한 페이지로 리다이렉트
            if (user != null) {
                // 로그인 사용자의 경우 대시보드로 리다이렉트
                request.getSession().setAttribute("error", "체크인하려는 예약이 회원님의 예약과 일치하지 않습니다. 본인의 예약만 체크인할 수 있습니다.");
                return "redirect:/dashboard.do";
            } else {
                // 비회원의 경우 메인 페이지로 리다이렉트
                request.getSession().setAttribute("lookupError", "입력하신 정보와 일치하는 예약이 없습니다.");
                return "redirect:/index.do";
            }
        }
    }
}