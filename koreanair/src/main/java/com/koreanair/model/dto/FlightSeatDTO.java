package com.koreanair.model.dto;

import lombok.Data;

@Data
public class FlightSeatDTO {
    private String seatId;      // seat_id (PK, VARCHAR(50))
    private String flightId;    // flight_id
    private String classId;     // class_id
    private String status;      // status (e.g., '예약 가능')
    private SeatClassDTO seatClass; // 좌석 등급 정보를 담기 위한 객체
}