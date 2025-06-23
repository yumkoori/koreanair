package com.koreanair.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class AirportDTO {
    private String airportId;        // 공항 코드 (ICN, GMP 등)
    private String airportName;      // 공항명 (한글)
    private String airportEngName;   // 공항명 (영문)
    private String cityKor;          // 도시명 (한글)
    private String cityEng;          // 도시명 (영문)
}
