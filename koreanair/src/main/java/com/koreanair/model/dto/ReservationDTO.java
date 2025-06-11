package com.koreanair.model.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    // --- 기존 필드 ---
    private String bookingId;
    private String flightId;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private String lastName;
    private String firstName;
    private String departureAirportId;
    private String departureAirportName;
    private String arrivalAirportId;
    private String arrivalAirportName;

    // --- [추가된 필드] ---
    private String flightName;      // 비행기 편명 (flight_id로 대체)
    private String aircraftType;    // 기종 (DB에 없어 조회 불가)
    private String cabinClass;      // 좌석 등급
    private String phone;           // 연락처
    private String email;           // 이메일
    private String memberId;        // 회원번호
    private String ticketNumber;    // 항공권 번호 (DB에 없어 조회 불가)
}