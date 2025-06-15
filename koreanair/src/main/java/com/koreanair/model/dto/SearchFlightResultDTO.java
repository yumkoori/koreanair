package com.koreanair.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchFlightResultDTO {
    private String flightId;             // 항공편 ID
    private String airlineName;          // 운항사명
    private LocalDateTime departureTime; // 출발시간
    private LocalDateTime arrivalTime;   // 도착시간
    private int durationMinutes;         // 소요시간(분)
    private int availableSeatCount;      // 사용가능 좌석 수
}
