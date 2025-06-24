package com.koreanair.model.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    
    // JSP에서 사용할 시간 포맷팅 메서드들
    public String getDepartureTimeFormatted() {
        if (departureTime != null) {
            return departureTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }
    
    public String getArrivalTimeFormatted() {
        if (arrivalTime != null) {
            return arrivalTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }
    
    // 소요시간을 "시간 분" 형태로 포맷팅
    public String getDurationFormatted() {
        if (durationMinutes <= 0) {
            return "소요시간 미정";
        }
        
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        
        if (hours > 0 && minutes > 0) {
            return hours + "시간 " + minutes + "분";
        } else if (hours > 0) {
            return hours + "시간";
        } else {
            return minutes + "분";
        }
    }
}
