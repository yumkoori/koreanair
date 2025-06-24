package com.koreanair.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookingDTO {

	private String bookingId;
	private String outboundFlightId;
	private String returnFlightId;
	private Integer userNo;
	private String promotionId;
	private String bookingPw;
	private LocalDateTime expireTime;
}
