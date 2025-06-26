package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.RefundDetailDTO;
import com.koreanair.model.dto.User;
import com.koreanair.model.service.BookingService;

public class CancelReservationHandler implements CommandHandler {
    
    private BookingService bookingService = new BookingService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        
        // GET 요청: 환불 신청 폼을 보여줌
        if ("GET".equalsIgnoreCase(method)) {
            return processForm(request, response);
        } 
        // POST 요청: 실제 환불 처리를 진행함
        else if ("POST".equalsIgnoreCase(method)) {
            return processSubmit(request, response);
        }
        
        // 잘못된 접근은 홈페이지로 리다이렉트
        return "redirect:/index.do";
    }
    
    /**
     * 환불 신청 폼을 보여주는 메소드
     */
    private String processForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 비로그인 사용자는 로그인 페이지로
        if (user == null) {
            return "redirect:/loginForm.do";
        }

        String bookingId = request.getParameter("bookingId");
        
        // 서비스 계층을 통해 환불에 필요한 예약 정보 및 계산된 금액을 가져옴
        RefundDetailDTO refundDetail = bookingService.getBookingAndRefundDetailsV2(bookingId, user.getUserId());
        
        if (refundDetail != null) {
            // JSP에서 사용할 수 있도록 request에 정보 저장
            // refundDetail 객체는 reservation 정보와 refund 정보(운임, 세금, 위약금 등)를 모두 포함
            request.setAttribute("refundDetail", refundDetail);
            request.setAttribute("reservation", refundDetail.getReservation()); // JSP 호환성을 위해 기존 reservation 객체도 전달
            return "/WEB-INF/views/cancelReservationForm.jsp";
        } else {
            // 본인 예약이 아니거나 존재하지 않는 예약
            session.setAttribute("error", "취소하려는 예약이 회원님의 예약과 일치하지 않습니다. 본인의 예약만 취소할 수 있습니다.");
            return "redirect:/dashboard.do";
        }
    }

    /**
     * 환불 신청(예약 취소)을 처리하는 메소드
     */
    private String processSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        // 비로그인 사용자의 POST 요청 방지
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/loginForm.do";
        }

        String bookingId = request.getParameter("bookingId");
        
        // 서비스 계층의 예약 취소 로직 호출
        boolean success = bookingService.cancelBooking(bookingId);
        
        if (success) {
            // 성공 시 메시지와 함께 대시보드로 리다이렉트
            // 실제 운영 시에는 '환불이 정상적으로 접수되었습니다'와 같은 메시지를 포함한 별도의 완료 페이지로 이동하는 것이 좋습니다.
            return "redirect:/dashboard.do?message=cancelSuccess";
        } else {
            // 실패 시, 다시 환불 폼으로 돌려보내고 에러 메시지 표시
            request.setAttribute("error", "예약 취소 중 오류가 발생했습니다. 다시 시도해 주세요.");
            // 실패 시에도 폼을 다시 보여주기 위해 필요한 데이터를 다시 조회해야 함
            return processForm(request, response); 
        }
    }
}