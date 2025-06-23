package com.koreanair.model.dao;

import java.util.List;
import com.koreanair.model.dto.FareDTO;
import com.koreanair.model.dto.FlightDTO;
import com.koreanair.model.dto.FlightSeatDTO;

public interface FlightDAO {
    
    // --- 기존에 있던 메소드들 (수정하지 않음) ---
    void insertFlight(FlightDTO flight);
    void updateFlight(FlightDTO flight);
    void deleteFlight(String flightId);
    FlightDTO getFlightById(String flightId);
    List<FlightSeatDTO> getSeatMapByFlightId(String flightId);
    List<String> getReservedSeats(String flightId);
    void updateFare(String flightId, FareDTO fare);
    
    // ================================================================
    // ▼▼▼ 이전에 추가했던 assignSeatToCheckin 대신, 아래 3개의 메소드를 정의해야 합니다. ▼▼▼
    // ================================================================

    /**
     * 예약 ID로 이미 배정된 좌석이 있는지 확인합니다.
     */
    boolean isSeatAssigned(String bookingId);

    /**
     * 기존에 배정된 좌석을 새로운 좌석으로 변경합니다. (UPDATE)
     */
    boolean updateSeatForBooking(String bookingId, String newFlightSeatId, String newBookingSeatId);

    /**
     * 최초로 좌석을 배정합니다. (INSERT)
     */
    boolean insertSeatForBooking(String bookingId, String flightSeatId, String bookingSeatId);
    boolean deleteSeatAssignment(String bookingId);
}