package com.koreanair.command;

import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.service.BookingService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckinApplyHandler implements CommandHandler {

    private final BookingService bookingService = new BookingService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 'checkinDetail.jsp'에서 '다음' 버튼을 눌렀을 때의 GET 요청만 처리합니다.
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            
            String bookingId = request.getParameter("bookingId");
            
            // 연락처 등 정보를 미리 채워주기 위해 예약 정보를 다시 조회합니다.
            // 비회원 체크인 시나리오를 가정하여 userId는 null로 전달합니다.
            ReservationDTO reservation = bookingService.getBookingDetailsById(bookingId, null); 

            if (reservation != null) {
                 // 조회된 예약 정보를 request에 담습니다.
                 request.setAttribute("reservation", reservation);
                 // checkinApply.jsp로 포워딩합니다.
                 return "/WEB-INF/views/checkinApply.jsp";
            }
        }
        
        // 예약 정보가 없거나 잘못된 접근(예: POST)이면 메인으로 리다이렉트합니다.
        request.getSession().setAttribute("error", "유효하지 않은 접근입니다.");
        return "redirect:/index.do";
    }
}