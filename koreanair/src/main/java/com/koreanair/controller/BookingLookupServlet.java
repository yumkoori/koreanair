package com.koreanair.controller;

// ▼▼▼ json-lib 라이브러리의 클래스를 import 합니다. ▼▼▼
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.ReservationDTO;
import com.koreanair.model.service.BookingService;

import net.sf.json.JSONObject; // json-lib 라이브러리의 클래스

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
        
        // ▼▼▼ [수정된 부분] json-lib를 사용하여 JSON 응답을 생성합니다. ▼▼▼
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();

        if (bookingInfo != null) {
            // [성공]
            HttpSession session = request.getSession();
            session.setAttribute("lookupResult", bookingInfo);
            
            jsonResponse.put("success", true);
            jsonResponse.put("redirectUrl", "reservationDetail?bookingId=" + bookingInfo.getBookingId());
        } else {
            // [실패]
            jsonResponse.put("success", false);
            jsonResponse.put("error", "입력하신 정보와 일치하는 예약이 없습니다. 다시 확인해 주세요.");
        }

        // Map 객체를 json-lib의 JSONObject로 변환합니다.
        JSONObject jsonObject = JSONObject.fromObject(jsonResponse);

        // 생성된 JSON 문자열을 클라이언트(브라우저)에 응답합니다.
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
        out.flush();
        // ▲▲▲ 여기까지 수정 ▲▲▲
    }
}