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

public class SeatRevenueDTO {
	
	String seat_id;
	int total_revenue;
	double revenue_percent;
}
