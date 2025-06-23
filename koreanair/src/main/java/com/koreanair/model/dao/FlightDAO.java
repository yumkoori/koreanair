package com.koreanair.model.dao;

import java.util.List;

import java.util.Map;
import com.koreanair.model.dto.FlightSeatDTO;
import com.koreanair.model.dto.FareDTO;
import com.koreanair.model.dto.FlightDTO;
import com.koreanair.model.dto.SearchFlightDTO;
import com.koreanair.model.dto.SearchFlightResultDTO;
import com.koreanair.model.dto.SeatAvailabilityDTO;
import com.koreanair.model.dto.SeatMapDTO;


public interface FlightDAO {
    
    // --- 기존에 있던 메소드들 (수정하지 않음) ---
    void insertFlight(FlightDTO flight);
    void updateFlight(FlightDTO flight);
    void deleteFlight(String flightId);
    FlightDTO getFlightById(String flightId);
    

    List<FlightSeatDTO> getSeatMapByFlightIds(String flightId);
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

    // 항공편별 좌석 배치도 조회
    SeatMapDTO getSeatMapByFlightId(String flightId);
    
    // 항공편별 현재 좌석 예약 현황 조회 (예약된 좌석 목록 조회)
    List<SeatAvailabilityDTO> getReservedSeats(String flightId, int passengers);
    
    Map<String, Integer> getSeatsPrice(String flightId);
    
    List<SearchFlightResultDTO> getSearchFlight(SearchFlightDTO searchFlightDTO); 
    
    Map<String,Integer> getWeekLowPrices(SearchFlightDTO dto);
    
    void releaseExpiredPendingSeats();
}

