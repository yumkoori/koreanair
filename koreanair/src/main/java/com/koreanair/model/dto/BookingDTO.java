package com.koreanair.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookingDTO {

	private String bookingId;
	private String flightId;
	private String userNo;
	private String promotionId;
	private String bookingPw;
}
