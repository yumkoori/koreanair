package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.dto.User;
import com.koreanair.model.service.BookingService;

public class HomeHandler implements CommandHandler {
    
    private BookingService bookingService = new BookingService();
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        switch (action) {
            case "/":
            case "/index.do":
                return showMainIndex(request, response);
            case "/dashboard.do":
                return showDashboard(request, response);
            
            // [수정] '/lookupForm.do' 요청을 처리하는 case 추가
            case "/lookupForm.do":
                return "/WEB-INF/views/lookupForm.jsp";
                
            case "/checkupForm.do":
                return handleCheckupForm(request, response);
                
            default:
                return showMainIndex(request, response);
        }
    }
    
    // 메인 인덱스 페이지 표시 (새로운 항공사 메인 페이지)
    private String showMainIndex(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        return "/index.jsp";
    }
    
    // 대시보드 표시
    private String showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        // AuthenticationFilter에서 이미 인증을 확인했으므로 바로 대시보드 표시
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/loginForm.do";
        }
        return "/views/login/dashboard.jsp";
    }
    
    // 체크인 폼 처리 (GET: 폼 표시, POST: 비회원 체크인 조회)
    private String handleCheckupForm(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            // GET 요청: 체크인 폼 표시
            return "/WEB-INF/views/checkupForm.jsp";
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            // POST 요청: 비회원 체크인 조회 처리
            String bookingId = request.getParameter("bookingId");
            String departureDate = request.getParameter("departureDate");
            String lastName = request.getParameter("lastName");
            String firstName = request.getParameter("firstName");
            
            // 비회원 체크인 조회
            ReservationDTO reservation = bookingService.searchBooking(bookingId, departureDate, lastName, firstName);
            
            if (reservation != null) {
                // 조회 성공: checkinDetail.jsp로 포워딩
                request.setAttribute("reservation", reservation);
                return "/WEB-INF/views/checkinDetail.jsp";
            } else {
                // 조회 실패: 에러 메시지와 함께 체크인 폼으로 다시
                request.setAttribute("error", "입력하신 정보와 일치하는 예약이 없습니다.");
                return "/WEB-INF/views/checkupForm.jsp";
            }
        }
        
        return "/WEB-INF/views/checkupForm.jsp";
    }
}