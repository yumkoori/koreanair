package com.koreanair.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PopularDTO {
	
	String departure_airport_id;
	String arrival_airport_id;
	int count;
	double route_percent; // 노선별 예약 비율 추가

}
