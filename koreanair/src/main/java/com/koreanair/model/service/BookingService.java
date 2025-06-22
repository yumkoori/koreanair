package com.koreanair.model.service;

import java.sql.SQLException;

import com.koreanair.model.dao.BookingDAO;
import com.koreanair.model.dao.ReservationDAO;
import com.koreanair.model.dao.ReservationDAOImpl; // 구현 클래스 import 추가
import com.koreanair.model.dto.BookingDTO;
import com.koreanair.model.dto.ReservationDTO;

public class BookingService {

    // 실제 구현 클래스(ReservationDAOImpl)의 인스턴스를 생성하도록 수정
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final BookingDAO bookingDAO = new BookingDAO();
    
    public ReservationDTO searchBooking(String bookingId, String departureDate, String lastName, String firstName) {
        // DAO에 정의된 메소드를 호출
        return reservationDAO.findReservation(bookingId, departureDate, lastName, firstName);
    }
    
    // [추가] 예약 ID로 상세 정보를 조회하는 서비스 메소드
    public ReservationDTO getBookingDetailsById(String bookingId) {
        return reservationDAO.findReservationById(bookingId);
    }
    
    
    public String saveBookingToPending(BookingDTO dto) {
    	return bookingDAO.saveBooking(dto);

    }
    
    public void updateSeatStatusToPending(String flightId, String seatClass, int totalPassenger) {
    	try {
			bookingDAO.updateSeatToPending(flightId, seatClass, totalPassenger);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}