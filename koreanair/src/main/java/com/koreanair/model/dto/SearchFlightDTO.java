package com.koreanair.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFlightDTO {
	//사용자가 검색한 항공편 input값
    private String departure;      // 출발지 (예: 구마모토)
    private String arrival;        // 도착지 (예: 밀라노)
    private String departureDate;  // 출발일 (yyyy-MM-dd)
    private String returnDate;     // 귀국일 (yyyy-MM-dd, 왕복일 경우 필수)
    private int passengers;     // 탑승객 정보 (예: 성인 1명)
    private String seatClass;      // 좌석 등급 (예: 일반석)
    private String tripType;       // 여정 유형 (oneway / round)
}
