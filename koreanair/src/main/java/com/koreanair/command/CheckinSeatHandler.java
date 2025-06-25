package com.koreanair.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.FlightSeatDTO;
import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.dto.User;
import com.koreanair.model.service.BookingService;
import com.koreanair.model.service.CheckinService;

public class CheckinSeatHandler implements CommandHandler {

    private final CheckinService checkinService = new CheckinService();
    private final BookingService bookingService = new BookingService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return showSeatSelectionForm(request, response);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            return processSeatSelection(request, response);
        }
        return "/index.do";
    }
    
    private String showSeatSelectionForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String bookingId = request.getParameter("bookingId");

        // ▼▼▼ [수정/추가] 로그인 사용자 정보 확인 및 예약 소유주 검증 로직 ▼▼▼
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 비정상적인 접근 (필터에서 처리되지만, 핸들러에서도 방어 코드 추가)
        if (user == null) {
            return "redirect:/loginForm.do";
        }

        // 비회원 조회에서 넘어온 경우 예약 소유자 확인
        String guestBookingId = (String) session.getAttribute("guestBookingId");
        ReservationDTO reservation = null;
        
        if (guestBookingId != null && guestBookingId.equals(bookingId)) {
            // 비회원 조회에서 로그인으로 넘어온 경우, 예약 소유자 확인
            reservation = bookingService.getBookingDetailsById(bookingId, user.getUserId());
            if (reservation == null) {
                // 예약 정보가 로그인한 사용자와 일치하지 않음
                session.setAttribute("error", "좌석을 변경하려는 예약이 회원님의 예약과 일치하지 않습니다. 본인의 예약만 좌석 변경할 수 있습니다.");
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
            // 일반적인 로그인 사용자 좌석 변경
            reservation = bookingService.getBookingDetailsById(bookingId, user.getUserId());
            // 검증 실패: 예약이 없거나, 다른 사람의 예약을 변경하려는 경우
            if (reservation == null) {
                session.setAttribute("error", "좌석을 변경하려는 예약이 회원님의 예약과 일치하지 않습니다. 본인의 예약만 좌석 변경할 수 있습니다.");
                return "redirect:/dashboard.do";
            }
        }
        // ▲▲▲ [수정/추가] 여기까지 ▲▲▲

        // 검증 성공: 아래 로직은 기존과 동일
        List<FlightSeatDTO> seatMap = checkinService.getSeatMapForCheckin(reservation.getFlightId());
        request.setAttribute("reservation", reservation);
        request.setAttribute("seatMap", seatMap);
        return "/WEB-INF/views/checkinSeat.jsp";
    }

    /**
     * [최종 수정본] 서비스의 반환값에 따라 분기 처리하여 다른 메시지와 리디렉션 경로를 설정합니다.
     */
    private String processSeatSelection(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String bookingId = request.getParameter("bookingId");
        String seatId = request.getParameter("seatId");
        String flightId = request.getParameter("flightId");

        // [로그 추가] 핸들러가 받은 파라미터 값 확인
        System.out.println("[DEBUG - CheckinSeatHandler] 요청 수신. bookingId: " + bookingId + ", seatId: " + seatId);
        System.out.println("[DEBUG - CheckinSeatHandler] 요청 수신. bookingId: " + bookingId + ", flightId: " + flightId + ", seatId: " + seatId);

        if (seatId == null || seatId.trim().isEmpty()) {
            request.setAttribute("error", "좌석을 선택해주세요.");
            return showSeatSelectionForm(request, response);
        }

        // 서비스의 반환값(int)을 받음
        int result = checkinService.selectSeatAndCompleteCheckin(bookingId, flightId, seatId);
        
        // [로그 추가] 서비스 로직 최종 결과 확인
        System.out.println("[DEBUG - CheckinSeatHandler] 최종 체크인 성공 여부 (1: 최초, 2: 변경): " + result);

        HttpSession session = request.getSession();

        switch (result) {
            case 1: // 최초 좌석 지정 성공
                session.setAttribute("message", "좌석 예약 완료!");
                return "redirect:/index.do"; // 메인 페이지로 이동

            case 2: // 좌석 변경 성공
                session.setAttribute("message", "좌석 변경이 완료되었습니다.");
                // 예약 상세 페이지로 다시 돌아가기 위해 bookingId 파라미터 추가
                return "redirect:/reservationDetail?bookingId=" + bookingId; 

            default: // 실패 (case 0)
                request.setAttribute("error", "좌석 선택 중 오류가 발생했습니다. 다른 좌석을 선택해주세요.");
                return showSeatSelectionForm(request, response); // 다시 좌석 선택 폼 보여주기
        }
    }
}