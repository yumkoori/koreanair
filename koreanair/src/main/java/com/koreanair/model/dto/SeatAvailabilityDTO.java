package com.koreanair.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatAvailabilityDTO {
	
    private String classId;            // 좌석 등급 코드 (e.g. ECON, PREM 등)
    private String className;          // 좌석 등급명 (이코노미, 프레스티지 등)
    private String detailClassName;    // 좌석 상세명
    private int availableSeatCount;    // 사용가능 좌석 수
    private int price;
}
