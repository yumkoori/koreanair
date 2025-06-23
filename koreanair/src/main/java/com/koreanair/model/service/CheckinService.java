package com.koreanair.model.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.koreanair.model.dao.CheckinDAO;
import com.koreanair.model.dao.CheckinDAOImpl;
import com.koreanair.model.dao.FlightDAO;
import com.koreanair.model.dao.FlightDAOImpl;
import com.koreanair.model.dto.CheckinDTO;
import com.koreanair.model.dto.FlightSeatDTO;

public class CheckinService {
    private FlightDAO flightDAO = new FlightDAOImpl();
    private CheckinDAO checkinDAO = new CheckinDAOImpl();

    public List<FlightSeatDTO> getSeatMapForCheckin(String flightId) {
        // 이 메소드는 수정할 필요 없이 완벽하게 동작합니다.
        List<FlightSeatDTO> allSeats = flightDAO.getSeatMapByFlightId(flightId);
        List<String> reservedSeatIds = flightDAO.getReservedSeats(flightId);
        Map<String, FlightSeatDTO> seatMap = allSeats.stream()
                .collect(Collectors.toMap(FlightSeatDTO::getSeatId, seat -> seat));
        for (String reservedId : reservedSeatIds) {
            if (seatMap.containsKey(reservedId)) {
                seatMap.get(reservedId).setStatus("RESERVED");
            }
        }
        return allSeats;
    }

    /**
     * [최종 수정본] 좌석 선택 및 체크인을 최종 처리하고, 결과 상태를 int로 반환합니다.
     * @return 0: 실패, 1: 최초 좌석 지정 성공, 2: 좌석 변경 성공
     */
    public int selectSeatAndCompleteCheckin(String bookingId, String flightId, String seatId) {

        boolean alreadyAssigned = flightDAO.isSeatAssigned(bookingId);

        if (alreadyAssigned) {
            // [수정] 1. (자식) check_in 테이블 정보 삭제
            if (!checkinDAO.deleteCheckin(bookingId)) {
                System.out.println("[ERROR - CheckinService] 기존 체크인 정보 삭제 실패");
                return 0; // 실패
            }
            
            // [수정] 2. (부모) booking_seat 테이블 정보 삭제
            if (!flightDAO.deleteSeatAssignment(bookingId)) {
                System.out.println("[ERROR - CheckinService] 기존 좌석 정보 삭제 실패");
                return 0; // 실패
            }
        }

        // [공통 로직] 3. (부모) booking_seat 테이블에 새로운 좌석 정보 추가
        System.out.println("[DEBUG - CheckinService] 새로운 좌석 배정(INSERT)을 시도합니다.");
        String newBookingSeatId = bookingId + "_" + seatId;
        String newFlightSeatId = "FS-" + flightId + "-" + seatId;
        if (!flightDAO.insertSeatForBooking(bookingId, newFlightSeatId, newBookingSeatId)) {
            System.out.println("[ERROR - CheckinService] 새로운 좌석 배정 실패");
            return 0; // 실패
        }

        // 4. (자식) check_in 테이블에 새로운 체크인 정보 추가
        System.out.println("[DEBUG - CheckinService] check_in 레코드 생성.");
        CheckinDTO checkinData = new CheckinDTO();
        checkinData.setCheckIn(bookingId + "_CHK");
        checkinData.setBookingSeatId(newBookingSeatId);
        checkinData.setCheckInType("WEB");
        checkinData.setCheckInStatus("COMPLETED");
        checkinData.setBaggageCount(0);
        checkinData.setBaggageTagNumber(null);
        
        if (!checkinDAO.createCheckin(checkinData)) {
            System.out.println("[ERROR - CheckinService] 새로운 체크인 정보 생성 실패");
            return 0; // 실패
        }
        
        // 모든 과정이 성공했을 때, 상태에 따라 다른 성공 코드를 반환
        return alreadyAssigned ? 2 : 1; // alreadyAssigned가 true이면 '변경'(2), false이면 '최초'(1)
    }
}