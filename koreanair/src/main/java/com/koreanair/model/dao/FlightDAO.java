package com.koreanair.model.dao;

import java.util.List;
import java.util.Map;

import com.koreanair.model.dto.FareDTO;
import com.koreanair.model.dto.FlightDTO;
import com.koreanair.model.dto.SearchFlightDTO;
import com.koreanair.model.dto.SearchFlightResultDTO;
import com.koreanair.model.dto.SeatAvailabilityDTO;
import com.koreanair.model.dto.SeatMapDTO;

public interface FlightDAO {
	
	// 항공편 등록
    void insertFlight(FlightDTO flight);
	
    // 항공편 수정
    void updateFlight(FlightDTO flight);
    
    // 항공편 삭제
    void deleteFlight(String flightId);

    // 특정 항공편 상세 조회 
    FlightDTO getFlightById(String flightId);
    
    // 항공편별 좌석 배치도 조회
    SeatMapDTO getSeatMapByFlightId(String flightId);
    
    // 항공편별 현재 좌석 예약 현황 조회 (예약된 좌석 목록 조회)
    List<SeatAvailabilityDTO> getReservedSeats(String flightId);
    
    Map<String, Integer> getSeatsPrice(String flightId);
    
    // 항공편 가격 설정 (운임, 유류할증료, 수수료 등)
    void updateFare(String flightId, FareDTO fare);
    
    List<SearchFlightResultDTO> getSearchFlight(SearchFlightDTO searchFlightDTO); 
}
