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
public class FlightSeatSaveDTO {
	String seat_id;
	String flight_id;  // aircraft
	String class_id;
	String status;
	
	
	String classseat;
	String aircraft;
	int row;
	String seat;
	int price;
	String planeType;
	String seatClass;  // JavaScript에서 전송하는 class 필드를 받기 위한 필드 추가
}
