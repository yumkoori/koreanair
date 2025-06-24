package com.koreanair.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PassengerDTO {
	private String passengerId;
	private Integer userNo;
	private String bookingId;
	private String firstName;
	private String lastName;
	private String birthDate;
	private String gender;
	private String type;
	
}
