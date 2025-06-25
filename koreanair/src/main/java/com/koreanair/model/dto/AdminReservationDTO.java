package com.koreanair.model.dto;

import java.time.LocalTime;

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

public class AdminReservationDTO {
	
	String booking_id;
	String user_name;
	String email;
	String phone;
	String start;
	String end;
	String startDate;
	String bookingDate;
	String status;
	String seat_class;
	int passenger;
	int totalPrice;
	LocalTime expire_time;
	String endDate;
	String flightNO;
	String birth_date;
}
